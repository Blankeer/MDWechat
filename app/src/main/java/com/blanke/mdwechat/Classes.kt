package com.blanke.mdwechat

import com.blanke.mdwechat.util.LogUtil.log
import de.robv.android.xposed.XposedHelpers

object Classes {
    private fun findClass(className: String?): Class<*>? {
        className ?: return null
        try {
            return XposedHelpers.findClass(className, com.blanke.mdwechat.WechatGlobal.wxLoader)
        } catch (e: Exception) {
            log("$className = null")
            return null
        }
    }

    val HomeUI: Class<*> by WechatGlobal.wxLazy("HomeUI") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.HomeUI)
    }

    val LauncherUI: Class<*> by WechatGlobal.wxLazy("LauncherUI") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.LauncherUI)
    }

    val WxViewPager: Class<*> by WechatGlobal.wxLazy("WxViewPager") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.WxViewPager)
    }

    val CustomViewPager: Class<*> by WechatGlobal.wxLazy("CustomViewPager") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.CustomViewPager)
    }

    val MainTabUI: Class<*> by WechatGlobal.wxLazy("MainTabUI") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.MainTabUI)
    }

    val MainTabUIPageAdapter: Class<*> by WechatGlobal.wxLazy("MainTabUIPageAdapter") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.MainTabUIPageAdapter)
    }

    val LauncherUIBottomTabView: Class<*> by WechatGlobal.wxLazy("LauncherUIBottomTabView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.LauncherUIBottomTabView)
    }

    val TabIconView: Class<*> by WechatGlobal.wxLazy("TabIconView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.TabIconView)
    }

    val ThreadExecutor: Class<*> by WechatGlobal.wxLazy("ThreadExecutor") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ThreadExecutor)
    }

    val LauncherUIBottomTabViewItem: Class<*> by WechatGlobal.wxLazy("LauncherUIBottomTabViewItem") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.LauncherUIBottomTabViewItem)
    }

    val ActionBarContainer: Class<*> by WechatGlobal.wxLazy("ActionBarContainer") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ActionBarContainer)
    }

    val ScrollingTabContainerView: Class<*> by WechatGlobal.wxLazy("ScrollingTabContainerView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ScrollingTabContainerView)
    }

    val PhoneWindow: Class<*> by WechatGlobal.wxLazy("PhoneWindow") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.PhoneWindow)
    }

    val AvatarUtils: Class<*> by WechatGlobal.wxLazy("AvatarUtils") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.AvatarUtils)
    }

    val NoDrawingCacheLinearLayout: Class<*> by WechatGlobal.wxLazy("NoDrawingCacheLinearLayout") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.NoDrawingCacheLinearLayout)
    }

    val ConversationWithAppBrandListView: Class<*> by WechatGlobal.wxLazy("ConversationWithAppBrandListView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ConversationWithAppBrandListView)
    }

    val ConversationListView: Class<*> by WechatGlobal.wxLazy("ConversationListView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ConversationListView)
    }

    val ConversationFragment: Class<*> by WechatGlobal.wxLazy("ConversationFragment") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ConversationFragment)
    }

    val ContactFragment: Class<*> by WechatGlobal.wxLazy("ContactFragment") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ContactFragment)
    }

    val FragmentActivity: Class<*> by WechatGlobal.wxLazy("FragmentActivity") {
        findClass("android.support.v4.app.FragmentActivity")
    }

    val Fragment: Class<*> by WechatGlobal.wxLazy("Fragment") {
        findClass("android.support.v4.app.Fragment")
    }

    val DiscoverFragment: Class<*> by WechatGlobal.wxLazy("DiscoverFragment") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.DiscoverFragment)
    }

    val SettingsFragment: Class<*> by WechatGlobal.wxLazy("SettingsFragment") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.SettingsFragment)
    }

    val PreferenceFragment: Class<*> by WechatGlobal.wxLazy("PreferenceFragment") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.PreferenceFragment)
    }

    val NoMeasuredTextView: Class<*> by WechatGlobal.wxLazy("NoMeasuredTextView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.NoMeasuredTextView)
    }

    val ActionMenuView: Class<*> by WechatGlobal.wxLazy("ActionMenuView") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.ActionMenuView)
    }

    val WXCustomSchemeEntryActivity: Class<*> by WechatGlobal.wxLazy("WXCustomSchemeEntryActivity") {
        findClass(com.blanke.mdwechat.WechatGlobal.wxVersionConfig.classes.WXCustomSchemeEntryActivity)
    }

    val RemittanceAdapterUI: Class<*> by WechatGlobal.wxLazy("RemittanceAdapterUI") {
        findClass("com.tencent.mm.plugin.remittance.ui.RemittanceAdapterUI")
    }

    val Toolbar: Class<*> by WechatGlobal.wxLazy("Toolbar") {
        findClass("android.support.v7.widget.Toolbar")
    }
}