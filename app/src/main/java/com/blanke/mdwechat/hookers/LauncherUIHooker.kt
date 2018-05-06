package com.blanke.mdwechat.hookers

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blanke.mdwechat.Classes.LauncherUIBottomTabView
import com.blanke.mdwechat.Classes.WxViewPager
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.Fields.HomeUI_mMainTabUI
import com.blanke.mdwechat.Fields.LauncherUI_mHomeUI
import com.blanke.mdwechat.Fields.MainTabUI_mCustomViewPager
import com.blanke.mdwechat.Methods.WxViewPager_selectedPage
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.Objects.Main.LauncherUI_mTabLayout
import com.blanke.mdwechat.Objects.Main.LauncherUI_mViewPager
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.util.ConvertUtils
import com.blanke.mdwechat.util.LogUtil.log
import com.blanke.mdwechat.util.ViewUtils
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import com.gh0u1l5.wechatmagician.spellbook.mirror.com.tencent.mm.ui.Classes
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.lang.ref.WeakReference
import java.util.*

object LauncherUIHooker : HookerProvider {
    val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(launcherResumeHooker, mainTabUIPageAdapterHook)
    }

    val launcherResumeHooker = Hooker {
        XposedHelpers.findAndHookMethod(C.Activity, "onPostResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val activity = param.thisObject as? Activity ?: return
                if (activity::class.java != Classes.LauncherUI) {
                    return
                }
                val isInit = XposedHelpers.getAdditionalInstanceField(activity, keyInit)
                if (isInit != null) {
                    log("LauncherUI 已经hook过")
                    return
                }
                XposedHelpers.setAdditionalInstanceField(activity, keyInit, true)
                initHookLauncherUI(activity)
            }

            private fun initHookLauncherUI(activity: Activity) {
                Objects.Main.LauncherUI = WeakReference(activity)
                val homeUI = LauncherUI_mHomeUI.get(activity)
                val mainTabUI = HomeUI_mMainTabUI.get(homeUI)
                val viewPager = MainTabUI_mCustomViewPager.get(mainTabUI) as View
                LauncherUI_mViewPager = WeakReference(viewPager)

                // remove tabView
                val linearViewGroup = viewPager.parent as ViewGroup
                val tabView = linearViewGroup.getChildAt(1)
                log("tabView=$tabView")
                linearViewGroup.removeView(tabView)

                val contentViewGroup = linearViewGroup.parent as ViewGroup
                log("contentViewGroup=$contentViewGroup")

                // hide actionBar
//                val actionBar = Fields.HomeUI_mActionBar.get(homeUI)
//                log("actionBar=$actionBar")
//                XposedHelpers.callMethod(actionBar, "hide")

                try {
                    addTabLayout(linearViewGroup)
                } catch (e: Throwable) {
                    log(e)
                }
            }
        })
    }

    val mainTabUIPageAdapterHook = Hooker {
        XposedHelpers.findAndHookMethod(WxViewPager, WxViewPager_selectedPage.name, C.Int, C.Boolean, C.Boolean, C.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                val vp = param?.thisObject
                if (LauncherUI_mViewPager.get() == vp) {
                    val position = param?.args!![0] as Int
//                    log("WxViewPager_selectedPage position = $position , arg[1] =${param?.args!![1]}")
                    LauncherUI_mTabLayout.get()?.currentTab = position
                }
            }
        })
//        XposedHelpers.findAndHookMethod(MainTabUIPageAdapter, MainTabUIPageAdapter_onPageScrolled.name, C.Int, Float::class.java, C.Int, object : XC_MethodHook() {
//            override fun afterHookedMethod(param: MethodHookParam?) {
//                val positionOffset = param?.args!![1] as Float
//                val position = param.args[0]
//                log("MainTabUIPageAdapter_onPageScrolled ,positionOffset=$positionOffset,startScrollPosition=$position")
//                LauncherUI_mTabLayout.get()?.apply {
//                    startScrollPosition = position as Int
//                    indicatorOffset = positionOffset
//                }
//            }
//        })

        XposedHelpers.findAndHookMethod(TextView::class.java, "setText", CharSequence::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                val tv = param?.thisObject as View
                val text = param.args!![0]
                if (text == null || text !is String) {
                    return
                }
                val tabView = ViewUtils.getParentView(tv, 5)
                if (tabView != null && tabView.javaClass.name == LauncherUIBottomTabView.name) {
                    ViewUtils.getParentView(tv, 3)?.apply {
                        val tabViewItemParent = this.parent as ViewGroup
                        val position = tabViewItemParent.indexOfChild(this)
                        log("unread position= $position,count = $text")
                        val number = if (text.length == 0) 0 else text.toIntOrNull()
                        LauncherUI_mTabLayout.get()?.apply {
                            showMsg(position, number ?: 99)
                        }
                    }
                }
            }
        })
        XposedHelpers.findAndHookMethod(ImageView::class.java, "setVisibility", C.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                val visible = param?.args!![0] as Int
                val view = param.thisObject as ImageView
                val tabView = ViewUtils.getParentView(view, 5)
                if (tabView != null && tabView.javaClass.name == LauncherUIBottomTabView.name) {
                    ViewUtils.getParentView(view, 3)?.apply {
                        val tabViewItemParent = this.parent as ViewGroup
                        val position = tabViewItemParent.indexOfChild(this)
                        log("unread position= $position,visible = ${visible == View.VISIBLE}")
                        LauncherUI_mTabLayout.get()?.apply {
                            if (visible == View.VISIBLE) {
                                showMsg(position, -1)
                            } else if (!hasMsg(position)) {
                                showMsg(position, 0)
                            }
                        }
                    }
                }
            }
        })
    }

    fun addTabLayout(viewPagerLinearLayout: ViewGroup) {
        val context = viewPagerLinearLayout.context.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
        val resContext = viewPagerLinearLayout.context

        val tabLayout = CommonTabLayout(context)
        val primaryColor = Color.RED
        tabLayout.setBackgroundColor(primaryColor)
        tabLayout.textSelectColor = Color.WHITE
        tabLayout.textUnselectColor = 0x1acccccc
        val dp2 = ConvertUtils.dp2px(resContext, 1f)
        tabLayout.indicatorHeight = dp2.toFloat()
        tabLayout.indicatorColor = Color.WHITE
        tabLayout.indicatorCornerRadius = dp2.toFloat()
        tabLayout.indicatorAnimDuration = 200
        tabLayout.elevation = 5F
//        tabLayout.textsize = context.getResources().getDimension(R.dimen.tabTextSize)
//        tabLayout.textsize = dp2.toFloat()
        tabLayout.unreadBackground = Color.WHITE
        tabLayout.unreadTextColor = primaryColor
        tabLayout.selectIconColor = Color.WHITE
        tabLayout.unSelectIconColor = Color.WHITE
        tabLayout.isIndicatorAnimEnable = true

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
                log("tab click position=$position")
                tabLayout.currentTab = position
                LauncherUI_mViewPager.get()?.apply {
                    WxViewPager_selectedPage.invoke(this, position, false, false, 0)
                }
            }

            override fun onTabReselect(position: Int) {
            }
        })

        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.height = ConvertUtils.dp2px(resContext, 48f)
        viewPagerLinearLayout.addView(tabLayout, 0, params)
        log("add tableyout success")
        LauncherUI_mTabLayout = WeakReference(tabLayout)
    }
}