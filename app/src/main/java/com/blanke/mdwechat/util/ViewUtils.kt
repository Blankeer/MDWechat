package com.blanke.mdwechat.util

import android.view.View
import android.view.ViewGroup

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

    fun findFirstChildView(rootView: ViewGroup, viewClassName: String): View? {
        val childCount = rootView.childCount
        for (i in 0 until childCount) {
            val v = rootView.getChildAt(i)
            if (v.javaClass.name == viewClassName) {
                return v
            } else if (v is ViewGroup) {
                return findFirstChildView(v, viewClassName)
            }
        }
        return null
    }
}