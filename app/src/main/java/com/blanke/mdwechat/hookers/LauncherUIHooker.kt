package com.blanke.mdwechat.hookers

import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.blanke.mdwechat.*
import com.blanke.mdwechat.Classes.LauncherUIBottomTabView
import com.blanke.mdwechat.Classes.MainTabUIPageAdapter
import com.blanke.mdwechat.Classes.WxViewPager
import com.blanke.mdwechat.Fields.HomeUI_mMainTabUI
import com.blanke.mdwechat.Fields.LauncherUI_mHomeUI
import com.blanke.mdwechat.Fields.MainTabUI_mCustomViewPager
import com.blanke.mdwechat.Methods.MainTabUIPageAdapter_onPageScrolled
import com.blanke.mdwechat.Methods.WxViewPager_selectedPage
import com.blanke.mdwechat.Objects.Main.LauncherUI_mTabLayout
import com.blanke.mdwechat.Objects.Main.LauncherUI_mViewPager
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.hookers.main.FloatMenuHook
import com.blanke.mdwechat.hookers.main.HomeActionBarHook
import com.blanke.mdwechat.hookers.main.TabLayoutHook
import com.blanke.mdwechat.util.LogUtil.log
import com.blanke.mdwechat.util.ViewUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.lang.ref.WeakReference

