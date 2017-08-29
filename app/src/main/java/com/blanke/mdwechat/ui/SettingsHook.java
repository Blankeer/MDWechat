package com.blanke.mdwechat.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.blanke.mdwechat.config.HookConfig;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class SettingsHook extends BaseHookUi {
    private boolean hookListViewBackground = false;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!HookConfig.isHookripple()) {
            return;
        }
        xMethod(wxConfig.classes.SettingsFragment,
                wxConfig.methods.MainFragment_onTabCreate, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View listView = (View) XposedHelpers.getObjectField(param.thisObject,
                                wxConfig.fields.PreferenceFragment_mListView);
                        if (listView != null) {
                            listView.setBackground(new ColorDrawable(Color.WHITE));
                        }
                    }
                });
    }

    private Drawable getBackground() {
        return getColorPrimaryDrawable();
    }
}
