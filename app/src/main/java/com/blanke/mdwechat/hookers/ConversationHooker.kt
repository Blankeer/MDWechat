package com.blanke.mdwechat.hookers

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.Classes.ConversationFragment
import com.blanke.mdwechat.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.Fields.ConversationFragment_mListView
import com.blanke.mdwechat.Methods.ConversationWithAppBrandListView_isAppBrandHeaderEnable
import com.blanke.mdwechat.WeChatHelper.whiteDrawable
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.LogUtil
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object ConversationHooker : HookerProvider {
    const val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(resumeHook, disableAppBrand)
    }

    private val resumeHook = Hooker {
        XposedHelpers.findAndHookMethod(ConversationFragment, "onResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val fragment = param?.thisObject
                if (fragment != null) {
                    val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                    if (isInit != null) {
                        LogUtil.log("ConversationFragment 已经hook过")
                        return
                    }
                    XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                    init(fragment)
                }
            }

            private fun init(fragment: Any) {
                val listView = ConversationFragment_mListView.get(fragment)
                if (listView != null && listView is View) {
                    val background = AppCustomConfig.getTabBg(0)
                    listView.background = if (background != null) BitmapDrawable(background) else whiteDrawable
                }
            }
        })
    }
    private val disableAppBrand = Hooker {
        XposedHelpers.findAndHookMethod(ConversationWithAppBrandListView,
                ConversationWithAppBrandListView_isAppBrandHeaderEnable.name, C.Boolean, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                if (HookConfig.is_hook_remove_appbrand) {
                    param?.result = false
                }
            }
        })
    }
}