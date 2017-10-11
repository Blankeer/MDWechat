package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class DiscoverHook : BaseHookUi() {
    //    private String[] hookMenus = {"find_friends_by_near", "find_friends_by_shake",
    //            "more_tab_game_recommend", "find_friends_by_qrcode","jd_market_entrance"};
    //    private boolean hookListViewBackground = false;
    private lateinit var hookMenus: MutableList<String>

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        hookMenus = arrayListOf()
        if (HookConfig.isHookmenu_game) {
            hookMenus.add("more_tab_game_recommend")
        }
        if (HookConfig.isHookmenu_shop) {
            hookMenus.add("jd_market_entrance")
        }
        if (HookConfig.isHookmenu_qrcode) {
            hookMenus.add("find_friends_by_qrcode")
        }
        if (HookConfig.isHookmenu_shake) {
            hookMenus.add("find_friends_by_shake")
        }
        if (HookConfig.isHookmenu_near) {
            hookMenus.add("find_friends_by_near")
        }
        if (hookMenus.size > 0) {
            xMethod(wxConfig.classes.MMPreferenceAdapter,
                    wxConfig.methods.MMPreferenceAdapter_setVisible,
                    String::class.java, Boolean::class.java,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                            val preName = param!!.args[0] as String
                            //                        log("preName=" + preName + ",show=" + param.args[1]);
                            for (hookMenu in hookMenus!!) {
                                if (hookMenu == preName) {
                                    param.args[1] = true
                                    break
                                }
                            }
                        }
                    })
        }
        val background = DrawableUtils.getExternalStorageAppBitmap(Common.DISCOVER_BACKGROUND_FILENAME)

        xMethod(wxConfig.classes.DiscoverFragment,
                wxConfig.methods.MainFragment_onTabCreate, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val listView = getObjectField(param!!.thisObject,
                        wxConfig.fields.PreferenceFragment_mListView) as View
                if (listView != null) {
                    if (background != null) {
                        listView.background = BitmapDrawable(background)
                    } else {
                        listView.background = whiteDrawable
                    }
                }
            }
        })
        if (HookConfig.isHookripple) {
            xMethod(wxConfig.classes.MMPreferenceAdapter,
                    "getView", Int::class.java, View::class.java, ViewGroup::class.java,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                            val view = param!!.result as View
                            view.background = getRippleDrawable(view.context)
                        }
                    })
        }
    }
}
