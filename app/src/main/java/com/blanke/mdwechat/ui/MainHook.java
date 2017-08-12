package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blanke.mdwechat.WeChatHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCClasses.LauncherUI;
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
                log("LauncherUI startMainUI isMainInit=" + isMainInit);
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
                //移除 action
//                View actionBar = activity.findViewById(getId(activity, "fw"));
//                actionBar.setVisibility(View.GONE);

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
                        log("LauncherUI onDestroy()");
                        isMainInit = false;
                    }
                });
    }
}
