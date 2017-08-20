package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCClasses.ChattingUInonFragment;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.MMFragmentActivity;
import static com.blanke.mdwechat.WeChatHelper.WCField.ActionBarContainer_mBackground;
import static com.blanke.mdwechat.WeChatHelper.WCId.ActionBar_Divider_id;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.getActionBar;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.getActionBarActivity;
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
//        XposedHelpers.findAndHookConstructor("android.support.v7.widget.ActionBarContainer",
//                lpparam.classLoader, Context.class, AttributeSet.class, new XC_MethodHook() {
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        setObjectField(param.thisObject, ActionBarContainer_mBackground, getActionBarColorDrawable());
//                    }
//                });
        XposedHelpers.findAndHookMethod("android.support.v7.widget.ActionBarContainer",
                lpparam.classLoader, "onFinishInflate", new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        setObjectField(param.thisObject, ActionBarContainer_mBackground, getActionBarColorDrawable());
                    }
                });
        //设置状态栏颜色 actionbar,divider
        findAndHookMethod(MMFragmentActivity, "onResume", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                refreshPrefs();
                int statusColor = WeChatHelper.colorPrimary;
                activity.getWindow().setStatusBarColor(statusColor);
                //hide divider
                Object actionBarWrap = XposedHelpers.callMethod(
                        XposedHelpers.callMethod(activity, getActionBarActivity), getActionBar);
                View actionBar = (View) XposedHelpers.callMethod(actionBarWrap, "getCustomView");
                if (actionBar != null) {
                    //search
                    View divider = null;
//                    log("activity=" + activity.getClass().getName());
                    if (activity.getClass().getName().endsWith("FTSMainUI")) {
                        //todo
                        divider = actionBar.findViewById(getId(activity, "h0"));
                    } else {
                        divider = actionBar.findViewById(getId(activity, ActionBar_Divider_id));
                    }
//                    log("divider=" + divider);
                    if (divider != null) {
                        divider.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        //fragment divider hide
        findAndHookMethod(ChattingUInonFragment, "bSZ", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View actionBar = (View) XposedHelpers.getObjectField(param.thisObject, "vqP");
                if (actionBar != null) {
                    View divider = actionBar.findViewById(getId(actionBar.getContext(), ActionBar_Divider_id));
                    if (divider != null) {
                        divider.setVisibility(View.INVISIBLE);
                    }
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
