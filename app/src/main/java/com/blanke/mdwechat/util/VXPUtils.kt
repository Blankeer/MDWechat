package com.blanke.mdwechat.util


object VXPUtils {
    fun isVXPEnv(): Boolean {
        return System.getProperty("vxp") != null
    }
}

