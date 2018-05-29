package com.blanke.mdwechat.hookers

import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.Fragment
import android.view.View
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.Fields.ConversationFragment_mListView
import com.blanke.mdwechat.Methods.ConversationWithAppBrandListView_isAppBrandHeaderEnable
import com.blanke.mdwechat.WeChatHelper.whiteDrawable
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.LogUtil
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object ConversationHooker : HookerProvider {
    const val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(resumeHook, disableAppBrand)
    }

    private val resumeHook = Hooker {
        XposedHelpers.findAndHookMethod(Classes.Fragment, "performResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                LogUtil.log("Fragment.performResume f=${param!!.thisObject::class.java.name}")
                val fragment = param?.thisObject
                if (fragment.javaClass.name != Classes.ConversationFragment.name) {
                    return
                }
//                LogUtil.logSuperClasses(param!!.thisObject::class.java)
                LogUtil.log("ConversationFragment.onResume(): ${param!!.thisObject is Fragment}")
                val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                if (isInit != null) {
                    LogUtil.log("ConversationFragment 已经hook过")
                    return
                }
                XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                init(fragment)
            }

            private fun init(fragment: Any) {
                val listView = ConversationFragment_mListView.get(fragment)
                if (listView != null && listView is View) {
                    val background = AppCustomConfig.getTabBg(0)
                    listView.background = if (background != null) BitmapDrawable(background) else whiteDrawable
                }
            }
        })
        //
//        XposedBridge.hookAllConstructors(ConversationWithCacheAdapter, object : XC_MethodHook() {
//            override fun afterHookedMethod(param: MethodHookParam?) {
//                val context = param?.args!![0]
//                val arg1 = param?.args!![1]
//                log("ConversationWithCacheAdapter context=$context,arg1=$arg1")
////                log("LauncherUI is FragmentActivity = ${context is FragmentActivity}")
//
////                if (context.javaClass.name == Classes.LauncherUI.name) {
////                val act = context as FragmentActivity
//                val fm = XposedHelpers.callMethod(context, "getSupportFragmentManager")
//                log(" fm = $fm,class=${fm.javaClass.name}")
//                val fragmets = XposedHelpers.callMethod(fm, "be") as List<*>
//                log("fm= $fm,fragment =${fragmets}")
//
//                fragmets.forEach {
//                    log("LauncherUI fragment = $it ,class=${it?.javaClass?.name},id = ${XposedHelpers.callMethod(it!!, "getId")}")
//                }
//            }
//        })
    }
    private val disableAppBrand = Hooker {
        XposedHelpers.findAndHookMethod(ConversationWithAppBrandListView,
                ConversationWithAppBrandListView_isAppBrandHeaderEnable.name, C.Boolean, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                if (HookConfig.is_hook_remove_appbrand) {
                    param?.result = false
                }
            }
        })
    }
}