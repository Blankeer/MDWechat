package com.blanke.mdwechat.widget

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.blanke.mdwechat.R


class MaterialSearchView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyle: Int = -1) : FrameLayout(mContext, attrs, defStyle), View.OnClickListener {

    interface onSearchListener {
        fun onSearch(query: String)

        fun searchViewOpened()

        fun searchViewClosed()

        fun onCancelSearch()
    }

    open class SimpleonSearchListener : onSearchListener {

        override fun onSearch(query: String) {

        }

        override fun searchViewOpened() {

        }

        override fun searchViewClosed() {

        }

        override fun onCancelSearch() {

        }
    }

    val searchView: EditText
    private val mClearSearch: ImageView
    private var mOnSearchListener: onSearchListener? = null
    private val cardLayout: FrameLayout
    private val backArrowImg: ImageView
    private val noResultsFoundText: TextView? = null

    fun setHintText(hint: String) {
        searchView.hint = hint
    }

    init {
        cardLayout = FrameLayout(mContext)
        val gd = GradientDrawable()
        gd.setColor(Color.WHITE)
        gd.cornerRadius = dp2px(2f).toFloat()
        cardLayout.background = gd

        val contentLayout = LinearLayout(mContext)
        contentLayout.orientation = LinearLayout.HORIZONTAL

        backArrowImg = ImageView(mContext)
//        backArrowImg.isClickable = true
//        backArrowImg.background = ContextCompat.getDrawable(context, android.R.attr.selectableItemBackgroundBorderless)
        backArrowImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_back))
        val p = dp2px(8f)
        backArrowImg.setPadding(p, p, p, p)
        //        int w = 48;
        val backParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        contentLayout.addView(backArrowImg, backParams)

        searchView = EditText(mContext)
        searchView.gravity = Gravity.CENTER_VERTICAL
        searchView.imeOptions = IME_ACTION_SEARCH
        searchView.setPadding(p, 0, p, 0)
        searchView.setLines(1)
        searchView.setBackgroundColor(Color.WHITE)
        searchView.setTextColor(Color.BLACK)
        searchView.hint = "搜索"
        searchView.setHintTextColor(Color.GRAY)
        val editParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        editParams.weight = 1f
        contentLayout.addView(searchView, editParams)

        mClearSearch = ImageView(mContext)
//        mClearSearch.isClickable = true
//        mClearSearch.background = ContextCompat.getDrawable(context, android.R.attr.selectableItemBackgroundBorderless)
        mClearSearch.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_clear))
        mClearSearch.visibility = View.INVISIBLE
        mClearSearch.setPadding(p, p, p, p)
        val clearParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        contentLayout.addView(mClearSearch, clearParams)

        cardLayout.addView(contentLayout)
        val cardParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        cardParams.bottomMargin = dp2px(6f)
        cardParams.topMargin = cardParams.bottomMargin
        cardParams.rightMargin = cardParams.topMargin
        cardParams.leftMargin = cardParams.rightMargin
        addView(cardLayout, cardParams)

        searchView.inputType = InputType.TYPE_CLASS_TEXT

        mClearSearch.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        backArrowImg.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //                if (mOnSearchListener != null) {
                //                    mOnSearchListener.onSearch(getSearchQuery());
                //                }
                toggleClearSearchButton(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        searchView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)) {
                val query = searchQuery
                if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                    mOnSearchListener!!.onSearch(query)
                }
                return@OnKeyListener true
            }
            false
        })
        backArrowImg.setOnClickListener(this)
        mClearSearch.setOnClickListener(this)
        visibility = View.GONE
        clearAnimation()
    }

    fun setOnSearchListener(l: onSearchListener) {
        mOnSearchListener = l
    }

    var searchQuery: String
        get() = if (searchView.text != null) searchView.text.toString() else ""
        set(query) {
            searchView.setText(query)
            toggleClearSearchButton(query)
        }

    var isSearchViewVisible: Boolean = visibility == View.VISIBLE

    // Show the SearchView
    fun show() {
        if (isSearchViewVisible) return
        isSearchViewVisible = true
        visibility = View.VISIBLE
        if (mOnSearchListener != null) {
            mOnSearchListener!!.searchViewOpened()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val animator = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.width - 40,
                    cardLayout.height / 2,
                    0f,
                    Math.hypot(cardLayout.width.toDouble(), cardLayout.height.toDouble()).toFloat())
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    showKeyboard(searchView)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            cardLayout.visibility = View.VISIBLE
            if (cardLayout.visibility == View.VISIBLE) {
                animator.duration = 300
                animator.start()
                cardLayout.isEnabled = true
            }
        } else {
            cardLayout.visibility = View.VISIBLE
            cardLayout.isEnabled = true
            showKeyboard(searchView)
        }

    }

    fun hide() {
        if (!isSearchViewVisible) return
        isSearchViewVisible = false
        if (mOnSearchListener != null) {
            mOnSearchListener!!.searchViewClosed()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val animatorHide = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.width - 40,
                    cardLayout.height / 2,
                    Math.hypot(cardLayout.width.toDouble(), cardLayout.height.toDouble()).toFloat(),
                    0f)
            animatorHide.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    hideKeyboard(searchView)
                }

                override fun onAnimationEnd(animation: Animator) {
                    cardLayout.visibility = View.GONE
                    clearSearch()
                    visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            animatorHide.duration = 300
            animatorHide.start()
        } else {
            hideKeyboard(searchView)
            cardLayout.visibility = View.GONE
            clearSearch()
            visibility = View.GONE
        }
    }

    //    public static WindowManager.LayoutParams getSearchViewLayoutParams(final Activity activity) {
    //        final Rect rect = new Rect();
    //        final Window window = activity.getWindow();
    //        window.getDecorView().getWindowVisibleDisplayFrame(rect);
    //        final int statusBarHeight = rect.top;
    //
    //        final TypedArray actionBarSize = activity.getTheme().obtainStyledAttributes(
    //                new int[]{R.attr.actionBarSize});
    //        actionBarSize.recycle();
    //        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
    //                rect.right /* This ensures we don't go under the navigation bar in landscape */,
    //                WindowManager.LayoutParams.WRAP_CONTENT,
    //                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
    //                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
    //                PixelFormat.TRANSLUCENT);
    //
    //        params.gravity = Gravity.TOP | Gravity.START;
    //        params.x = 0;
    //        params.y = statusBarHeight;
    //        return params;
    //    }

    private fun toggleClearSearchButton(query: CharSequence) {
        mClearSearch.visibility = if (!TextUtils.isEmpty(query)) View.VISIBLE else View.INVISIBLE
    }

    private fun clearSearch() {
        searchView.setText("")
        mClearSearch.visibility = View.INVISIBLE
    }

    private fun onCancelSearch() {
        if (mOnSearchListener != null) {
            mOnSearchListener!!.onCancelSearch()
        }
        hide()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_BACK) {
            onCancelSearch()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onClick(v: View) {
        if (v === backArrowImg) {
            onCancelSearch()
        } else if (v === mClearSearch) {
            clearSearch()
        }
    }

    protected fun dp2px(dp: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun showKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        view.isFocusableInTouchMode = true
        view.isFocusable = true
        imm.showSoftInput(view, 0)
    }

    fun hideKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}