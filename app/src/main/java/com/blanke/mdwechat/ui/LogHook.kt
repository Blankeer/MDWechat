package com.blanke.mdwechat.ui

import android.content.Intent
import android.os.Bundle
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.util.LogUtil.bundleToString
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class LogHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookTouchEvents()
        hookCreateActivity()
        hookXLogSetup(lpparam)
    }

    private fun hookTouchEvents() {
        xMethod("android.view.View", "onTouchEvent", C.MotionEvent, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                val obj = param.thisObject
                log("View.onTouchEvent => view = $obj")
            }
        })
    }

    private fun hookCreateActivity() {
        xMethod("android.app.Activity", "startActivity", C.Intent, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                val obj = param.thisObject
                val intent = param.args[0] as Intent?
                log("Activity.startActivity => ${obj.javaClass}, intent => ${bundleToString(intent?.extras)}")
            }
        })

        xMethod("android.app.Activity", "onCreate", C.Bundle, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                val obj = param.thisObject
                val bundle = param.args[0] as Bundle?
                val intent = XposedHelpers.callMethod(obj, "getIntent") as Intent?
                log("Activity.onCreate => ${obj.javaClass}, intent => ${bundleToString(intent?.extras)}, bundle => ${bundleToString(bundle)}")
            }
        })
    }

    private fun hookXLogSetup(lpparam: XC_LoadPackage.LoadPackageParam) {
        val XLogSetup = XposedHelpers.findClass(wxConfig.classes.XLogSetup, lpparam.classLoader)
        XposedBridge.hookAllMethods(XLogSetup, wxConfig.methods.XLogSetup_keep_setupXLog, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.args[5] = true
            }
        })
    }
}
