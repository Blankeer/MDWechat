package com.blanke.mdwechat

import android.content.Context
import android.content.res.XModuleResources
import com.blanke.mdwechat.config.WxObjects
import com.blanke.mdwechat.util.LogUtil.log
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.ref.WeakReference

class WechatHook : XC_MethodHook(), IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {
    private lateinit var MODULE_PATH: String

    @Throws(Throwable::class)
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        MODULE_PATH = startupParam.modulePath
    }

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Common.WECHAT_PACKAGENAME) {
            return
        }
        WeChatHelper.initPrefs()
        val context = XposedHelpers.callMethod(
                XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null),
                        "currentActivityThread"), "getSystemContext") as Context
        val wechatPackageInfo = context.packageManager.getPackageInfo(Common.WECHAT_PACKAGENAME, 0)
        WxObjects.Application = WeakReference(context)
        val versionName = wechatPackageInfo.versionName
        log("wechat version=" + versionName
                + ",processName=" + lpparam.processName
                + ",MDWechat version=" + BuildConfig.VERSION_NAME)
        try {
            WeChatHelper.init(versionName, lpparam)
        } catch (e: Throwable) {
            log(e)
        }
    }

    private fun hookSettings(lpparam: XC_LoadPackage.LoadPackageParam, versionName: String, isSupport: Boolean) {
        XposedHelpers.findAndHookMethod("com.blanke.mdwechat.settings.SettingsFragment",
                lpparam.classLoader, "getWeChatVersion", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                param!!.result = versionName
            }
        })
        XposedHelpers.findAndHookMethod("com.blanke.mdwechat.settings.SettingsFragment",
                lpparam.classLoader, "isSupportWechat", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                param!!.result = isSupport
            }
        })

    }

    @Throws(Throwable::class)
    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != Common.WECHAT_PACKAGENAME) {
            return
        }
        //        log("handleInitPackageResources");
        val modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res)
        //        WeChatHelper.WCId.ic_add = resparam.res.addResource(modRes, R.drawable.ic_add);


        //        resparam.res.setReplacement(Common.WECHAT_PACKAGENAME,
        //                "color", "z", Color.RED);
        //        resparam.res.setReplacement(Common.WECHAT_PACKAGENAME,
        //                "drawable", WeChatHelper.WCDrawable.Conference_ListView_Item_Background,
        //                modRes.fwd(R.drawable.selector_item));
    }

}
