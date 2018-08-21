package com.blanke.mdwechat.auto_search

import com.blanke.mdwechat.auto_search.bean.LogEvent
import com.blankj.utilcode.util.LogUtils
import org.greenrobot.eventbus.EventBus

object Logs {
    fun i(msg: String) {
        EventBus.getDefault().post(LogEvent(msg))
        LogUtils.i(msg)
    }
}