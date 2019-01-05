package com.blanke.mdwechat.hookers

import android.graphics.drawable.ColorDrawable
import android.view.View
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.NightModeUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object SettingsHooker : HookerProvider {
    const val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(resumeHook)
    }

    private val resumeHook = Hooker {
        XposedHelpers.findAndHookMethod(Classes.Fragment, "performResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val fragment = param?.thisObject ?: return
                if (fragment.javaClass.name != Classes.SettingsFragment.name) {
                    return
                }
                val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                if (isInit != null) {
                    LogUtil.log("SettingsFragment 已经hook过")
                    return
                }
                XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                init(fragment)
            }

            private fun init(fragment: Any) {
//                val lv = PreferenceFragment_mListView.get(fragment)
            }
        })
        XposedBridge.hookAllMethods(CC.View, "setBackground", object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val view = param.thisObject as View
                if (view::class.java.name.contains("PullDownListView")) {
                    val drawable = param.args[0]
                    if (drawable !is ColorDrawable) {
                        val background = AppCustomConfig.getTabBg(3)
                        param.args[0] = NightModeUtils.getBackgroundDrawable(background)
                    }
                }
            }
        })
    }
}