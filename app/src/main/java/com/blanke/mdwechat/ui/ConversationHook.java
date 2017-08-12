package com.blanke.mdwechat.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class ConversationHook extends BaseHookUi{
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod(View.class,
                "setBackground", Drawable.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        View view = (View) param.thisObject;
                        if (view.getId() ==
                                getId(view.getContext(), WeChatHelper.WCId.Conversation_ListView_Item_Id)) {
                            log("hook view id= aie setBackground");
                            Drawable drawable = new ColorDrawable(Color.WHITE);
                            param.args[0] = drawable;
                        }
                    }
                });
    }
}
