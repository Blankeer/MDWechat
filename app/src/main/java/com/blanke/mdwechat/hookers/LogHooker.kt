package com.blanke.mdwechat.hookers

import android.util.Log
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.util.FileUtils
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object LogHooker : HookerProvider {
    private val dateStr
        get() = SimpleDateFormat("yyyy-MM-dd").format(Date())

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(LogIHooker, LogEHooker)
    }

    private val LogIHooker = Hooker {
        XposedBridge.hookAllMethods(Log::class.java, "i", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val msg = param.args[1] as String
                if (msg.contains("mdwechat", true)) {
                    printLog(msg)
                }
            }
        })
    }

    private val LogEHooker = Hooker {
        XposedBridge.hookAllMethods(Log::class.java, "i", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val msg = param.args[1] as String
                if (msg.contains("mdwechat", true)) {
                    printLog(msg)
                }
            }
        })
    }

    private fun printLog(log: String) {
        val logFile = File(AppCustomConfig.getLogFile(dateStr))
        logFile.parentFile.mkdirs()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        FileUtils.write(logFile.absolutePath, "$time $log\n", true)
    }
}