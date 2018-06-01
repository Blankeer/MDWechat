package com.blanke.mdwechat.bean

class ViewTreeItem(
        val clazz: String,
        val children: Array<ViewTreeItem?> = arrayOf()
)