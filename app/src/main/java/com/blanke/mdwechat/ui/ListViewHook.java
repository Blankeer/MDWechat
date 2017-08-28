package com.blanke.mdwechat.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;

/**
 * Created by blanke on 2017/8/1.
 */

public class ListViewHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        //去掉黄色的 selector
        findAndHookConstructor(ListView.class, Context.class, AttributeSet.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ListView listView = (ListView) param.thisObject;
//                log("ListView=" + listView.getClass().getName());
                ColorDrawable transDrawable = new ColorDrawable(Color.TRANSPARENT);
                listView.setSelector(transDrawable);
//                listView.setBackground(transDrawable);
            }
        });
//        findAndHookMethod(AbsListView.class, "obtainView", int.class, boolean[].class
//                , new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        View view = (View) param.getResult();
//                        Context context = view.getContext();
//                        view.setBackground(getRippleDrawable(context));
//                    }
//                });
    }
}
