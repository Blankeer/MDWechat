package com.blanke.mdwechat.hookers

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blanke.mdwechat.Classes.LauncherUIBottomTabView
import com.blanke.mdwechat.Classes.WxViewPager
import com.blanke.mdwechat.Fields.HomeUI_mMainTabUI
import com.blanke.mdwechat.Fields.LauncherUI_mHomeUI
import com.blanke.mdwechat.Fields.MainTabUI_mCustomViewPager
import com.blanke.mdwechat.Methods.WxViewPager_selectedPage
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.Objects.Main.LauncherUI_mTabLayout
import com.blanke.mdwechat.Objects.Main.LauncherUI_mViewPager
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.hookers.main.FloatMenuHook
import com.blanke.mdwechat.hookers.main.TabLayoutHook
import com.blanke.mdwechat.util.LogUtil.log
import com.blanke.mdwechat.util.ViewUtils
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import com.gh0u1l5.wechatmagician.spellbook.mirror.com.tencent.mm.ui.Classes
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.lang.ref.WeakReference

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
                WeChatHelper.reloadPrefs()
                val isInit = XposedHelpers.getAdditionalInstanceField(activity, keyInit)
                if (isInit != null) {
                    log("LauncherUI 已经hook过")
                    return
                }
                XposedHelpers.setAdditionalInstanceField(activity, keyInit, true)
                initHookLauncherUI(activity)
            }

            private fun initHookLauncherUI(activity: Activity) {
                val density = activity.resources.displayMetrics.density
                AppCustomConfig.bitmapScale = density / 3F

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
                    TabLayoutHook.addTabLayout(linearViewGroup)
                } catch (e: Throwable) {
                    log(e)
                }
                try {
                    FloatMenuHook.addFloatMenu(contentViewGroup)
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
}