package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.WxClass
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class SettingsHook : BaseHookUi() {

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(WxClass.SettingsFragment!!,
                wxConfig.methods.MainFragment_onTabCreate, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val listView = getObjectField(param!!.thisObject,
                        wxConfig.fields.PreferenceFragment_mListView) as View
                val background = AppCustomConfig.getTabBg(3)
                if (background != null) {
                    listView.background = BitmapDrawable(background)
                } else {
                    listView.background = whiteDrawable
                }
            }
        })
    }
}
