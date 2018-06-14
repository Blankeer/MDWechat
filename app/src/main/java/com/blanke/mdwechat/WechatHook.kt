package com.blanke.mdwechat

import com.blanke.mdwechat.hookers.*
import com.blanke.mdwechat.util.LogUtil.log
import com.gh0u1l5.wechatmagician.spellbook.SpellBook
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WechatHook : IXposedHookLoadPackage {
    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            if (SpellBook.isImportantWechatProcess(lpparam)) {
                log("hook wechat success")
                WeChatHelper.initPrefs()
                val hookers = mutableListOf(
                        LauncherUIHooker,
                        ActionBarHooker,
                        StatusBarHooker,
                        AvatarHooker,
                        ListViewHooker,
                        ConversationHooker,
                        ContactHooker,
                        DiscoverHooker,
                        SettingsHooker,
                        LogHooker
                )
                if (BuildConfig.DEBUG) {
                    hookers.add(DebugHooker)
                }
                SpellBook.startup(lpparam, hookers)
            }
        } catch (e: Throwable) {
            log(e)
        }
    }
}
