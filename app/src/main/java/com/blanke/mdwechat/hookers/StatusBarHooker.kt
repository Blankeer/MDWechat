package com.blanke.mdwechat.hookers

import android.view.Window
import com.blanke.mdwechat.Classes.PhoneWindow
import com.blanke.mdwechat.WeChatHelper
import com.blanke.mdwechat.WeChatHelper.colorPrimary
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ColorUtils
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import com.githang.statusbar.StatusBarCompat
import com.githang.statusbar.StatusBarCompat.toGrey
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object StatusBarHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(phoneWindowHook)
    }

    private val phoneWindowHook = Hooker {
        findAndHookMethod(PhoneWindow, "setStatusBarColor", C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val color = getStatueBarColor()
                val oldColor = param?.args!![0] as Int
                if (color != oldColor) {
                    WeChatHelper.reloadPrefs()
                    val window = param.thisObject as Window
                    StatusBarCompat.setStatusBarColor(window, color, toGrey(color) > 225)
                    window.navigationBarColor = color
                    param.result = null
                }
            }
        })
    }

    private fun getStatueBarColor(): Int {
        return if (HookConfig.is_hook_statusbar_transparent) colorPrimary else ColorUtils.getDarkerColor(colorPrimary, 0.85f)
    }
}