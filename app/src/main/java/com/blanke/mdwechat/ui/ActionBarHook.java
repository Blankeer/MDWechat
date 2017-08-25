package com.blanke.mdwechat.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xMethod;
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
        xMethod(wxConfig.classes.ActionBarContainer,
                "onFinishInflate",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        setObjectField(param.thisObject,
                                wxConfig.fields.ActionBarContainer_mBackground,
                                getActionBarColorDrawable());
                    }
                });
        //set statusBar color
        findAndHookMethod("com.android.internal.policy.PhoneWindow", lpparam.classLoader,
                "setStatusBarColor", int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        int statusColor = WeChatHelper.colorPrimary;
                        param.args[0] = statusColor;
                    }
                });
        //hook ToolbarWidgetWrapper
        xMethod(wxConfig.classes.ToolbarWidgetWrapper,
                "setCustomView", View.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View view = (View) param.args[0];
                        logViewTree(view);
                        View backIv = findViewByIdName(view, wxConfig.views.ActionBar_BackImageView);
                        if (backIv != null) {
//                            log("backIv=" + getViewMsg(backIv));
                        }
                        View divider = findViewByIdName(view, wxConfig.views.ActionBar_Divider);
                        if (divider != null) {
//                            log("divider=" + getViewMsg(divider));
                            divider.setVisibility(View.GONE);
                        }
                        View divider2 = findViewByIdName(view, wxConfig.views.SearchActionBar_Divider);
                        if (divider2 != null) {
                            log("search actionbar divider2=" + getViewMsg(divider2));
                            divider2.setVisibility(View.GONE);
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
