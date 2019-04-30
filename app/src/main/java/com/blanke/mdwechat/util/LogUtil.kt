package com.blanke.mdwechat.util

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by blanke on 2017/10/3.
 */

object LogUtil {
    @JvmStatic
    fun log(msg: String) {
        Log.i("MDWechatModule", "MDWechat $msg")
    }

    fun log(t: Throwable) {
        Log.e("MDWechatModule", "MDWechat " + Log.getStackTraceString(t))
    }

    fun bundleToString(bundle: Bundle?): String? {
        val str = bundle?.keySet()?.joinToString(", ") {
            "$it = ${bundle[it]}"
        }
        return "{$str}"
    }

    fun logSuperClasses(clazz: Class<*>) {
        log(clazz.name)
        if (clazz.superclass != null) {
            logSuperClasses(clazz.superclass)
        }
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

    fun logView(view: View) {
        log(getViewLogInfo(view))
    }

    fun getViewLogInfo(view: View): String {
        val sb = StringBuffer(view.toString())
        if (view is TextView) {
            sb.append("${view.text}(" + view.hint + ")")
        } else if (view is ViewGroup) {
            sb.append(" childCount = ${view.childCount}")
        }
        sb.append(" desc= ${view.contentDescription}")
        return sb.toString()
    }

    fun logViewStackTraces(view: View, level: Int = 0) {
        val sb = StringBuffer()
        for (i in 0..level) {
            sb.append("\t")
        }
        sb.append(getViewLogInfo(view))
        log(sb.toString())
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                logViewStackTraces(view.getChildAt(i), level + 1)
            }
        }
    }

    fun findTextViewStackTrace(view: View, text: String): View? {
        if (view is TextView) {
            if (view.text.contains(text)) {
                return view
            }
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val res = findTextViewStackTrace(view.getChildAt(i), text)
                if (res != null) {
                    return res
                }
            }
        }
        return null
    }
}
