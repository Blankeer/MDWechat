package com.blanke.mdwechat.hookers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Classes.ActionBarContainer
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.WeChatHelper.colorPrimaryDrawable
import com.blanke.mdwechat.WechatGlobal
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object ActionBarHooker : HookerProvider {

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(actionBarHooker)
    }

    private val actionBarHooker = Hooker {
        findAndHookMethod(ActionBarContainer, "setPrimaryBackground", Drawable::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val drawable = param.args[0]
                var needHook = true
                val actionBar = param.thisObject as ViewGroup
                if (drawable is ColorDrawable) {
//                    LogUtil.log("actionbar color=" + Integer.toHexString(drawable.color))
                    if (drawable.color == Color.parseColor("#F2F2F2")
                            || drawable.color == Color.parseColor("#FFFAFAFA")
                            || drawable.color == Color.TRANSPARENT) {
                        needHook = false
                    }
                    if (WechatGlobal.wxVersion!! >= Version("7.0.0")) {
                        if (actionBar.context::class.java.name == Classes.LauncherUI.name) {
                            needHook = true
                        }
                    }
                    if (drawable.color == colorPrimaryDrawable.color) {
                        needHook = false
                    }
                }
                if (needHook) {
                    param.args[0] = colorPrimaryDrawable
                }
                val init_elevation = XposedHelpers.getAdditionalInstanceField(actionBar, "init_elevation")
                if (init_elevation == null) {
                    actionBar.elevation = 5F
                    XposedHelpers.setAdditionalInstanceField(actionBar, "init_elevation", true)
                }
            }
        })
    }
}