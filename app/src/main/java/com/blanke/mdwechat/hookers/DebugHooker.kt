package com.blanke.mdwechat.hookers

import android.view.MotionEvent
import android.view.View
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil.logView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object DebugHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(viewTouchHook)
    }

    private val viewTouchHook = Hooker {
        XposedHelpers.findAndHookMethod(CC.View, "onTouchEvent", CC.MotionEvent, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                val view = param.thisObject as View
                val event = param.args[0] as MotionEvent
                if (event.action == MotionEvent.ACTION_UP) {
                    logView(view)
                }
            }
        })
    }
}