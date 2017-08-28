package com.blanke.mdwechat.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
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

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        xMethod(wxConfig.classes.ActionBarContainer,
                "onFinishInflate",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        setObjectField(param.thisObject,
                                wxConfig.fields.ActionBarContainer_mBackground,
                                getColorPrimaryDrawable());
                        View actionbar = (View) param.thisObject;
                        actionbar.setElevation(0);
                    }
                });
        //set statusBar color
        findAndHookMethod("com.android.internal.policy.PhoneWindow", lpparam.classLoader,
                "setStatusBarColor", int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[0] = getColorPrimary();
                    }
                });
        //hook ToolbarWidgetWrapper
        xMethod(wxConfig.classes.ToolbarWidgetWrapper,
                "setCustomView", View.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View view = (View) param.args[0];
//                        logViewTree(view);
                        ImageView backIv = (ImageView) findViewByIdName(view, wxConfig.views.ActionBar_BackImageView);
                        if (backIv != null) {
                            //TODO 删除selector 按下
//                            Drawable drawable = backIv.getDrawable();
//                            log("back drawable =" + drawable + "," + drawable.getClass().getName());
//                            if (drawable instanceof StateListDrawable) {
//                                log("back image is StateListDrawable");
//                                StateListDrawable stateListDrawable = (StateListDrawable) drawable;
//                                stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_long_pressable}, new ColorDrawable(Color.RED));
//                                backIv.setImageDrawable(stateListDrawable);
//                            }
                            backIv.setBackground(getRippleDrawable(view.getContext()));
                        }
                        View divider = findViewByIdName(view, wxConfig.views.ActionBar_Divider);
                        if (divider != null) {
                            divider.setVisibility(View.GONE);
                        }
                    }
                });
        //hook search actionbar
        xMethod(wxConfig.classes.ActionBarSearchView,
                wxConfig.methods.ActionBarSearchView_init, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View view = (View) param.thisObject;
                        View divider2 = findViewByIdName(view, wxConfig.views.SearchActionBar_Divider);
                        if (divider2 != null) {
//                            log("search actionbar divider2=" + getViewMsg(divider2));
                            divider2.setVisibility(View.GONE);
                        }
                        ImageView backIv = (ImageView) findViewByIdName(view, wxConfig.views.SearchActionBar_BackImageView);
                        if (backIv != null) {
                            backIv.setBackground(getRippleDrawable(view.getContext()));
                        }
                    }
                });
        findAndHookConstructor(wxConfig.classes.ActionBarEditText,
                lpparam.classLoader, Context.class, AttributeSet.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        EditText editText = (EditText) param.thisObject;
                        editText.setTextColor(Color.WHITE);
                        editText.setHintTextColor(Color.WHITE);
                        editText.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
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
