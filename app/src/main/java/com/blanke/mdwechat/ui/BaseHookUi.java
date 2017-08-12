package com.blanke.mdwechat.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

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

    /**
     * 打印调试 view tree
     *
     * @param rootView
     * @param level
     */
    protected void printViewTree(View rootView, int level) {
        if (rootView instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) rootView;
            logSpace(level, rootView.getClass().getName());
            if (rootView.getClass().getName().contains("ConversationOverscrollListView")) {
                ListView listView = (ListView) rootView;
                log("listview id=" + listView.getId() + ",bld=" + getId(listView.getContext(), "bld"));
                ListAdapter adapter = listView.getAdapter();
                log("adapter=" + adapter.getClass().getName());
                if (adapter instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
                    ListAdapter realAdapter = headerViewListAdapter.getWrappedAdapter();
                    log("realAdapter=" + realAdapter.getClass().getName());
                }
                View view = listView.getChildAt(0);
                Drawable drawable = view.getBackground();
                if (drawable != null) {
                    log("listView.getChildAt(0).getBackground()!=null");
                }
                view.setBackground(new ColorDrawable(Color.TRANSPARENT));

                return;
            }
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                printViewTree(v, level + 1);
            }
        } else {
            logSpace(level, rootView.getClass().getName());
        }
    }

    private void logSpace(int count, String msg) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < count; j++) {
            sb.append("\t");
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
}
