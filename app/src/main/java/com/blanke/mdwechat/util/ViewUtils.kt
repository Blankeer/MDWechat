package com.blanke.mdwechat.util

import android.view.View

object ViewUtils {

    fun getParentView(view: View, index: Int): View? {
        var currentView = view
        for (i in 0 until index) {
            val v = currentView.parent
            if (v != null && v is View) {
                currentView = v
            } else {
                return null
            }
        }
        return currentView
    }
}