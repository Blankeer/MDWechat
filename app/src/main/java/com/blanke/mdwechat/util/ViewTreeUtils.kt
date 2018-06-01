package com.blanke.mdwechat.util

import android.view.View
import android.view.ViewGroup
import com.blanke.mdwechat.bean.ViewTreeItem

object ViewTreeUtils {

    /**
     * 判断 view 的层级结构是否和定义的相等
     */
    fun equals(tree: ViewTreeItem, view: View): Boolean {
        if (!equals(tree.clazz, view)) {
            return false
        }
        if (view is ViewGroup) {
            LogUtil.log("view.childCount=${view.childCount},tree.children.size=${tree.children.size}")
            if (view.childCount == tree.children.size) {
                for (i in 0 until view.childCount) {
                    if (!equals(tree.children[i], view.getChildAt(i))) {
                        return false
                    }
                }
                return true
            }
        } else {
            if (tree.children.isEmpty()) {
                return true
            }
        }
        return false
    }

    fun equals(clazz: String, view: View): Boolean {
        return view.javaClass.name.contains(clazz, true)
    }
}