package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class ConversationHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        val background = DrawableUtils.getExternalStorageAppBitmap(Common.CONVERSATION_BACKGROUND_FILENAME)
        xMethod(wxConfig.classes.ConversationFragment,
                wxConfig.methods.MainFragment_onTabCreate,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val listView = getObjectField(param!!.thisObject, wxConfig.fields.ConversationFragment_mListView) as View
                        if (background != null) {
                            listView.background = BitmapDrawable(background)
                        } else {
                            listView.background = whiteDrawable
                        }
                    }
                })
        if (HookConfig.isHookripple) {
            xMethod(wxConfig.classes.ConversationAdapter,
                    "getView", C.Int, C.View, C.ViewGroup,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                            //                            int i = (int) param.args[0];
                            //                            Object userInfo = XposedHelpers.callMethod(param.thisObject, "yQ", i);
                            //                            Object topInfo = XposedHelpers.callMethod(param.thisObject, "j", userInfo);
                            //                            boolean isTop = XposedHelpers.getBooleanField(topInfo, "vvv");
                            val view = param!!.result as View
                            //                            if (isTop) {
                            //                                view.setBackground(getGreyRippleDrawable(view.getContext()));
                            //                            } else {
                            view.background = getDefaultRippleDrawable(view.context)
                            //                            }
                        }
                    })
        }
    }
}
