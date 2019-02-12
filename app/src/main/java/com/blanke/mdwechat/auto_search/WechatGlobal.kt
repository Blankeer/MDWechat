package com.blanke.mdwechat.auto_search

import com.blanke.mdwechat.Version


object WechatGlobal {
    var wxPackageName: String = ""
    var wxVersion: Version? = null
    lateinit var wxLoader: ClassLoader
    lateinit var wxClasses: List<String>

}