package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blanke.mdwechat.R;
import com.blanke.mdwechat.WeChatHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.MD_CONTEXT;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.LauncherUI;
import static com.blanke.mdwechat.WeChatHelper.WCId.ActionBar_Add_id;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class MainHook extends BaseHookUi {
    private static boolean isMainInit = false;//主界面初始化 hook 完毕

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookWechatMain();
    }

    private void hookWechatMain() {
        findAndHookMethod(LauncherUI, WeChatHelper.WCMethods.startMainUI, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                log("LauncherUI startMainUI isMainInit=" + isMainInit);
                if (isMainInit) {
                    return;
                }
                final Activity activity = (Activity) param.thisObject;

                View viewPager = activity.findViewById(
                        getId(activity, WeChatHelper.WCId.LauncherUI_ViewPager_Id));
                ViewGroup linearLayoutContent = (ViewGroup) viewPager.getParent();

                //移除底部 tabview
                View tabView = linearLayoutContent.getChildAt(1);
                if (tabView != null && tabView instanceof RelativeLayout) {
                    linearLayoutContent.removeView(tabView);
                }

                ViewGroup contentFrameLayout = (ViewGroup) linearLayoutContent.getParent();
                addFloatButton(contentFrameLayout);

//                TextView tv2 = new TextView(activity);
//                tv2.setText(myContexxt.getString(R.string.test_string2));
//
//                linearLayoutContent.addView(tv2, 0);
//
//                ImageView iv = new ImageView(activity);
//                Drawable drawable = ContextCompat.getDrawable(myContexxt, R.drawable.ic_book_blue_800_24dp);
//                iv.setImageDrawable(drawable);
//                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(100, 100);
//                linearLayoutContent.addView(iv, 1, params2);

                isMainInit = true;
            }
        });
        findAndHookMethod(LauncherUI,
                "onDestroy", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
//                        log("LauncherUI onDestroy()");
                        isMainInit = false;
                    }
                });
        //remove add more view
        findAndHookMethod(View.class, "onAttachedToWindow", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View view = (View) param.thisObject;
                if (view instanceof ImageView
                        && view.getId() == getId(view.getContext(), ActionBar_Add_id)) {
//                    log("view ActionBar_Add hook success");
                    View addParentView = (View) view.getParent();
                    ViewGroup addParentParentView = (ViewGroup) addParentView.getParent();
                    addParentParentView.removeView(addParentView);
                }
            }
        });
    }

    private void addFloatButton(ViewGroup frameLayout) {
        int primaryColor = getPrimaryColor();
        Context context = MD_CONTEXT;
        FloatingActionMenu actionMenu = new FloatingActionMenu(context);
        actionMenu.setMenuButtonColorNormal(primaryColor);
        actionMenu.initMenuButton();

        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_accessible_black_24dp));
        fab.setColorNormal(primaryColor);
        fab.setButtonSize(SIZE_MINI);
        fab.setLabelText("扫一扫");
        actionMenu.addMenuButton(fab);
        fab.setLabelColors(primaryColor, primaryColor, primaryColor);

        FloatingActionButton fab2 = new FloatingActionButton(context);
        fab2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_3d_rotation_red_800_24dp));
        fab2.setColorNormal(primaryColor);
        fab2.setButtonSize(SIZE_MINI);
        fab2.setLabelText("好友");
        actionMenu.addMenuButton(fab2);
        fab2.setLabelColors(primaryColor, primaryColor, primaryColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 8;
        params.bottomMargin = 8;
        params.gravity = Gravity.END | Gravity.BOTTOM;
        frameLayout.addView(actionMenu, params);
    }

    private int getPrimaryColor() {
        refreshPrefs();
        return WeChatHelper.colorPrimary;
    }
}
