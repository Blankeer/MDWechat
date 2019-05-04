package com.blanke.mdwechat.hookers

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.blanke.mdwechat.*
import com.blanke.mdwechat.Classes.PhoneWindow
import com.blanke.mdwechat.WeChatHelper.colorPrimary
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.ColorUtils
import com.blanke.mdwechat.util.mainThread
import com.blankj.utilcode.util.BarUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod


object StatusBarHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(phoneWindowHook)
    }

    private val phoneWindowHook = Hooker {
        val color = getStatusBarColor()
        if (WechatGlobal.wxVersion!! < Version("7.0.3")) {
            findAndHookMethod(PhoneWindow, "setStatusBarColor", CC.Int, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                    val oldColor = param?.args!![0] as Int
                    if (oldColor == Color.parseColor("#F2F2F2")
                            || oldColor == Color.parseColor("#FFFAFAFA")
                            || oldColor == 0) {
                        return
                    }
                    if (color != oldColor) {
                        WeChatHelper.reloadPrefs()
                        val window = param.thisObject as Window
                        window.statusBarColor = color
                        window.navigationBarColor = color
                        param.result = null
                    }
                }
            })
        } else {
            XposedHelpers.findAndHookMethod(CC.Activity, "onCreate", CC.Bundle, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
//                    LogUtil.log("activity onCreate " + activity)
                    val statusView = View(activity)
                    statusView.background = ColorDrawable(getStatusBarColor())
                    statusView.elevation = 1F
                    val statusParam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    statusParam.topMargin = 0
                    statusParam.height = BarUtils.getStatusBarHeight()

                    statusView.layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())
                    val rootView = activity.window.decorView as ViewGroup
                    mainThread(100) {
                        rootView.addView(statusView)
                        val window = activity.window
                        window.statusBarColor = color
                        window.navigationBarColor = color
                    }
                    if (activity::class.java == Classes.LauncherUI) {
                        mainThread(1000) {
                            val window = activity.window
                            window.statusBarColor = color
                            window.navigationBarColor = color
                        }
                    }
                }
            })
        }
    }

    fun getStatusBarColor(): Int {
        return if (HookConfig.is_hook_statusbar_transparent) colorPrimary else ColorUtils.getDarkerColor(colorPrimary, 0.85f)
    }
}