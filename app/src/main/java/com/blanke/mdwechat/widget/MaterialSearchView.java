package com.blanke.mdwechat.widget;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blanke.mdwechat.R;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;


public class MaterialSearchView extends FrameLayout implements View.OnClickListener {
    private final InputMethodManager inputMethodManager;

    public interface onSearchListener {
        void onSearch(String query);

        void searchViewOpened();

        void searchViewClosed();

        void onCancelSearch();
    }

    public static class SimpleonSearchListener implements onSearchListener {

        @Override
        public void onSearch(String query) {

        }

        @Override
        public void searchViewOpened() {

        }

        @Override
        public void searchViewClosed() {

        }

        @Override
        public void onCancelSearch() {

        }
    }

    private final EditText mSearchEditText;
    private final ImageView mClearSearch;
    private onSearchListener mOnSearchListener;
    private CardView cardLayout;
    private ImageView backArrowImg;
    private Context mContext;
    private TextView noResultsFoundText;

    public void setHintText(String hint) {
        mSearchEditText.setHint(hint);
    }

    public CardView getCardLayout() {
        return cardLayout;
    }

    public MaterialSearchView(final Context context) {
        this(context, null);
    }

    public MaterialSearchView(final Context context, final AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MaterialSearchView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        cardLayout = new CardView(context);
        cardLayout.setCardBackgroundColor(Color.WHITE);
        cardLayout.setRadius(2);

        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.HORIZONTAL);

        backArrowImg = new ImageView(context);
//        backArrowImg.setClickable(true);
//        backArrowImg.setBackground(ContextCompat.getDrawable(context, android.R.attr.selectableItemBackgroundBorderless));
        backArrowImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_back));
        int p = 12;
        backArrowImg.setPadding(p, p, p, p);
//        int w = 48;
        LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout.addView(backArrowImg, backParams);

        mSearchEditText = new EditText(context);
        mSearchEditText.setGravity(Gravity.CENTER_VERTICAL);
        mSearchEditText.setImeOptions(IME_ACTION_SEARCH);
        mSearchEditText.setPadding(p, 0, p, 0);
        mSearchEditText.setLines(1);
        mSearchEditText.setBackgroundColor(Color.WHITE);
        mSearchEditText.setTextColor(Color.BLACK);
        mSearchEditText.setHint("搜索");
        mSearchEditText.setHintTextColor(Color.GRAY);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        editParams.weight = 1;
        contentLayout.addView(mSearchEditText, editParams);

        mClearSearch = new ImageView(context);
//        mClearSearch.setClickable(true);
//        mClearSearch.setBackground(ContextCompat.getDrawable(context, android.R.attr.selectableItemBackgroundBorderless));
        mClearSearch.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_clear));
        mClearSearch.setVisibility(INVISIBLE);
        mClearSearch.setPadding(p, p, p, p);
        LinearLayout.LayoutParams clearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout.addView(mClearSearch, clearParams);

        cardLayout.addView(contentLayout);
        FrameLayout.LayoutParams cardParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cardParams.leftMargin = cardParams.rightMargin = cardParams.topMargin = cardParams.bottomMargin = 6;
        addView(cardLayout, cardParams);

        mSearchEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        mClearSearch.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        backArrowImg.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (mOnSearchListener != null) {
//                    mOnSearchListener.onSearch(getSearchQuery());
//                }
                toggleClearSearchButton(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSearchEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER ||
                                keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    final String query = getSearchQuery();
                    if (!TextUtils.isEmpty(query) && mOnSearchListener != null) {
                        mOnSearchListener.onSearch(query);
                    }
                    return true;
                }
                return false;
            }
        });
        backArrowImg.setOnClickListener(this);
        mClearSearch.setOnClickListener(this);
        setVisibility(View.GONE);
        clearAnimation();

        inputMethodManager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
    }

    public void setOnSearchListener(final onSearchListener l) {
        mOnSearchListener = l;
    }

    public void setSearchQuery(final String query) {
        mSearchEditText.setText(query);
        toggleClearSearchButton(query);
    }

    public String getSearchQuery() {
        return mSearchEditText.getText() != null ? mSearchEditText.getText().toString() : "";
    }

    public boolean isSearchViewVisible() {
        return getVisibility() == View.VISIBLE;
    }

    // Show the SearchView
    public void show() {
        if (isSearchViewVisible()) return;
        setVisibility(View.VISIBLE);
        if (mOnSearchListener != null) {
            mOnSearchListener.searchViewOpened();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.getWidth() - 20,
                    cardLayout.getHeight()/2,
                    0,
                    (float) Math.hypot(cardLayout.getWidth(), cardLayout.getHeight()));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchEditText.requestFocus();
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            cardLayout.setVisibility(View.VISIBLE);
            if (cardLayout.getVisibility() == View.VISIBLE) {
                animator.setDuration(300);
                animator.start();
                cardLayout.setEnabled(true);
            }
        } else {
            cardLayout.setVisibility(View.VISIBLE);
            cardLayout.setEnabled(true);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

    }

    public void hide() {
        if (!isSearchViewVisible()) return;
        if (mOnSearchListener != null) {
            mOnSearchListener.searchViewClosed();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animatorHide = ViewAnimationUtils.createCircularReveal(cardLayout,
                    cardLayout.getWidth() - 20,
                    cardLayout.getHeight()/2,
                    (float) Math.hypot(cardLayout.getWidth(), cardLayout.getHeight()),
                    0);
            animatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    cardLayout.setVisibility(View.GONE);
                    inputMethodManager.hideSoftInputFromWindow(cardLayout.getWindowToken(), 0);
                    clearSearch();
                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorHide.setDuration(300);
            animatorHide.start();
        } else {
            inputMethodManager.hideSoftInputFromWindow(cardLayout.getWindowToken(), 0);
            cardLayout.setVisibility(View.GONE);
            clearSearch();
            setVisibility(View.GONE);
        }
    }

    public EditText getSearchView() {
        return mSearchEditText;
    }

    public static WindowManager.LayoutParams getSearchViewLayoutParams(final Activity activity) {
        final Rect rect = new Rect();
        final Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        final int statusBarHeight = rect.top;

        final TypedArray actionBarSize = activity.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        actionBarSize.recycle();
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                rect.right /* This ensures we don't go under the navigation bar in landscape */,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = statusBarHeight;
        return params;
    }

    private void toggleClearSearchButton(final CharSequence query) {
        mClearSearch.setVisibility(!TextUtils.isEmpty(query) ? View.VISIBLE : View.INVISIBLE);
    }

    private void clearSearch() {
        mSearchEditText.setText("");
        mClearSearch.setVisibility(View.INVISIBLE);
    }

    private void onCancelSearch() {
        if (mOnSearchListener != null) {
            mOnSearchListener.onCancelSearch();
        }
        hide();
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onCancelSearch();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (v == backArrowImg) {
            onCancelSearch();
        } else if (v == mClearSearch) {
            clearSearch();
        }
    }
}