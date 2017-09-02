package com.blanke.mdwechat.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blanke.mdwechat.Common;
import com.blanke.mdwechat.config.HookConfig;
import com.blanke.mdwechat.util.DrawableUtils;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by blanke on 2017/8/1.
 */

public class ContactHook extends BaseHookUi {
    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        if (HookConfig.isHookripple()) {
            xMethod(wxConfig.classes.ContactFragment,
                    wxConfig.methods.MainFragment_onTabCreate,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ListView listView = (ListView) getObjectField(param.thisObject, wxConfig.fields.ContactFragment_mListView);
                            if (listView.getHeaderViewsCount() > 0) {
                                ArrayList<ListView.FixedViewInfo> mHeaderViewInfos = (ArrayList<ListView.FixedViewInfo>) getObjectField(listView, "mHeaderViewInfos");
                                View header = mHeaderViewInfos.get(0).view;
//                            log("header=" + header);
                                if (header != null) {
//                                printViewTree(header, 0);
                                    ViewGroup headLayout = (ViewGroup) header;
                                    for (int i = 0; i < headLayout.getChildCount(); i++) {
                                        ViewGroup item = (ViewGroup) headLayout.getChildAt(i);
                                        ViewGroup itemContent = (ViewGroup) item.getChildAt(0);
                                        if (itemContent != null) {
                                            itemContent.setBackground(getRippleDrawable(headLayout.getContext()));
                                            itemContent.getChildAt(0).setBackground(getTransparentDrawable());
                                        }
                                    }
                                }
                            }
                            final Bitmap background = DrawableUtils.getExternalStorageAppBitmap(Common.CONTACT_BACKGROUND_FILENAME);
                            if (background != null) {
                                listView.setBackground(new BitmapDrawable(background));
                            }
                        }
                    });
            xMethod(wxConfig.classes.ContactAdapter,
                    "getView", int.class, View.class, ViewGroup.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ViewGroup view = (ViewGroup) param.getResult();
//                        printViewTree(view, 0);
                            if (view != null) {
                                ViewGroup itemContent = (ViewGroup) view.getChildAt(1);
                                if (itemContent != null) {
                                    itemContent.setBackground(getTransparentDrawable());
                                    View item = itemContent.getChildAt(0);
                                    if (item != null) {
                                        item.setBackground(getTransparentDrawable());
                                    }
                                }
                                view.setBackground(getRippleDrawable(view.getContext()));
                            }
                        }
                    });
        }
    }
}
