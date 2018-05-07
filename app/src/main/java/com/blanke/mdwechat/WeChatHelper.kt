package com.blanke.mdwechat

import android.content.Intent
import com.blanke.mdwechat.config.AppCustomConfig
import de.robv.android.xposed.XSharedPreferences
import java.io.File

object WeChatHelper {
    lateinit var XMOD_PREFS: XSharedPreferences

    fun startPluginActivity(classFullName: String?) {
//        if (classFullName == null) return
//        try {
//            val context = WxObjects.LauncherUI?.get() ?: return
//            val temp1 = classFullName.substringAfterLast("plugin.")
//            val className = temp1.substring(temp1.indexOf(".ui."), temp1.length)
//            val groupName = temp1.substringBefore(".ui")
//            log("startPluginActivity groupName=$groupName,className=$className")
////            val PluginHelper = XposedHelpers.findClass(wxConfig.classes.PluginHelper, loadPackageParam.classLoader)
////            XposedHelpers.callStaticMethod(PluginHelper, wxConfig.methods.PluginHelper_start,
////                    context as Context, groupName, className)
//        } catch (e: Exception) {
//            log("startPluginActivity $classFullName fail , class need 'com.tencent.mm.plugin.*.ui.*'")
//            log(e)
//        }
    }

    fun startActivity(actName: String) {
//        val context = WxObjects.LauncherUI?.get() ?: return
//        val intent = Intent()
//        intent.setClassName(context as Context, actName)
//        context.startActivity(intent)
    }

    fun startActivity(intent: Intent, actName: String?) {
        if (actName == null) return
//        val context = WxObjects.LauncherUI?.get() ?: return
//        intent.setClassName(context as Context, actName)
//        context.startActivity(intent)
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
