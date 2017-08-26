package com.blanke.mdwechat.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blanke.mdwechat.R;
import com.blanke.mdwechat.util.ConvertUtils;
import com.blanke.mdwechat.widget.MaterialSearchView;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.MD_CONTEXT;
import static com.blanke.mdwechat.WeChatHelper.wxConfig;
import static com.blanke.mdwechat.WeChatHelper.xClass;
import static com.blanke.mdwechat.WeChatHelper.xMethod;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
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
    private FloatingActionMenu actionMenu;
    private Object mMainTabClickListener = null;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookWechatMain(lpparam);
    }

    private void hookWechatMain(final XC_LoadPackage.LoadPackageParam lpparam) {
        xMethod(wxConfig.classes.LauncherUI,
                wxConfig.methods.LauncherUI_startMainUI,
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        refreshPrefs();
                        if (isMainInit) {
                            return;
                        }
                        final Activity activity = (Activity) param.thisObject;

                        // print views
                        Handler handler = new Handler(activity.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                printActivityWindowViewTree(activity);
                            }
                        }, 30 * 1000);

                        View viewPager = findViewByIdName(activity, wxConfig.views.LauncherUI_MainViewPager);
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
                        mHomeUi = getObjectField(activity, wxConfig.fields.LauncherUI_mHomeUi);
                        if (mHomeUi != null) {
                            Object popWindowAdapter = XposedHelpers.getObjectField(mHomeUi, wxConfig.fields.HomeUI_mMenuAdapterManager);
                            if (popWindowAdapter != null) {
                                hookPopWindowAdapter = (AdapterView.OnItemClickListener) popWindowAdapter;
                                SparseArray hookArrays = new SparseArray();
                                int[] mapping = {10, 20, 2, 1};
//                              log("mapping:" + Arrays.toString(mapping));
                                Class mMenuItemViewHolder = xClass(wxConfig.classes.MenuItemViewHolder);
                                Class mMenuItemViewHolderWrapper = xClass(wxConfig.classes.MenuItemViewHolderWrapper);
                                Constructor dConstructor = mMenuItemViewHolder.getConstructor(int.class, String.class, String.class, int.class, int.class);
                                Constructor cConstructor = mMenuItemViewHolderWrapper.getConstructor(mMenuItemViewHolder);
                                for (int i = 0; i < mapping.length; i++) {
                                    Object d = dConstructor.newInstance(mapping[i], "", "", mapping[i], mapping[i]);
                                    Object c = cConstructor.newInstance(d);
                                    hookArrays.put(i, c);
                                }
//                              log("hookArrays=" + hookArrays);
                                XposedHelpers.setObjectField(popWindowAdapter, wxConfig.fields.MenuAdapterManager_mMenuArray, hookArrays);
                            }
                        }
                        /******************
                         *hook SearchView
                         *****************/
                        ViewGroup actionLayout = (ViewGroup) findViewByIdName(activity, wxConfig.views.ActionBarContainer);
                        addSearchView(actionLayout, lpparam);
                        // hook click search
                        xMethod(wxConfig.classes.LauncherUI,
                                "onOptionsItemSelected",
                                MenuItem.class,
                                new XC_MethodHook() {
                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        MenuItem menuItem = (MenuItem) param.args[0];
                                        if (menuItem.getItemId() == 1) {//search
//                                          log("hook launcherUi click action search icon success");
                                            if (searchView != null) {
                                                searchView.show();
                                            }
                                            param.setResult(true);
                                        }
                                    }
                                });
                        /******************
                         *hook tabLayout
                         *****************/
                        addTabLayout(linearLayoutContent, viewPager);

                        isMainInit = true;
                    }
                });
        xMethod(wxConfig.classes.LauncherUI,
                "onDestroy", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        log("LauncherUI onDestroy()");
                        isMainInit = false;
                    }
                });
        //hide more icon in actionBar
        xMethod(wxConfig.classes.LauncherUI, "onCreateOptionsMenu", Menu.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                log("homeUI=" + mHomeUi);
                if (mHomeUi != null) {
                    //hide actionbar more add icon
                    MenuItem moreMenuItem = (MenuItem) getObjectField(mHomeUi, wxConfig.fields.HomeUI_mMoreMenuItem);
//                    log("moreMenuItem=" + moreMenuItem);
                    moreMenuItem.setVisible(false);
                }
            }
        });
        //hook onKeyEvent
        xMethod(wxConfig.classes.LauncherUI, "dispatchKeyEvent", KeyEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                KeyEvent keyEvent = (KeyEvent) param.args[0];
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MENU) {//hide menu
                    param.setResult(true);
                    return;
                }
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {//hook back
                    if (searchView != null && searchView.isSearchViewVisible()) {
                        searchView.hide();
                        param.setResult(true);
                        return;
                    }
                    if (actionMenu != null && actionMenu.isOpened()) {
                        actionMenu.close(true);
                        param.setResult(true);
                        return;
                    }
                }
            }
        });


    }

    private void addFloatButton(ViewGroup frameLayout) {
        int primaryColor = getColorPrimary();
        Context context = MD_CONTEXT;
        actionMenu = new FloatingActionMenu(context);
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

    private void addSearchView(ViewGroup frameLayout, XC_LoadPackage.LoadPackageParam lpparam) {
        Context context = MD_CONTEXT;
        searchView = new MaterialSearchView(context);
        searchView.setOnSearchListener(new MaterialSearchView.SimpleonSearchListener() {
            @Override
            public void onSearch(String query) {
                searchKey = query;
                if (mHomeUi != null) {
                    XposedHelpers.callMethod(mHomeUi, wxConfig.methods.HomeUI_startSearch);
                }
                searchView.hide();
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(searchView, params);
        findAndHookConstructor(wxConfig.classes.ActionBarEditText,
                lpparam.classLoader, Context.class, AttributeSet.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (!TextUtils.isEmpty(searchKey)) {
                            final EditText editText = (EditText) param.thisObject;
                            editText.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    editText.setText(searchKey);
                                    searchKey = null;
                                }
                            }, 300);
                        }
                    }
                });
    }

    private void addTabLayout(ViewGroup viewPagerLinearLayout, final View pager) {
        Context context = MD_CONTEXT;
        final CommonTabLayout tabLayout = new CommonTabLayout(context);
        int primaryColor = getColorPrimary();
        tabLayout.setBackgroundColor(primaryColor);
        tabLayout.setTextSelectColor(Color.WHITE);
        tabLayout.setTextUnselectColor(0x1acccccc);
        int dp2 = ConvertUtils.dp2px(pager.getContext(), 1F);
        tabLayout.setIndicatorHeight(dp2);
        tabLayout.setIndicatorColor(Color.WHITE);
        tabLayout.setIndicatorCornerRadius(dp2);
        tabLayout.setIndicatorAnimDuration(200);
        tabLayout.setElevation(5);
        tabLayout.setTextsize(context.getResources().getDimension(R.dimen.tabTextSize));
        tabLayout.setUnreadBackground(Color.WHITE);
        tabLayout.setUnreadTextColor(primaryColor);
        tabLayout.setSelectIconColor(Color.WHITE);
        tabLayout.setUnSelectIconColor(0x1aaaaaaa);

//        String[] titles = {"消息", "通讯录", "朋友圈", "设置"};
        final int[] tabIcons = {R.drawable.ic_chat_tab, R.drawable.ic_contact_tab,
                R.drawable.ic_explore_tab, R.drawable.ic_person_tab};
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < tabIcons.length; i++) {
            final int finalI = i;
            mTabEntities.add(new CustomTabEntity() {
                @Override
                public String getTabTitle() {
                    return null;
                }

                @Override
                public int getTabSelectedIcon() {
                    return tabIcons[finalI];
                }

                @Override
                public int getTabUnselectedIcon() {
                    return 0;
                }
            });
        }
        tabLayout.setTabData(mTabEntities);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
