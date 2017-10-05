package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blanke.mdwechat.WeChatHelper;
import com.blanke.mdwechat.config.HookConfig;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by blanke on 2017/8/1.
 */

public abstract class BaseHookUi {
    private final String TAG = getClass().getSimpleName();
    private Drawable rippleDrawable;

    public abstract void hook(XC_LoadPackage.LoadPackageParam lpparam);

    //刷新设置
    protected void refreshPrefs() {
        WeChatHelper.XMOD_PREFS.reload();
    }

    protected View findViewByIdName(Activity activity, String idName) {
        return activity.findViewById(getViewId(activity, idName));
    }

    protected View findViewByIdName(View view, String idName) {
        return view.findViewById(getViewId(view.getContext(), idName));
    }

    @Deprecated
    protected int getId(Context context, String idName) {
        return context.getResources().getIdentifier(context.getPackageName() + ":id/" + idName, null, null);
    }

    protected int getViewId(Context context, String name) {
        return getResourceIdByName(context, "id", name);
    }

    protected int getColorId(Context context, String idName) {
        return getResourceIdByName(context, "color", idName);
    }

    protected int getDrawableIdByName(Context context, String name) {
        return getResourceIdByName(context, "drawable", name);
    }

    protected int getResourceIdByName(Context context, String resourceName, String name) {
        return context.getResources().getIdentifier(
                context.getPackageName() + ":" + resourceName + "/" + name,
                null, null);
    }

    protected ColorDrawable getColorPrimaryDrawable() {
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(getColorPrimary());
        return colorDrawable;
    }

    protected int getColorPrimary() {
        return HookConfig.getColorPrimary();
    }

    protected Drawable getRippleDrawable(Context context) {
        if (rippleDrawable == null) {
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta = context.obtainStyledAttributes(attrs);
            rippleDrawable = ta.getDrawable(0);
            ta.recycle();
        } else {
            return rippleDrawable.getConstantState().newDrawable().mutate();
        }
        return rippleDrawable;
    }

//    protected Drawable getGreyRippleDrawable(Context context) {
//        return DrawableUtils.getPressedColorRippleDrawable(ContextCompat.getColor(context, R.color.colorBlueGrey)
//                , ContextCompat.getColor(context, R.color.colorGrey));
//    }

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

    protected void logViewTree(View view) {
        printViewTree(view, 0);
    }

    protected void printActivityViewTree(Activity activity) {
        View contentView = activity.findViewById(android.R.id.content);
        printViewTree(contentView, 0);
    }

    protected void printActivityWindowViewTree(Activity activity) {
        log("-------start----------");
        printViewTree(activity.getWindow().getDecorView(), 0);
        log("-------end----------");
    }

    protected Drawable getTransparentDrawable() {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    protected Drawable getWhiteDrawable() {
        return new ColorDrawable(Color.WHITE);
    }

    protected String getViewMsg(View view) {
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

    protected void log(View view) {
        log(getViewMsg(view));
    }

    protected void log(String msg) {
        LogUtil.log(TAG + ":" + msg);
    }

    protected void log(Throwable e) {
        LogUtil.log(e);
    }

    protected void logSuperClass(Class clazz) {
        if (clazz == Object.class) {
            return;
        }
        log(clazz.getName());
        logSuperClass(clazz.getSuperclass());
    }

    protected void logStackTraces() {
        logStackTraces(15);
    }

    protected void logStackTraces(int methodCount) {
        logStackTraces(methodCount, 3);
    }

    protected void logStackTraces(int methodCount, int methodOffset) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String level = "";
        log("---------logStackTraces start----------");
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + methodOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].getClassName())
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            log(builder.toString());
        }
        log("---------logStackTraces end----------");
    }
}
