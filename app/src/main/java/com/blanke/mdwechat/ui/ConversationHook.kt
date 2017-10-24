package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class ConversationHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(wxConfig.classes.ConversationFragment,
                wxConfig.methods.MainFragment_onTabCreate,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val listView = getObjectField(param!!.thisObject, wxConfig.fields.ConversationFragment_mListView) as View
                        val background = AppCustomConfig.getTabBg(0)
                        listView.background = if (background != null) BitmapDrawable(background) else whiteDrawable
                    }
                })
        xMethod(wxConfig.classes.ConversationAdapter,
                "getView", C.Int, C.View, C.ViewGroup,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                        if (!HookConfig.is_hook_ripple) {
                            return
                        }
                        val i = param.args [0] as Int
                        val userInfo = XposedHelpers.callMethod(param.thisObject, wxConfig.methods.ConversationAdapter_getUserInfo, i)
                        val topInfo = XposedHelpers.callMethod(param.thisObject, wxConfig.methods.ConversationAdapter_getTopInfo, userInfo)
                        val isTop = XposedHelpers.getBooleanField(topInfo, wxConfig.fields.TopInfo_isTop)
                        val view = param.result as View
//                        log("$i isTop = $isTop")
                        view.background = if (!isTop) getTransparentRippleDrawable() else getRippleDrawable(HookConfig.get_color_conversation_top)
                    }
                })
    }
}
