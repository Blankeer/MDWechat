package com.blanke.mdwechat.ui

import de.robv.android.xposed.XposedBridge

/**
 * Created by blanke on 2017/10/3.
 */

object LogUtil {
    fun log(msg: String) {
        XposedBridge.log("MDWechat:\t" + msg + "\tts=" + System.currentTimeMillis())
    }

    fun log(e: Throwable) {
        XposedBridge.log(e)
    }
}
