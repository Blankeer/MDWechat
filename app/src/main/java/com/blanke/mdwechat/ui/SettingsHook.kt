package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class SettingsHook : BaseHookUi() {

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        val background = DrawableUtils.getExternalStorageAppBitmap(Common.SETTINGS_BACKGROUND_FILENAME)
        xMethod(wxConfig.classes.SettingsFragment,
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
    }
}
