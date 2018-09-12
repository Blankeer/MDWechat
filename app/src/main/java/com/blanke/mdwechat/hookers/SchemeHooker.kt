package com.blanke.mdwechat.hookers

import android.app.Activity
import android.content.Intent
import com.blanke.mdwechat.Classes.RemittanceAdapterUI
import com.blanke.mdwechat.Methods.WXCustomSchemeEntryActivity_entry
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object SchemeHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(donateHooker)
    }

    private val donateHooker = Hooker {
        XposedBridge.hookMethod(WXCustomSchemeEntryActivity_entry, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val intent = param.args[0] as Intent?
                val uri = intent?.data ?: return
                val activity = param.thisObject as Activity
                if (uri.host == "mdwechat") {
                    val segments = uri.pathSegments
                    if (segments.firstOrNull() == "donate") {
                        activity.startActivity(Intent(activity, RemittanceAdapterUI).apply {
                            putExtra("scene", 1)
                            putExtra("pay_channel", 12)
                            putExtra("receiver_name", "wxp://${segments[1]}")
                        })
                    }
                    param.result = false
                }
            }
        })
    }
}