package com.blanke.mdwechat.util

import android.os.Bundle
import android.view.View
import android.widget.TextView
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

    fun bundleToString(bundle: Bundle?): String? {
        val str = bundle?.keySet()?.joinToString(", ") {
            "$it = ${bundle[it]}"
        }
        return "{$str}"
    }

    fun logStackTraces(methodCount: Int = 15, methodOffset: Int = 3) {
        val trace = Thread.currentThread().stackTrace
        var level = ""
        log("---------logStackTraces start----------")
        for (i in methodCount downTo 1) {
            val stackIndex = i + methodOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].className)
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            log(builder.toString())
        }
        log("---------logStackTraces end----------")
    }

    fun logParentView(view: View, level: Int = 5) {
        log("---------logParentView start----------")
        var currentView = view
        for (i in 0 until level) {
            val v = currentView.parent
            if (v != null && v is View) {
                logView(v)
                currentView = v
            }
        }
        log("---------logParentView end----------")
    }

    fun logView(view: View): String {
        val className = view.javaClass.name
        val id = view.id
        var text = ""
        if (view is TextView) {
            val textView = view
            text = textView.text.toString()
            text += "(" + textView.hint + ")"
        }
        return "$className,$id,$text,$view"
    }

}
