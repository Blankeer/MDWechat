package com.blanke.mdwechat.hookers

import android.view.View
import com.blanke.mdwechat.util.LogUtil.logView
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object DebugHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(viewTouchHook)
    }

    private val viewTouchHook = Hooker {
        XposedHelpers.findAndHookMethod(C.View, "onTouchEvent", C.MotionEvent, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                val view = param.thisObject as View
                logView(view)
            }
        })
    }
}