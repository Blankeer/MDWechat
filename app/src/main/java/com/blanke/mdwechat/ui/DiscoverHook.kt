package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class DiscoverHook : BaseHookUi() {
    //    private String[] hookMenus = {"find_friends_by_near", "find_friends_by_shake",
    //            "more_tab_game_recommend", "find_friends_by_qrcode","jd_market_entrance"};
    // album_dyna_photo_ui_title, app_brand_entrance
    //    private boolean hookListViewBackground = false;
    private lateinit var hookMenus: MutableList<String>

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(wxConfig.classes.MMPreferenceAdapter,
                wxConfig.methods.MMPreferenceAdapter_setVisible,
                C.String, C.Boolean,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        hookMenus = arrayListOf()
                        if (HookConfig.is_hook_menu_game) {
                            hookMenus.add("more_tab_game_recommend")
                        }
                        if (HookConfig.is_hook_menu_shop) {
                            hookMenus.add("jd_market_entrance")
                        }
                        if (HookConfig.is_hook_menu_qrcode) {
                            hookMenus.add("find_friends_by_qrcode")
                        }
                        if (HookConfig.is_hook_menu_shake) {
                            hookMenus.add("find_friends_by_shake")
                        }
                        if (HookConfig.is_hook_menu_near) {
                            hookMenus.add("find_friends_by_near")
                        }
                        if (HookConfig.is_hook_menu_sns) {
                            hookMenus.add("album_dyna_photo_ui_title")
                        }
                        if (HookConfig.is_hook_menu_appbrand) {
                            hookMenus.add("app_brand_entrance")
                        }
                        if (hookMenus.size == 0) {
                            return
                        }
                        val preName = param!!.args[0] as String
                        //                        log("preName=" + preName + ",show=" + param.args[1])
                        for (hookMenu in hookMenus) {
                            if (hookMenu == preName) {
                                param.args[1] = true
                                break
                            }
                        }
                    }
                })
        val background = AppCustomConfig.getTabBg(2)

        xMethod(wxConfig.classes.DiscoverFragment,
                wxConfig.methods.MainFragment_onTabCreate, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val listView = getObjectField(param!!.thisObject,
                        wxConfig.fields.PreferenceFragment_mListView) as ViewGroup
                if (background != null) {
                    listView.background = BitmapDrawable(background)
                } else {
                    listView.background = whiteDrawable
                }
//                logViewTree(listView)
//                for (i in 0..listView.childCount) {
//                    val item = listView.getChildAt(i)
////                    log("${item.javaClass} layoutParams=${item.layoutParams} ")
//                    if (item != null && item is TextView && item.contentDescription == "分隔栏") {
////                        listView.removeViewInLayout(item)
//                        listView.requestLayout()
//                    }
//                }
            }
        })

        xMethod(wxConfig.classes.MMPreferenceAdapter,
                "getView", C.Int, C.View, C.ViewGroup,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val view = param!!.result as View
                        if (HookConfig.is_hook_ripple) {
                            if (view is TextView && view.contentDescription == "分隔栏") {
                                view.visibility = View.GONE
                            } else {
                                view.background = getTransparentRippleDrawable()
                            }
                        }
                    }
                })
    }
}
