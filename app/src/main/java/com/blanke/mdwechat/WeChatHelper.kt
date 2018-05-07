package com.blanke.mdwechat

import android.content.Context
import android.content.Intent
import com.blanke.mdwechat.config.AppCustomConfig
import de.robv.android.xposed.XSharedPreferences
import java.io.File

object WeChatHelper {
    lateinit var XMOD_PREFS: XSharedPreferences

    fun startActivity(actName: String) {
        val context = Objects.Main.LauncherUI.get() ?: return
        val intent = Intent()
        intent.setClassName(context as Context, actName)
        context.startActivity(intent)
    }

    fun initPrefs() {
        XMOD_PREFS = XSharedPreferences(File(AppCustomConfig.getConfigFile(Common.MOD_PREFS + ".xml")))
        XMOD_PREFS.makeWorldReadable()
        XMOD_PREFS.reload()
    }

    fun reloadPrefs() {
        XMOD_PREFS.reload()
    }
}
