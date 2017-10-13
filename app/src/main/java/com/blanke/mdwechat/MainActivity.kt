package com.blanke.mdwechat

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.blanke.mdwechat.util.ConvertUtils
import com.blanke.mdwechat.widget.MaterialSearchView
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionButton.SIZE_MINI
import com.github.clans.fab.FloatingActionMenu
import java.util.*

class MainActivity : Activity() {
    private var contentView: FrameLayout? = null
    private var materialSearchView: MaterialSearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contentView = findViewById(R.id.main_content) as FrameLayout
        materialSearchView = findViewById(R.id.searchView) as MaterialSearchView
        addFab(contentView!!)
        addTabLayout(contentView!!)
    }

    private fun addFab(contentView: FrameLayout) {
        val primaryColor = resources.getColor(R.color.colorPrimary)
        val context = this
        val actionMenu = FloatingActionMenu(context)
        actionMenu.menuButtonColorNormal = primaryColor
        actionMenu.menuButtonColorPressed = primaryColor
        actionMenu.setMenuIcon(ContextCompat.getDrawable(context, R.drawable.ic_add))
        actionMenu.initMenuButton()

        actionMenu.setFloatButtonClickListener { fab, index -> Log.d("click ", "fab=" + fab + ",index=" + index + ",label" + fab.labelText) }

        addFloatButton(actionMenu, context, "扫一扫", R.drawable.ic_scan, primaryColor)
        addFloatButton(actionMenu, context, "收付款", R.drawable.ic_money, primaryColor)
        addFloatButton(actionMenu, context, "群聊", R.drawable.ic_chat, primaryColor)
        addFloatButton(actionMenu, context, "添加好友", R.drawable.ic_friend_add, primaryColor)


        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = ConvertUtils.dp2px(context, 12f)
        params.rightMargin = margin
        params.bottomMargin = margin
        params.gravity = Gravity.END or Gravity.BOTTOM
        contentView.addView(actionMenu, params)
    }

    private fun addFloatButton(actionMenu: FloatingActionMenu, context: Context, label: String, drawable: Int, primaryColor: Int) {
        val fab = FloatingActionButton(context)
        fab.setImageDrawable(ContextCompat.getDrawable(context, drawable))
        fab.colorNormal = primaryColor
        fab.colorPressed = primaryColor
        fab.buttonSize = SIZE_MINI
        fab.labelText = label
        actionMenu.addMenuButton(fab)
        fab.setLabelColors(primaryColor, primaryColor, primaryColor)
    }

    private fun addTabLayout(contentLayout: ViewGroup) {
        val context = this
        val tabLayout = CommonTabLayout(context)
        val color = resources.getColor(R.color.colorPrimary)
        tabLayout.setBackgroundColor(color)
        tabLayout.textSelectColor = Color.WHITE
        tabLayout.textUnselectColor = 0x1acccccc
        tabLayout.indicatorHeight = ConvertUtils.dp2px(this, 1f).toFloat()
        tabLayout.indicatorColor = Color.WHITE
        tabLayout.indicatorCornerRadius = ConvertUtils.dp2px(this, 2f).toFloat()
        tabLayout.indicatorAnimDuration = 200
        tabLayout.elevation = 5f
        tabLayout.textsize = context.resources.getDimension(R.dimen.tabTextSize)
        tabLayout.unreadBackground = Color.WHITE
        tabLayout.unreadTextColor = color
        tabLayout.selectIconColor = Color.WHITE
        tabLayout.unSelectIconColor = 0x1acccccc

        val titles = arrayOf("消息", "通讯录", "朋友圈", "设置")
        val tabIcons = intArrayOf(R.drawable.ic_chat_tab, R.drawable.ic_contact_tab, R.drawable.ic_explore_tab, R.drawable.ic_person_tab)
        val mTabEntities = ArrayList<CustomTabEntity>()
//        for (i in tabIcons.indices) {
//            val finalI = i
//            mTabEntities.add(object : CustomTabEntity {
//                override fun getTabTitle(): String? {
//                    return null
//                }
//
//                override fun getTabSelectedIcon(): Int {
//                    return tabIcons[finalI]
//                }
//
//                override fun getTabUnselectedIcon(): Int {
//                    return R.drawable.ic_scan
//                }
//            })
//        }
        tabLayout.setTabData(mTabEntities)

        tabLayout.showMsg(0, 5)
        tabLayout.showMsg(1, -1)
        tabLayout.showMsg(2, 0)
        tabLayout.showMsg(3, 88)

        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.height = ConvertUtils.dp2px(this, 48f)
        params.topMargin = 400
        contentLayout.addView(tabLayout, params)
    }

    fun clickButton(view: View) {
        materialSearchView!!.show()
    }
}
