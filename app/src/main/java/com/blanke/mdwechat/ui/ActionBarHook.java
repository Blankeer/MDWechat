package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCField.ActionBarContainer_mBackground;
import static com.blanke.mdwechat.WeChatHelper.WCId.ActionBar_Divider_id;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

/**
 * Created by blanke on 2017/8/1.
 */

public class ActionBarHook extends BaseHookUi {

    //不开启状态栏沉浸的 activities
    public static String[] excludeStatusBarActivities = {};
    private ColorDrawable actionBarColorDrawable;
    private ColorDrawable actionBarSplitDrawable;

    public ActionBarHook() {
        actionBarColorDrawable = new ColorDrawable();
        actionBarSplitDrawable = new ColorDrawable();
        actionBarSplitDrawable.setColor(Color.TRANSPARENT);
    }

    private ColorDrawable getActionBarColorDrawable() {
        refreshPrefs();
        int statusColor = WeChatHelper.colorPrimary;
        actionBarColorDrawable.setColor(statusColor);
        return actionBarColorDrawable;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.support.v7.widget.ActionBarContainer",
                lpparam.classLoader, "onFinishInflate", new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        setObjectField(param.thisObject, ActionBarContainer_mBackground, getActionBarColorDrawable());
                    }
                });
        findAndHookMethod(View.class, "onAttachedToWindow", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View view = (View) param.thisObject;
                if (view instanceof ImageView
                        && view.getId() == getId(view.getContext(), ActionBar_Divider_id)) {
//                    log("view ActionBar_Divider hook success");
                    view.setVisibility(View.INVISIBLE);
                }
            }
        });
        //设置状态栏颜色 actionbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity activity = (Activity) param.thisObject;
                    refreshPrefs();
                    int statusColor = WeChatHelper.colorPrimary;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.getWindow().setStatusBarColor(statusColor);
                    }
                }
            });
        }
        //chat fragment 单独处理
//        findAndHookMethod("android.support.v4.app.Fragment", lpparam.classLoader,
//                "onResume",
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        log("fragment=" + param.thisObject.getClass().getName());
//                        Activity activity = (Activity) XposedHelpers.callMethod(param.thisObject, "aG");
//                        log("fragment in activity :" + activity.getClass().getName());
//                        View actionbar = activity.findViewById(getId(activity, ActionBar_id));
//                        log("actionbar=" + actionbar);
//                        if (actionbar != null
//                                && actionbar.getClass().getName().endsWith("ActionBarContainer")) {
//                            actionbar.setBackground(getActionBarColorDrawable());
//                        }
//                    }
//                });
//        findAndHookMethod(Activity.class, "onStart", new XC_MethodHook() {
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                log("Activity=" + param.thisObject.getClass().getName());
//                Activity activity = (Activity) param.thisObject;
//                String activityName = activity.getClass().getName();
//                View actionbar = activity.findViewById(getId(activity, ActionBar_id));
//                log("actionbar=" + actionbar);
//                if (actionbar != null
//                        && actionbar.getClass().getName().endsWith("ActionBarContainer")) {
////                    log("activity ,onResume()    " + activityName);
//                    actionbar.setBackground(getActionBarColorDrawable());
//                }
//            }
//        });
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
