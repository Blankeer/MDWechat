package com.blanke.mdwechat

import android.content.Context
import android.os.Environment
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.config.WxClass
import com.blanke.mdwechat.config.WxObjects
import com.blanke.mdwechat.config.WxVersionConfig
import com.blanke.mdwechat.ui.BaseHookUi
import com.blanke.mdwechat.ui.LogHook
import com.blanke.mdwechat.ui.MainHook
import com.blanke.mdwechat.util.LogUtil.log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File
import java.lang.ref.WeakReference

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
        createAppDir()
        try {
            wxConfig = WxVersionConfig.loadConfig(ver)
        } catch (e: Throwable) {
            log("不支持的版本:" + ver)
            return
        }
        log("支持该微信版本:" + ver)
        WxClass.init(wxConfig, lpparam.classLoader)
        initHookUis()
        val hook = HookConfig.isHookswitch
        log("hook 开关:" + hook)
        if (hook) {
            val application = WxObjects.Application?.get()
            if (application == null) {
                log("application = $application")
                return
            }
            val context = application.createPackageContext(Common.MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
            WxObjects.MdContext = WeakReference(context)
            //防止被回收,弱引用
            XposedHelpers.setAdditionalInstanceField(application, "hookContext", context)
            executeHookUi()
        }
    }

    private fun createAppDir() {
        val appDir = File(Environment.getExternalStorageDirectory().absoluteFile.toString()
                + File.separator + Common.APP_DIR + File.separator)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
    }

    private fun initHookUis() {
        hookUis = arrayListOf()
        if (BuildConfig.DEBUG) {
            hookUis.add(LogHook())
        }
        hookUis.add(MainHook())
//        hookUis.add(ListViewHook())
//        hookUis.add(ActionBarHook())
//        hookUis.add(ConversationHook())
//        hookUis.add(AvatarHook())
//        hookUis.add(ContactHook())
//        hookUis.add(UnreadViewHook())
//        hookUis.add(DiscoverHook())
//        hookUis.add(SettingsHook())
//        hookUis.add(ChatHook())
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

    fun xMethod(clazz: Class<*>, methodName: String, vararg parameterTypesAndCallback: Any): XC_MethodHook.Unhook {
        return findAndHookMethod(clazz, methodName, *parameterTypesAndCallback)
    }

    fun xClass(className: String): Class<*> {
        return findClass(className, loadPackageParam.classLoader)
    }
}
