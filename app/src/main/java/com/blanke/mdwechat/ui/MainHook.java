package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blanke.mdwechat.R;
import com.blanke.mdwechat.WeChatHelper;
import com.blanke.mdwechat.util.ConvertUtils;
import com.blanke.mdwechat.widget.MaterialSearchView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.reflect.Constructor;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.MD_CONTEXT;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.LauncherUI;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.PopWindowAdapter_Bean_C;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.PopWindowAdapter_Bean_D;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.Search_FTSMainUI;
import static com.blanke.mdwechat.WeChatHelper.WCField.HomeUi_PopWindowAdapter;
import static com.blanke.mdwechat.WeChatHelper.WCField.HomeUi_PopWindowAdapter_SparseArray;
import static com.blanke.mdwechat.WeChatHelper.WCField.LauncherUI_mHomeUi;
import static com.blanke.mdwechat.WeChatHelper.WCId.ActionBar_Add_id;
import static com.blanke.mdwechat.WeChatHelper.WCId.ActionBar_id;
import static com.blanke.mdwechat.WeChatHelper.WCId.SearchUI_EditText_id;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.HomeUi_StartSearch;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

/**
 * Created by blanke on 2017/8/1.
 */

public class MainHook extends BaseHookUi {
    private static boolean isMainInit = false;//主界面初始化 hook 完毕
    private static AdapterView.OnItemClickListener hookPopWindowAdapter;
    private MaterialSearchView searchView;
    private Object mHomeUi;
    private String searchKey;

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
                /******************
                 *hook floatButton
                 *****************/
                //添加 floatbutton
                ViewGroup contentFrameLayout = (ViewGroup) linearLayoutContent.getParent();
                addFloatButton(contentFrameLayout);

