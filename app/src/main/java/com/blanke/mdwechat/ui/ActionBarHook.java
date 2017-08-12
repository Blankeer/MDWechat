package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCClasses.MMFragmentActivity;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class ActionBarHook extends BaseHookUi {

    //不开启状态栏沉浸的 activities
    public static String[] excludeStatusBarActivities = {};
    private ColorDrawable actionBarColorDrawable;

    public ActionBarHook() {
        actionBarColorDrawable = new ColorDrawable();
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        //设置状态栏颜色 actionbar
        findAndHookMethod(MMFragmentActivity, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                refreshPrefs();
                Activity activity = (Activity) param.thisObject;
                String activityName = activity.getClass().getName();
                int statusColor = WeChatHelper.colorPrimary;
                actionBarColorDrawable.setColor(statusColor);
                if (isSetActionBarActivity(activityName)) {
//                    Object actionbar = null;
//                    if (WeChatHelper.WCVersion.versionNumber == 0) {
//                        actionbar = XposedHelpers.callMethod(XposedHelpers.callMethod(activity, "cR"), "cS");
//                    } else if (WeChatHelper.WCVersion.versionNumber == 1) {
////                        actionbar = XposedHelpers.callMethod(XposedHelpers.callMethod(activity, "cO"), "cP");
//                    }
////                    log("actionbar=" + actionbar);
////                    if (actionbar != null) {
////                        XposedHelpers.callMethod(actionbar, "setBackgroundDrawable", actionBarColorDrawable);
////                    }
//                    View actionBar = activity.findViewById(getId(activity, "g7"));
//                    log("actionbar=" + actionBar);
//                    if (actionBar != null) {
//                        actionBar.setBackground(actionBarColorDrawable);
//                    } else {
//                        log(activityName + "  actionbar==null");
//                    }
                }
            }
        });
    }

    private boolean isSetActionBarActivity(String activity) {
        for (String act : excludeStatusBarActivities) {
            if (act.equals(activity)) {
                return false;
            }
        }
        return true;
    }
}