//                log("tab click position=" + position);
                callMethod(pager, wxConfig.methods.WxViewPager_setCurrentItem, position);
            }

            @Override
            public void onTabReselect(int position) {
                if (mMainTabClickListener != null) {
                    log("call mMainTabClickListener onDoubleClick");
                    callMethod(mMainTabClickListener,
                            wxConfig.methods.MainTabClickListener_onDoubleClick,
                            position);
                }
            }
        });
        xMethod(wxConfig.classes.HomeUiViewPagerChangeListener,
                wxConfig.methods.HomeUiViewPagerChangeListener_onPageScrolled,
                int.class, float.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        float positionOffset = (float) param.args[1];
                        int position = (int) param.args[0];
                        tabLayout.setStartScrollPosition(position);
                        tabLayout.setIndicatorOffset(positionOffset);
                    }
                });
        xMethod(wxConfig.classes.HomeUiViewPagerChangeListener,
                wxConfig.methods.HomeUiViewPagerChangeListener_onPageSelected,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        int position = (int) param.args[0];
                        tabLayout.setCurrentTab(position);
                    }
                });

        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_setMainTabUnread,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        log("msg unread=" + param.args[0]);
                        tabLayout.showMsg(0, (Integer) param.args[0]);
                        if (mMainTabClickListener == null) {
                            mMainTabClickListener = getObjectField(param.thisObject,
                                    wxConfig.fields.LauncherUIBottomTabView_mTabClickListener);
                        }
                    }
                });
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_setContactTabUnread,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        log("contact unread=" + param.args[0]);
                        tabLayout.showMsg(1, (Integer) param.args[0]);
                    }
                });
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_setFriendTabUnread,
                int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        log("friend unread=" + param.args[0]);
                        tabLayout.showMsg(2, (Integer) param.args[0]);
                    }
                });
        xMethod(wxConfig.classes.LauncherUIBottomTabView,
                wxConfig.methods.LauncherUIBottomTabView_showFriendTabUnreadPoint,
                boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        boolean show = (boolean) param.args[0];
                        tabLayout.showMsg(2, show ? -1 : 0);
                    }
                });

        //settings unread ignore
//        xMethod(wxConfig.classes.LauncherUIBottomTabView,
//                "yD",
//                int.class, new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        log("yd unread=" + param.args[0]);
//                    }
//                });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = ConvertUtils.dp2px(viewPagerLinearLayout.getContext(), 48);
        viewPagerLinearLayout.addView(tabLayout, 0, params);
    }
}
