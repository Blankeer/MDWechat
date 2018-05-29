package com.blanke.mdwechat

import android.graphics.Bitmap
import com.blanke.mdwechat.CC.voidd
import com.blanke.mdwechat.Classes.AvatarUtils
import com.blanke.mdwechat.Classes.ContactFragment
import com.blanke.mdwechat.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.Classes.LauncherUIBottomTabView
import com.blanke.mdwechat.Classes.LauncherUIBottomTabViewItem
import com.blanke.mdwechat.Classes.MainTabUIPageAdapter
import com.blanke.mdwechat.Classes.WxViewPager
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal
import com.gh0u1l5.wechatmagician.spellbook.util.ReflectionUtil.findMethodsByExactParameters
import java.lang.reflect.Method
import java.lang.reflect.Modifier

object Methods {
    val MainTabUIPageAdapter_getCount: Method by WechatGlobal.wxLazy("MainTabUIPageAdapter_getCount") {
        MainTabUIPageAdapter.getMethod("getCount", C.Int)
    }

    val MainTabUIPageAdapter_onPageScrolled: Method by WechatGlobal.wxLazy("MainTabUIPageAdapter_onPageScrolled") {
        findMethodsByExactParameters(MainTabUIPageAdapter, voidd, C.Int, Float::class.java, C.Int)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val WxViewPager_selectedPage: Method by WechatGlobal.wxLazy("WxViewPager_selectedPage") {
        findMethodsByExactParameters(WxViewPager, voidd, C.Int, C.Boolean, C.Boolean, C.Int)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val LauncherUIBottomTabView_getTabItemView: Method by WechatGlobal.wxLazy("LauncherUIBottomTabView_getTabItemView") {
        findMethodsByExactParameters(LauncherUIBottomTabView, LauncherUIBottomTabViewItem, C.Int)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val AvatarUtils_getDefaultAvatarBitmap: Method by WechatGlobal.wxLazy("AvatarUtils_getDefaultAvatarBitmap") {
        findMethodsByExactParameters(AvatarUtils, Bitmap::class.java)
                .firstOrNull()?.apply { isAccessible = true }
    }

    val AvatarUtils_getAvatarBitmaps: List<Method> by WechatGlobal.wxLazy("AvatarUtils_getAvatarBitmaps") {
        findMethodsByExactParameters(AvatarUtils, Bitmap::class.java, C.String)
    }

    val ConversationWithAppBrandListView_isAppBrandHeaderEnable: Method by WechatGlobal.wxLazy("ConversationWithAppBrandListView_isAppBrandHeaderEnable") {
        findMethodsByExactParameters(ConversationWithAppBrandListView, C.Boolean, C.Boolean)
                .firstOrNull()?.apply { isAccessible = true }
    }

    // 所有生命周期方法
    val HomeFragment_lifecycles: List<Method> by WechatGlobal.wxLazy("ContactFragment_lifecycles") {
        findMethodsByExactParameters(ContactFragment, voidd)
                .filter { it.modifiers and Modifier.FINAL != 0 }
                .filter { it.modifiers and Modifier.PROTECTED != 0 }
    }
}