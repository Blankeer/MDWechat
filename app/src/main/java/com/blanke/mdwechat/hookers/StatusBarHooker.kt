package com.blanke.mdwechat.hookers

import android.graphics.Color
import android.view.Window
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.Classes.PhoneWindow
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.WeChatHelper.colorPrimary
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.ColorUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object StatusBarHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(phoneWindowHook)
    }

    private val phoneWindowHook = Hooker {
        findAndHookMethod(PhoneWindow, "setStatusBarColor", CC.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val oldColor = param?.args!![0] as Int
                if (oldColor == Color.parseColor("#F2F2F2")
                        || oldColor == Color.parseColor("#FFFAFAFA")
                        || oldColor == 0) {
                    return
                }
                val color = getStatueBarColor()
                if (color != oldColor) {
                    WeChatHelper.reloadPrefs()
                    val window = param.thisObject as Window
                    window.statusBarColor = color
                    window.navigationBarColor = color
                    param.result = null
                }
            }
        })
    }

    fun getStatueBarColor(): Int {
        return if (HookConfig.is_hook_statusbar_transparent) colorPrimary else ColorUtils.getDarkerColor(colorPrimary, 0.85f)
    }
}