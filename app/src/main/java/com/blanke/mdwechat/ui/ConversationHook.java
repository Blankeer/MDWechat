package com.blanke.mdwechat.ui;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.xMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class ConversationHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
//        xMethod(wxConfig.classes.ConversationFragment,
//                wxConfig.methods.MainFragment_onTabCreate,
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        View listView = (View) getObjectField(param.thisObject, wxConfig.fields.ConversationFragment_mListView);
////                        listView.setBackground(getBackground());
//                    }
//                });
        xMethod("com.tencent.mm.ui.conversation.g",
                "getView", int.class, View.class, ViewGroup.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View view = (View) param.getResult();
                        view.setBackground(getRippleDrawable(view.getContext()));
                    }
                });
    }

    private Drawable getBackground() {
        return getColorPrimaryDrawable();
    }
}
