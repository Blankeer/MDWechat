package com.blanke.mdwechat.plugins

import com.blanke.mdwechat.util.LogUtil.log
import com.gh0u1l5.wechatmagician.spellbook.interfaces.IXLogHook

object XLogPlugin : IXLogHook {
    override fun onXLogWrite(level: String, tag: String, msg: String) {
        log("XLogPlugin level = $level , tag = $tag , msg = $msg")
    }
}