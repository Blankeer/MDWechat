package com.blanke.mdwechat.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.blanke.mdwechat.Common;
import com.blanke.mdwechat.config.HookConfig;
import com.blanke.mdwechat.util.DrawableUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by blanke on 2017/8/1.
 */

public class ConversationHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        final Bitmap background = DrawableUtils.getExternalStorageAppBitmap(Common.CONVERSATION_BACKGROUND_FILENAME);
        xMethod(wxConfig.classes.ConversationFragment,
                wxConfig.methods.MainFragment_onTabCreate,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View listView = (View) getObjectField(param.thisObject, wxConfig.fields.ConversationFragment_mListView);
                        if (background != null) {
                            listView.setBackground(new BitmapDrawable(background));
                        } else {
                            listView.setBackground(getWhiteDrawable());
                        }
                    }
                });
        if (HookConfig.isHookripple()) {
            xMethod(wxConfig.classes.ConversationAdapter,
                    "getView", int.class, View.class, ViewGroup.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                            int i = (int) param.args[0];
//                            Object userInfo = XposedHelpers.callMethod(param.thisObject, "yQ", i);
//                            Object topInfo = XposedHelpers.callMethod(param.thisObject, "j", userInfo);
//                            boolean isTop = XposedHelpers.getBooleanField(topInfo, "vvv");
                            View view = (View) param.getResult();
//                            if (isTop) {
//                                view.setBackground(getGreyRippleDrawable(view.getContext()));
//                            } else {
                            view.setBackground(getRippleDrawable(view.getContext()));
//                            }
                        }
                    });
        }
    }
}