object LauncherUIHooker : HookerProvider {
    const val keyInit = "key_init"
    private var disablePageScrolledHook = false

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(launcherLifeHooker, mainTabUIPageAdapterHook, actionMenuHooker)
    }

    private val launcherLifeHooker = Hooker {
        XposedHelpers.findAndHookMethod(CC.Activity, "onDestroy", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val activity = param.thisObject as? Activity ?: return
                if (activity::class.java != Classes.LauncherUI) {
                    return
                }
                log("LauncherUI onDestroy()")
                Objects.clear()
            }
        })
        XposedHelpers.findAndHookMethod(CC.Activity, "onPostResume",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val activity = param.thisObject as? Activity ?: return
                        log("activity resume = $activity")
                        if (activity::class.java != Classes.LauncherUI) {
                            return
                        }
                        WeChatHelper.reloadPrefs()
                        val isInit = XposedHelpers.getAdditionalInstanceField(activity, keyInit)
                        if (isInit != null) {
                            log("LauncherUI 已经hook过")
                            return
                        }
                        log("LauncherUI onResume(), start hook")
                        initHookLauncherUI(activity)
                    }

                    private fun initHookLauncherUI(activity: Activity) {
                        val density = activity.resources.displayMetrics.density
                        AppCustomConfig.bitmapScale = density / 3F

                        Objects.Main.LauncherUI = WeakReference(activity)
                        val homeUI = LauncherUI_mHomeUI.get(activity)
                        val mainTabUI = HomeUI_mMainTabUI.get(homeUI)
                        val viewPager = MainTabUI_mCustomViewPager.get(mainTabUI)
                        if (viewPager == null || viewPager !is View) {
                            log("MainTabUI_mCustomViewPager == null return;")
                            return
                        }
                        LauncherUI_mViewPager = WeakReference(viewPager)

                        // remove tabView
                        val linearViewGroup = viewPager.parent as ViewGroup
                        val tabView = linearViewGroup.getChildAt(1)
                        if (HookConfig.is_hook_hide_wx_tab) {
                            if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                                // 672报错
                                tabView.visibility = View.GONE
                                tabView.layoutParams.height = 0
                            } else {
                                linearViewGroup.removeView(tabView)
                            }
                            log("移除 tabView $tabView")
                        }

                        val contentViewGroup = linearViewGroup.parent as ViewGroup
                        Objects.Main.LauncherUI_mContentLayout = WeakReference(contentViewGroup)

                        val actionBar = Fields.HomeUI_mActionBar.get(homeUI)
                        Objects.Main.HomeUI_mActionBar = WeakReference<Any>(actionBar)
                        HomeActionBarHook.fix(linearViewGroup)
                        if (HookConfig.is_hook_tab) {
                            try {
                                log("添加 TabLayout")
                                TabLayoutHook.addTabLayout(linearViewGroup)
                            } catch (e: Throwable) {
                                log("添加 TabLayout 报错")
                                log(e)
                            }
                        }
                        if (HookConfig.is_hook_float_button) {
                            try {
                                log("添加 FloatMenu")
                                FloatMenuHook.addFloatMenu(contentViewGroup)
                            } catch (e: Throwable) {
                                log("添加 FloatMenu 报错")
                                log(e)
                            }
                        }
                        XposedHelpers.setAdditionalInstanceField(activity, keyInit, true)
                    }
                })
    }

    private val mainTabUIPageAdapterHook = Hooker {
        XposedHelpers.findAndHookMethod(WxViewPager, WxViewPager_selectedPage.name, CC.Int, CC.Boolean, CC.Boolean, CC.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                val vp = param?.thisObject
                if (LauncherUI_mViewPager.get() == vp) {
                    val position = param?.args!![0] as Int
//                    log("WxViewPager_selectedPage position = $position , arg[1] =${param?.args!![1]}")
                    LauncherUI_mTabLayout.get()?.currentTab = position
                }
            }
        })
        XposedHelpers.findAndHookMethod(MainTabUIPageAdapter, MainTabUIPageAdapter_onPageScrolled.name, CC.Int, Float::class.java, CC.Int, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val positionOffset = param?.args!![1] as Float
                val position = param.args[0]
//                log("MainTabUIPageAdapter_onPageScrolled ,positionOffset=$positionOffset,startScrollPosition=$position")
                if (disablePageScrolledHook || positionOffset.toString().contains("E")) {// ?
                    disablePageScrolledHook = true
                    return
                }
                LauncherUI_mTabLayout.get()?.apply {
                    startScrollPosition = position as Int
                    indicatorOffset = positionOffset
                }
            }
        })

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
//                        log("unread position= $position,count = $text")
                        val number = if (text.length == 0) 0 else text.toIntOrNull()
                        LauncherUI_mTabLayout.get()?.apply {
                            number?.apply {
                                showMsg(position, number)
                            }
                        }
                    }
                }
            }
        })
        XposedHelpers.findAndHookMethod(View::class.java, "setVisibility", CC.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (param.thisObject !is ImageView) {
                    return
                }
                val visible = param?.args!![0] as Int
                val view = param.thisObject as ImageView
                val tabView = ViewUtils.getParentView(view, 5)
                if (tabView != null && tabView.javaClass.name == LauncherUIBottomTabView.name) {
                    ViewUtils.getParentView(view, 3)?.apply {
                        val tabViewItemParent = this.parent as ViewGroup
                        val position = tabViewItemParent.indexOfChild(this)
//                        log("unread position= $position,visible = ${visible == View.VISIBLE}")
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

    private val actionMenuHooker = Hooker {
        //hide menu item in actionBar
        XposedHelpers.findAndHookMethod(Classes.LauncherUI, "onCreateOptionsMenu", CC.Menu, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                if (!HookConfig.is_hook_float_button) {
                    return
                }
                val menu = param.args[0] as Menu
                menu.removeItem(2)
            }
        })
        XposedHelpers.findAndHookMethod(com.blanke.mdwechat.Classes.ActionMenuView, "add",
                Int::class.java, Int::class.java, Int::class.java, CharSequence::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        if (param.args.size == 4) {
                            val str = param.args[3]
                            val menuItem = param.result as MenuItem
                            if (str == "微X模块") {
                                log("检测到 微X模块")
                                menuItem.isVisible = false
                                Objects.Main.LauncherUI_mWechatXMenuItem = WeakReference(menuItem)
                            }
                        }
                    }
                })
    }
}