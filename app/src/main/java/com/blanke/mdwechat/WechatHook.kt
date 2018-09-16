package com.blanke.mdwechat

import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.config.WxVersionConfig
import com.blanke.mdwechat.hookers.*
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.LogUtil.log
import com.blanke.mdwechat.util.VXPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WechatHook : IXposedHookLoadPackage, IXposedHookZygoteInit {
    private var modulePath: String? = null

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        LogUtil.log("modulePath=$modulePath")
    }

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            if (!(lpparam.packageName.contains("com.tencent") && lpparam.packageName.contains("mm")))
                return
            // 暂时不 hook 小程序
            if (lpparam.processName.contains("appbrand")) {
                return
            }
            WeChatHelper.initPrefs()
            if (!HookConfig.is_hook_switch) {
                log("模块总开关已关闭")
                return
            }
            if (notNeedHook()) {
                log("notNeedHook：VXP 不需要hook")
                return
            }
            log("模块加载成功")
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
                    SchemeHooker,
                    LogHooker
            )
            if (BuildConfig.DEBUG) {
                hookers.add(0, DebugHooker)
            }
            hookMain(lpparam, hookers)
        } catch (e: Throwable) {
            log(e)
        }
    }

    private fun hookMain(lpparam: XC_LoadPackage.LoadPackageParam, plugins: List<HookerProvider>) {
        WechatGlobal.init(lpparam)
        try {
            WechatGlobal.wxVersionConfig = WxVersionConfig.loadConfig(WechatGlobal.wxVersion!!.toString())
        } catch (e: Exception) {
            log("${WechatGlobal.wxVersion} 配置文件不存在或解析失败")
            return
        }
        log("wechat version=" + WechatGlobal.wxVersion
                + ",processName=" + lpparam.processName
                + ",MDWechat version=" + BuildConfig.VERSION_NAME)
        plugins.forEach { provider ->
            provider.provideStaticHookers()?.forEach { hooker ->
                if (!hooker.hasHooked) {
                    hooker.hook()
                    hooker.hasHooked = true
                }
            }
        }
    }

    private fun notNeedHook(): Boolean {
        // 判断当前微信和 mdwechat 是不是 vxp 环境,真实环境下的 mdwechat 不 hook VXP 下的微信
        val isMdwechatVXP = modulePath?.contains("exposed") ?: false
        val isWechatVXP = VXPUtils.isVXPEnv()
        LogUtil.log("modulePath=$modulePath")
        LogUtil.log("isMdwechatVXP=$isMdwechatVXP")
        LogUtil.log("isWechatVXP=$isWechatVXP")
        return !isMdwechatVXP && isWechatVXP
    }
}

