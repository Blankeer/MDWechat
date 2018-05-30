package com.blanke.mdwechat.hookers

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Classes.SettingsFragment
import com.blanke.mdwechat.Fields.PreferenceFragment_mListView
import com.blanke.mdwechat.WeChatHelper.whiteDrawable
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.util.LogUtil
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object SettingsHooker : HookerProvider {
    const val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(resumeHook)
    }

    private val resumeHook = Hooker {
        LogUtil.log("SettingsFragment=$SettingsFragment,PreferenceFragment_mListView=$PreferenceFragment_mListView")
        XposedHelpers.findAndHookMethod(Classes.Fragment, "performResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val fragment = param?.thisObject ?: return
                if (fragment.javaClass.name != Classes.SettingsFragment.name) {
                    return
                }
                LogUtil.log("SettingsFragment fragment=$fragment")
                val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                if (isInit != null) {
                    LogUtil.log("SettingsFragment 已经hook过")
                    return
                }
                XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                init(fragment)
            }

            private fun init(fragment: Any) {
                val listView = PreferenceFragment_mListView.get(fragment)
                if (listView != null && listView is View) {
                    val background = AppCustomConfig.getTabBg(3)
                    listView.background = if (background != null) BitmapDrawable(background) else whiteDrawable
                }
            }
        })
    }
}