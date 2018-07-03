package com.blanke.mdwechat.hookers.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.LinearLayout
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.Methods
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ConvertUtils
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.ViewUtils
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import java.lang.ref.WeakReference
import java.util.*

object TabLayoutHook {
    fun addTabLayout(viewPagerLinearLayout: ViewGroup) {
        val context = viewPagerLinearLayout.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
        val resContext = viewPagerLinearLayout.context

        val tabLayout = CommonTabLayout(context)
        val primaryColor = HookConfig.get_color_primary
        tabLayout.setBackgroundColor(primaryColor)
        tabLayout.textSelectColor = Color.WHITE
        val dp2 = ConvertUtils.dp2px(resContext, 1f)
        tabLayout.indicatorHeight = dp2.toFloat()
        tabLayout.indicatorColor = Color.WHITE
        tabLayout.indicatorCornerRadius = dp2.toFloat()
        tabLayout.indicatorAnimDuration = 200
        tabLayout.elevation = if (HookConfig.is_hook_tab_elevation) 5F else 0F
        tabLayout.unreadBackground = Color.WHITE
        tabLayout.unreadTextColor = primaryColor
        tabLayout.selectIconColor = Color.WHITE
        tabLayout.unSelectIconColor = Color.WHITE

        val mTabEntities = intArrayOf(0, 1, 2, 3)
//                .filterNot { HookConfig.is_hook_hide_wx_tab_2 && it == 2 }
//                .filterNot { HookConfig.is_hook_hide_wx_tab_3 && it == 3 }
                .mapTo(ArrayList<CustomTabEntity>()) {
                    object : CustomTabEntity.TabCustomData() {
                        override fun getTabIcon(): Drawable {
                            return BitmapDrawable(resContext.resources, AppCustomConfig.getTabIcon(it))
                        }
                    }
                }
        tabLayout.setTabData(mTabEntities)

        tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                LogUtil.log("tab click position=$position")
                tabLayout.currentTab = position
                Objects.Main.LauncherUI_mViewPager.get()?.apply {
                    Methods.WxViewPager_selectedPage.invoke(this, position, false, false, 0)
                }
            }

            override fun onTabReselect(position: Int) {
            }
        })

        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.height = ConvertUtils.dp2px(resContext, 48f)
        viewPagerLinearLayout.addView(tabLayout, 0, params)
        LogUtil.log("add tableyout success")
        Objects.Main.LauncherUI_mTabLayout = WeakReference(tabLayout)
        // change main ActionBar elevation = 0
        val actionBarLayout = ViewUtils.getParentView(viewPagerLinearLayout, 3) as ViewGroup
        val actionBar = actionBarLayout.getChildAt(1)
        actionBar.elevation = 0F
    }
}