package com.blanke.mdwechat

import com.blanke.mdwechat.Classes.AvatarUtils
import com.blanke.mdwechat.Classes.ContactFragment
import com.blanke.mdwechat.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.Classes.LauncherUIBottomTabView
import com.blanke.mdwechat.Classes.MainTabUIPageAdapter
import com.blanke.mdwechat.Classes.WXCustomSchemeEntryActivity
import com.blanke.mdwechat.Classes.WxViewPager
import java.lang.reflect.Method

object Methods {
    private fun findMethodsByName(clazz: Class<*>?, name: String?, vararg parameterTypes: Class<*>): Method? {
        name ?: return null
        clazz ?: return null
        return clazz.getDeclaredMethod(name, *parameterTypes)?.apply { isAccessible = true }
    }

    private fun findMethodsByName(clazz: Class<*>?, names: List<String>?, vararg parameterTypes: Class<*>): List<Method> {
        clazz ?: return listOf()
        names ?: return listOf()
        return names.map {
            findMethodsByName(clazz, it, *parameterTypes)
        }.filter { it != null }.map { it!! }
    }

    val MainTabUIPageAdapter_getCount: Method by WechatGlobal.wxLazy("MainTabUIPageAdapter_getCount") {
        findMethodsByName(MainTabUIPageAdapter,
                WechatGlobal.wxVersionConfig.methods.MainTabUIPageAdapter_getCount,
                CC.Int)
    }

    val MainTabUIPageAdapter_onPageScrolled: Method by WechatGlobal.wxLazy("MainTabUIPageAdapter_onPageScrolled") {
        findMethodsByName(MainTabUIPageAdapter,
                WechatGlobal.wxVersionConfig.methods.MainTabUIPageAdapter_onPageScrolled,
                CC.Int, CC.Float, CC.Int)
    }

    val WxViewPager_selectedPage: Method by WechatGlobal.wxLazy("WxViewPager_selectedPage") {
        findMethodsByName(WxViewPager,
                WechatGlobal.wxVersionConfig.methods.WxViewPager_selectedPage,
                CC.Int, CC.Boolean, CC.Boolean, CC.Int)
    }

    val LauncherUIBottomTabView_getTabItemView: Method by WechatGlobal.wxLazy("LauncherUIBottomTabView_getTabItemView") {
        findMethodsByName(LauncherUIBottomTabView,
                WechatGlobal.wxVersionConfig.methods.WxViewPager_selectedPage,
                CC.Int)
    }

    val AvatarUtils_getDefaultAvatarBitmap: Method by WechatGlobal.wxLazy("AvatarUtils_getDefaultAvatarBitmap") {
        findMethodsByName(AvatarUtils,
                WechatGlobal.wxVersionConfig.methods.AvatarUtils_getDefaultAvatarBitmap)
    }

    val AvatarUtils_getAvatarBitmaps: List<Method> by WechatGlobal.wxLazy("AvatarUtils_getAvatarBitmaps") {
        findMethodsByName(AvatarUtils,
                WechatGlobal.wxVersionConfig.methods.AvatarUtils_getAvatarBitmaps,
                CC.String)
    }

    val ConversationWithAppBrandListView_isAppBrandHeaderEnable: Method by WechatGlobal.wxLazy("ConversationWithAppBrandListView_isAppBrandHeaderEnable") {
        findMethodsByName(ConversationWithAppBrandListView,
                WechatGlobal.wxVersionConfig.methods.ConversationWithAppBrandListView_isAppBrandHeaderEnable,
                CC.Boolean)
    }

    // 所有生命周期方法
    val HomeFragment_lifecycles: List<Method> by WechatGlobal.wxLazy("ContactFragment_lifecycles") {
        findMethodsByName(ContactFragment,
                WechatGlobal.wxVersionConfig.methods.HomeFragment_lifecycles)
    }

    val WXCustomSchemeEntryActivity_entry: Method by WechatGlobal.wxLazy("WXCustomSchemeEntryActivity_entry") {
        findMethodsByName(WXCustomSchemeEntryActivity,
                WechatGlobal.wxVersionConfig.methods.WXCustomSchemeEntryActivity_entry,
                CC.Intent)
    }
}