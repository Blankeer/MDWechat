package com.blanke.mdwechat.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCClasses.MMFragmentActivity;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class StatusBarHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findAndHookMethod(MMFragmentActivity, "onCreate", Bundle.class, new XC_MethodHook() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    refreshPrefs();
                    Activity activity = (Activity) param.thisObject;
                    int statusColor = WeChatHelper.colorPrimary;
                    activity.getWindow().setStatusBarColor(statusColor);
                }
            });
        }
    }
}
