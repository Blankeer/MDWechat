package com.blanke.mdwechat.util


object VXPUtils {

    // 必须主线程调用
    fun isVXPEnv(): Boolean {
        val traces = Thread.currentThread().stackTrace
        val allTraces = traces.joinToString { it.className }
        return allTraces.contains("com.lody.virtual") || allTraces.contains("me.weishu.exposed")
    }
}

