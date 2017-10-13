package com.blanke.mdwechat.bean

/**
 * Created by blanke on 2017/10/13.
 */

data class FloatButtonConfig(var info: String = "", var menu: FLoatButtonConfigItem?, var items: Array<FLoatButtonConfigItem>?)

data class FLoatButtonConfigItem(var order: Int = 0, var type: String = "", var icon: String = "", var text: String = "")
