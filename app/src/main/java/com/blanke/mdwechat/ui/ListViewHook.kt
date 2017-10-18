package com.blanke.mdwechat.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ListView
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.findAndHookConstructor
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class ListViewHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!HookConfig.isHookripple) {
            return
        }
        //去掉黄色的 selector
        findAndHookConstructor(C.ListView, C.Context, C.AttributeSet, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val listView = param!!.thisObject as ListView
                //                log("ListView=" + listView.getClass().getName());
                val transDrawable = ColorDrawable(Color.TRANSPARENT)
                listView.selector = transDrawable
                //                listView.setBackground(transDrawable);
            }
        })
        //        findAndHookMethod(AbsListView.class, "obtainView", int.class, boolean[].class
        //                , new XC_MethodHook() {
        //                    @Override
        //                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        //                        View view = (View) param.getResult();
        //                        Context context = view.getContext();
        //                        view.setBackground(getDefaultRippleDrawable(context));
        //                    }
        //                });
    }
}
