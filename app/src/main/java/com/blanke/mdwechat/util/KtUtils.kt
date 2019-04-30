package com.blanke.mdwechat.util

import android.os.Handler
import android.os.Looper
import kotlin.concurrent.thread

fun mainThread(delay: Long = 0, run: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(run, delay)
}

fun waitInvoke(checkInterval: Long = 100, mainThread: Boolean = false, check: () -> Boolean, callBack: () -> Unit) {
    thread {
        while (true) {
            Thread.sleep(checkInterval)
            var result = false
            if (mainThread) {
                var end = false
                com.blanke.mdwechat.util.mainThread {
                    result = check()
                    end = true
                }
                while (!end) {
                    Thread.sleep(100)
                }
            } else {
                result = check()
            }
            if (result) {
                if (mainThread) {
                    com.blanke.mdwechat.util.mainThread {
                        callBack()
                    }
                } else {
                    callBack()
                }
                break
            }
        }
    }
}