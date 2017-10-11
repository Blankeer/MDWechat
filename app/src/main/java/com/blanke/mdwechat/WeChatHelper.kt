package com.blanke.mdwechat

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Environment
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.config.WxVersionConfig
import com.blanke.mdwechat.ui.*
import com.blanke.mdwechat.ui.LogUtil.log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

/**
 * Created by blanke on 2017/7/29.
 */

object WeChatHelper {
    private lateinit var loadPackageParam: XC_LoadPackage.LoadPackageParam
    lateinit var XMOD_PREFS: XSharedPreferences
    private lateinit var hookUis: MutableList<BaseHookUi>
    lateinit var wxConfig: WxVersionConfig

    @Throws(Throwable::class)
    fun init(ver: String, lpparam: XC_LoadPackage.LoadPackageParam) {
        loadPackageParam = lpparam
        initApplication(ver, lpparam)
    }

    private fun initApplication(ver: String, lpparam: XC_LoadPackage.LoadPackageParam) {
        findAndHookMethod(Application::class.java, "attach", Context::class.java, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                val application = param.thisObject as Application
                val MD_CONTEXT = application.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
                try {
                    wxConfig = WxVersionConfig.loadConfig(MD_CONTEXT, ver)
                    log(wxConfig.toString())
                } catch (e: Exception) {
                    log("不支持的版本:" + ver)
                    return
                }

                log("支持该微信版本:" + ver)
                initHookUis()
                xMethod(wxConfig.classes.LauncherUI, "onCreate", Bundle::class.java,
                        object : XC_MethodHook() {
                            @Throws(Throwable::class)
                            override fun beforeHookedMethod(param1: XC_MethodHook.MethodHookParam?) {
                                val hook = HookConfig.isHookswitch
                                log("hook 开关:" + hook)
                                if (hook) {
                                    executeHookUi()
                                }
                            }
                        })
                createAppDir()
            }
        })
    }

    private fun createAppDir() {
        val appDir = File(Environment.getExternalStorageDirectory().absoluteFile.toString()
                + File.separator + Common.APP_DIR + File.separator)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
    }

    private fun initHookUis() {
//        hookUis = ArrayList<BaseHookUi>()
        hookUis = arrayListOf()
        hookUis.add(MainHook())
        hookUis.add(ListViewHook())
        hookUis.add(ActionBarHook())
        hookUis.add(ConversationHook())
        hookUis.add(AvatarHook())
        hookUis.add(ContactHook())
        hookUis.add(UnreadViewHook())
        hookUis.add(DiscoverHook())
        hookUis.add(SettingsHook())
        hookUis.add(ChatHook())
    }

    private fun executeHookUi() {
        hookUis.forEach { hookUi ->
            try {
                hookUi.hook(loadPackageParam)
            } catch (e: Throwable) {
                log(hookUi.javaClass.simpleName + " hook fail,msg=" + e.message)
                log(e)
            }
        }
    }

    fun initPrefs() {
        XMOD_PREFS = XSharedPreferences(Common.MY_APPLICATION_PACKAGE, Common.MOD_PREFS)
        XMOD_PREFS.makeWorldReadable()
    }

    fun xMethod(className: String, methodName: String, vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {
        return findAndHookMethod(className, loadPackageParam.classLoader, methodName, *parameterTypesAndCallback)
    }

    fun xClass(className: String): Class<*> {
        return findClass(className, loadPackageParam.classLoader)
    }
}
