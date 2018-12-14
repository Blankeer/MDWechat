package com.blanke.mdwechat.util


object VXPUtils {

    // 必须主线程调用
    fun isVXPEnv(): Boolean {
        return System.getProperty("vxp") != null
    }
}