                // hook floatbutton click
                mHomeUi = getObjectField(activity, LauncherUI_mHomeUi);
//                log("mHomeUI=" + mHomeUi);
                if (mHomeUi != null) {
                    Object popWindowAdapter = XposedHelpers.getObjectField(mHomeUi, HomeUi_PopWindowAdapter);
//                    log("popWindowAdapter=" + popWindowAdapter);
                    if (popWindowAdapter != null) {
                        hookPopWindowAdapter = (AdapterView.OnItemClickListener) popWindowAdapter;
                        SparseArray hookArrays = new SparseArray();
                        int[] mapping = {10, 20, 2, 1};
//                        log("mapping:" + Arrays.toString(mapping));
                        Constructor dConstructor = PopWindowAdapter_Bean_D.getConstructor(int.class, String.class, String.class, int.class, int.class);
                        Constructor cConstructor = PopWindowAdapter_Bean_C.getConstructor(PopWindowAdapter_Bean_D);
                        for (int i = 0; i < mapping.length; i++) {
                            Object d = dConstructor.newInstance(mapping[i], "", "", mapping[i], mapping[i]);
                            Object c = cConstructor.newInstance(d);
                            hookArrays.put(i, c);
                        }
//                        log("hookArrays=" + hookArrays);
                        XposedHelpers.setObjectField(popWindowAdapter, HomeUi_PopWindowAdapter_SparseArray, hookArrays);
                    }
                }
                /******************
                 *hook SearchView
                 *****************/
//                ViewGroup actionFrameLayout = (ViewGroup) contentFrameLayout.getParent();
//                ViewGroup rootFrameLayout = (ViewGroup) actionFrameLayout.getParent();
//                logSuperClass(rootFrameLayout.getClass());
                ViewGroup actionLayout = (ViewGroup) activity.findViewById(getId(activity, ActionBar_id));
                addSearchView(actionLayout);
                // hook click search
                findAndHookMethod(LauncherUI, "onOptionsItemSelected", MenuItem.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        MenuItem menuItem = (MenuItem) param.args[0];
                        if (menuItem.getItemId() == 1) {//search
//                            log("hook launcherUi click action search icon success");
                            if (searchView != null) {
                                searchView.show();
                            }
                            param.setResult(true);
                        }
                    }
                });


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
                    // TODO: 2017/8/16 bug,
                    View addParentView = (View) view.getParent();
                    if (addParentView != null) {
                        ViewGroup addParentParentView = (ViewGroup) addParentView.getParent();
                        if (addParentParentView != null) {
                            addParentParentView.removeView(addParentView);
                        }
                    }
                }
            }
        });
    }

    private void addFloatButton(ViewGroup frameLayout) {
        int primaryColor = getPrimaryColor();
        Context context = MD_CONTEXT;
        FloatingActionMenu actionMenu = new FloatingActionMenu(context);
        actionMenu.setMenuButtonColorNormal(primaryColor);
        actionMenu.setMenuButtonColorPressed(primaryColor);
        actionMenu.setMenuIcon(ContextCompat.getDrawable(context, R.drawable.ic_add));
        actionMenu.initMenuButton();

        actionMenu.setFloatButtonClickListener(new FloatingActionMenu.FloatButtonClickListener() {
            @Override
            public void onClick(FloatingActionButton fab, int index) {
                log("click fab,index=" + index + ",label" + fab.getLabelText());
                if (hookPopWindowAdapter != null) {
                    hookPopWindowAdapter.onItemClick(null, null, index, 0);
                }
            }
        });

        addFloatButton(actionMenu, context, "扫一扫", R.drawable.ic_scan, primaryColor);
        addFloatButton(actionMenu, context, "收付款", R.drawable.ic_money, primaryColor);
        addFloatButton(actionMenu, context, "群聊", R.drawable.ic_chat, primaryColor);
        addFloatButton(actionMenu, context, "添加好友", R.drawable.ic_friend_add, primaryColor);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ConvertUtils.dp2px(frameLayout.getContext(), 12);
        params.rightMargin = margin;
        params.bottomMargin = margin;
        params.gravity = Gravity.END | Gravity.BOTTOM;
        frameLayout.addView(actionMenu, params);
    }

    private FloatingActionButton addFloatButton(FloatingActionMenu actionMenu, Context context,
                                                String label, int drawable, int primaryColor) {
        FloatingActionButton fab = new FloatingActionButton(context);
        fab.setImageDrawable(ContextCompat.getDrawable(context, drawable));
        fab.setColorNormal(primaryColor);
        fab.setColorPressed(primaryColor);
        fab.setButtonSize(SIZE_MINI);
        fab.setLabelText(label);
        actionMenu.addMenuButton(fab);
        fab.setLabelColors(primaryColor, primaryColor, primaryColor);
        return fab;
    }

    private void addSearchView(ViewGroup frameLayout) {
        Context context = MD_CONTEXT;
        searchView = new MaterialSearchView(context);
        searchView.setOnSearchListener(new MaterialSearchView.SimpleonSearchListener() {
            @Override
            public void onSearch(String query) {
                searchKey = query;
                if (mHomeUi != null) {
                    XposedHelpers.callMethod(mHomeUi, HomeUi_StartSearch);
                }
                searchView.hide();
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(searchView, params);
        findAndHookMethod(Search_FTSMainUI, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!TextUtils.isEmpty(searchKey)) {
                    final Activity activity = (Activity) param.thisObject;
                    log("searchkey=" + searchKey);
                    final Handler handler = new Handler(activity.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int id = getId(activity, SearchUI_EditText_id);
                            View editView = activity.getWindow().findViewById(id);
                            log("id=" + id);
                            if (editView == null) {
//                                printActivityWindowViewTree(activity);
                                handler.postDelayed(this, 200);
                                return;
                            }
                            log("editview=" + editView);
                            if (editView instanceof EditText) {
                                EditText editText = (EditText) editView;
                                log("editText=" + editText);
                                editText.setText(searchKey);
                            }
                            searchKey = null;
                        }
                    }, 200);
                }
            }
        });
    }

    private int getPrimaryColor() {
        refreshPrefs();
        return WeChatHelper.colorPrimary;
    }
}
