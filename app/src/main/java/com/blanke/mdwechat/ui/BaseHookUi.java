package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blanke.mdwechat.Common;
import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.Common.MY_APPLICATION_PACKAGE;
import static com.blanke.mdwechat.WeChatHelper.MD_CONTEXT;

/**
 * Created by blanke on 2017/8/1.
 */

public abstract class BaseHookUi {
    private final String TAG = getClass().getSimpleName();

    public abstract void hook(XC_LoadPackage.LoadPackageParam lpparam);

    //刷新设置
    protected void refreshPrefs() {
        WeChatHelper.XMOD_PREFS.reload();
    }

    protected int getId(Context context, String idName) {
        return context.getResources().getIdentifier(context.getPackageName() + ":id/" + idName, null, null);
    }

    protected int getColorId(Context context, String idName) {
        return context.getResources().getIdentifier(context.getPackageName() + ":color/" + idName, null, null);
    }

    protected int getColorId(Resources resources, String idName) {
        return resources.getIdentifier(Common.WECHAT_PACKAGENAME + ":color/" + idName, null, null);
    }

    protected int getDrawableIdByName(String name) {
        return getResourceIdByName("drawable", name);
    }

    protected int getResourceIdByName(String resourceName, String name) {
        return MD_CONTEXT.getResources().getIdentifier(name, resourceName, MY_APPLICATION_PACKAGE);
    }

    /**
     * 打印调试 view tree
     *
     * @param rootView
     * @param level
     */
    protected void printViewTree(View rootView, int level) {
        logSpace(level, getViewMsg(rootView));
        if (rootView instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) rootView;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                printViewTree(v, level + 1);
            }
        }
    }

    protected void printActivityViewTree(Activity activity) {
        View contentView = activity.findViewById(android.R.id.content);
        printViewTree(contentView, 0);
    }

    protected void printActivityWindowViewTree(Activity activity) {
        printViewTree(activity.getWindow().getDecorView(), 0);
    }

    private String getViewMsg(View view) {
        String className = view.getClass().getName();
        int id = view.getId();
        String text = "";
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            text = textView.getText().toString();
            text += "(" + textView.getHint() + ")";
        }
        return className + "," + id + "," + text + "," + view;
    }

    private void logSpace(int count, String msg) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < count; j++) {
            sb.append("----");
        }
        sb.append(msg);
        log(sb.toString());
    }

    protected void log(String msg) {
        XposedBridge.log(TAG + ":" + msg);
    }

    protected void log(Throwable e) {
        XposedBridge.log(e);
    }

    protected void logSuperClass(Class clazz) {
        if (clazz == Object.class) {
            return;
        }
        log(clazz.getName());
        logSuperClass(clazz.getSuperclass());
    }
}
