package com.blanke.mdwechat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.blanke.mdwechat.ui.ActionBarHook;
import com.blanke.mdwechat.ui.AvatarHook;
import com.blanke.mdwechat.ui.BaseHookUi;
import com.blanke.mdwechat.ui.ConversationHook;
import com.blanke.mdwechat.ui.ListViewHook;
import com.blanke.mdwechat.ui.MainHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.Common.MY_APPLICATION_PACKAGE;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.LauncherUI;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by blanke on 2017/7/29.
 */

public class WeChatHelper {
    private static XC_LoadPackage.LoadPackageParam loadPackageParam;
    public static XSharedPreferences XMOD_PREFS;

    public static int colorPrimary;

    public static Context MD_CONTEXT;
    public static Activity WECHAT_LAUNCHER;
    private static List<BaseHookUi> hookUis;


    public static boolean init(String ver, XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        int versionNumber = getVersionNumber(ver);
        boolean re = versionNumber >= 0;
        if (!re) {
            return false;
        }
        loadPackageParam = lpparam;
        WCVersion.versionNumber = versionNumber;
        WCVersion.version = ver;
        colorPrimary = Color.parseColor("#009688");
        initHookUis();
        initApplication(lpparam);
        return true;
    }

    private static void initApplication(final XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                WCClasses.init(lpparam);
                WCMethods.init();
                WCId.init();
                WCDrawable.init();
                WCField.init();
                findAndHookMethod(LauncherUI,
                        "onCreate", Bundle.class,
                        new XC_MethodHook() {
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                WECHAT_LAUNCHER = (Activity) param.thisObject;
                                MD_CONTEXT = WECHAT_LAUNCHER.createPackageContext(MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
                                executeHookUi();
                            }
                        });
            }
        });
    }

    private static void initHookUis() {
        hookUis = new ArrayList<>();
        hookUis.add(new MainHook());
        hookUis.add(new ListViewHook());
        hookUis.add(new ActionBarHook());
        hookUis.add(new ConversationHook());
        hookUis.add(new AvatarHook());
    }

    private static void executeHookUi() {
        for (BaseHookUi hookUi : hookUis) {
            try {
                hookUi.hook(loadPackageParam);
            } catch (Exception e) {
                log(hookUi.getClass().getSimpleName() + " hook fail,msg=" + e.getMessage());
                log(e);
            }
        }
    }

    public static void initPrefs() {
        XMOD_PREFS = new XSharedPreferences(MY_APPLICATION_PACKAGE, Common.MOD_PREFS);
        XMOD_PREFS.makeWorldReadable();
    }

    public static class WCVersion {
        public static String version;

        public static int versionNumber;

    }


    private static int getVersionNumber(String version) {
        if (version.contains("6.5.7")) {
            return 0;
        } else if (version.contains("6.5.10")) {
            return 1;
        }
        return -1;
    }

    public static class WCClasses {
        public static Class LauncherUI;//主界面 activity
        public static Class MMFragmentActivity;//大部分 activity 的基类
        public static final String LauncherUIName = "com.tencent.mm.ui.LauncherUI";
        private static String MM_UI_PACKAGENAME = "com.tencent.mm.ui.";
        public static String MM_PLUGINSDK_UI_PACKNAME = "com.tencent.mm.pluginsdk.ui.";
        private static String MM_MODEL_PACKAGENAME = "com.tencent.mm.model.";
        private static final String AddressViewName = MM_UI_PACKAGENAME + "AddressView";//通讯录条目
        public static Class AddressView;
        private static String AvatarDrawableName;
        private static final String AvatarDrawableNames[] = {"", "j"};
        public static Class AvatarDrawable;
        public static Class AvatarUtil;
        public static String AvatarUtilName;
        public static String AvatarUtilNames[] = {"", "com.tencent.mm.v.b"};
        public static Class TouchImageView;
        public static Class ChattingUInonActivity;//聊天
        public static String ChattingUInonActivityName;
        public static String ChattingUInonActivityNames[] = {"", MM_UI_PACKAGENAME + "chatting.En_5b8fbb1e"};
        public static Class ChattingUInonFragment;//聊天fragment
        public static Class WebViewUI;
        public static String HomeUI_Name = MM_UI_PACKAGENAME + "HomeUI";
        public static Class HomeUI;
        public static Class PopWindowAdapter_Bean_C;
        public static String PopWindowAdapter_Bean_C_Name;
        public static String PopWindowAdapter_Bean_C_Names[] = {"", "c"};
        public static Class PopWindowAdapter_Bean_D;
        public static String PopWindowAdapter_Bean_D_Name;
        public static String PopWindowAdapter_Bean_D_Names[] = {"", "d"};
        public static Class Search_FTSMainUI;
        public static Class ViewPager_SimpleOnPageChangeListener;
        public static String ViewPager_SimpleOnPageChangeListener_Name;
        public static String ViewPager_SimpleOnPageChangeListener_Names[] = {"", "android.support.v4.view.ViewPager$h"};
        public static Class HomeUI_ViewPagerChangeListener;
        public static String HomeUI_ViewPagerChangeListener_Name;
        public static String HomeUI_ViewPagerChangeListener_Names[] = {"", HomeUI_Name + "$c"};
        public static Class LauncherUIBottomTabView;
        public static String ConversationOverscrollListView_Name = "ConversationOverscrollListView";
        public static Class ConversationOverscrollListView;


        private static void init(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            int index = WCVersion.versionNumber;
            LauncherUI = XposedHelpers.findClass(LauncherUIName, lpparam.classLoader);
            MMFragmentActivity = XposedHelpers.findClass(MM_UI_PACKAGENAME + "MMFragmentActivity", lpparam.classLoader);
            AddressView = XposedHelpers.findClass(AddressViewName, lpparam.classLoader);
            AvatarDrawableName = MM_PLUGINSDK_UI_PACKNAME + AvatarDrawableNames[index];
            AvatarDrawable = XposedHelpers.findClass(AvatarDrawableName, lpparam.classLoader);
            AvatarUtilName = AvatarUtilNames[index];
            AvatarUtil = XposedHelpers.findClass(AvatarUtilName, lpparam.classLoader);
            TouchImageView = XposedHelpers.findClass("com.tencent.mm.plugin.sns.ui.TouchImageView", lpparam.classLoader);
            ChattingUInonActivityName = ChattingUInonActivityNames[index];
            ChattingUInonActivity = XposedHelpers.findClass(ChattingUInonActivityName, lpparam.classLoader);
            ChattingUInonFragment = XposedHelpers.findClass(ChattingUInonActivityName + "$a", lpparam.classLoader);
            WebViewUI = XposedHelpers.findClass("com.tencent.mm.plugin.webview.ui.tools.WebViewUI", lpparam.classLoader);
            HomeUI = XposedHelpers.findClass(HomeUI_Name, lpparam.classLoader);
            PopWindowAdapter_Bean_C_Name = MM_UI_PACKAGENAME + "u$" + PopWindowAdapter_Bean_C_Names[index];
            PopWindowAdapter_Bean_C = XposedHelpers.findClass(PopWindowAdapter_Bean_C_Name, lpparam.classLoader);
            PopWindowAdapter_Bean_D_Name = MM_UI_PACKAGENAME + "u$" + PopWindowAdapter_Bean_D_Names[index];
            PopWindowAdapter_Bean_D = XposedHelpers.findClass(PopWindowAdapter_Bean_D_Name, lpparam.classLoader);
            Search_FTSMainUI = XposedHelpers.findClass("com.tencent.mm.plugin.search.ui.FTSMainUI", lpparam.classLoader);
            ViewPager_SimpleOnPageChangeListener_Name = ViewPager_SimpleOnPageChangeListener_Names[index];
            ViewPager_SimpleOnPageChangeListener = findClass(ViewPager_SimpleOnPageChangeListener_Name, lpparam.classLoader);
            HomeUI_ViewPagerChangeListener_Name = HomeUI_ViewPagerChangeListener_Names[index];
            HomeUI_ViewPagerChangeListener = findClass(HomeUI_ViewPagerChangeListener_Name, lpparam.classLoader);
            LauncherUIBottomTabView = findClass(MM_UI_PACKAGENAME + "LauncherUIBottomTabView", lpparam.classLoader);
            ConversationOverscrollListView = findClass(MM_UI_PACKAGENAME + "conversation." + ConversationOverscrollListView_Name, lpparam.classLoader);

        }
    }

    public static class WCMethods {
        private static String[] startMainUINames = {"bFE", "bNP"};
        public static String startMainUI;
        public static String getAvatarBitmap;
        public static String getAvatarBitmaps[] = {"", "a"};
        public static String getActionBarActivity;
        public static String getActionBarActivitys[] = {"", "cO"};
        public static String getActionBar;
        public static String getActionBars[] = {"", "cP"};
        public static String HomeUi_StartSearch;
        public static String HomeUi_StartSearchs[] = {"", "bNw"};
        public static String WxViewPager_setCurrentItem;
        public static String WxViewPager_setCurrentItems[] = {"", "Y"};
        public static String WxViewPager_onPageScrolled;
        public static String WxViewPager_onPageScrolleds[] = {"", "a"};
        public static String WxViewPager_onPageSelected;
        public static String WxViewPager_onPageSelecteds[] = {"", "V"};
        public static String LauncherUiTabView_setMainTabUnread;
        public static String LauncherUiTabView_setMainTabUnreads[] = {"", "yA"};
        public static String LauncherUiTabView_setContactTabUnread;
        public static String LauncherUiTabView_setContactTabUnreads[] = {"", "yB"};
        public static String LauncherUiTabView_setFriendTabUnread;
        public static String LauncherUiTabView_setFriendTabUnreads[] = {"", "yC"};

        private static void init() throws Throwable {
            int index = WCVersion.versionNumber;
            startMainUI = startMainUINames[index];
            getAvatarBitmap = getAvatarBitmaps[index];
            getActionBarActivity = getActionBarActivitys[index];
            getActionBar = getActionBars[index];
            HomeUi_StartSearch = HomeUi_StartSearchs[index];
            WxViewPager_setCurrentItem = WxViewPager_setCurrentItems[index];
            WxViewPager_onPageScrolled = WxViewPager_onPageScrolleds[index];
            WxViewPager_onPageSelected = WxViewPager_onPageSelecteds[index];
            LauncherUiTabView_setMainTabUnread = LauncherUiTabView_setMainTabUnreads[index];
            LauncherUiTabView_setContactTabUnread = LauncherUiTabView_setContactTabUnreads[index];
            LauncherUiTabView_setFriendTabUnread = LauncherUiTabView_setFriendTabUnreads[index];
        }
    }

    public static class WCId {
        private static String LauncherUI_ViewPager_Ids[] = {"blx", "ath"};
        public static String LauncherUI_ViewPager_Id;
        public static String Conversation_ListView_Item_Id;
        private static String Conversation_ListView_Item_Ids[] = {"", "aie"};
        public static String Conversation_ListView_Item_Avatar_Id;
        public static String Conversation_ListView_Item_Avatar_Ids[] = {"", "j9"};
        public static String Firends_ListView_Item_Avatar_Id;
        public static String Firends_ListView_Item_Avatar_Ids[] = {"", "cr3"};
        public static String Message_ListView_Item_Avator_Id;
        public static String Message_ListView_Item_Avator_Ids[] = {"", "ih"};
        public static String Discover_Avatar_Id;
        public static String Discover_Avatar_Ids[] = {"", "bx8"};
        public static String ActionBar_id;
        public static String ActionBar_ids[] = {"", "g7"};
        public static String WebViewUI_ActionBar_id;
        public static String WebViewUI_ActionBar_ids[] = {"", "g7"};
        public static String ActionBar_Divider_id;
        public static String ActionBar_Divider_ids[] = {"", "gm"};
        public static String ActionBar_Add_id;
        public static String ActionBar_Add_ids[] = {"", "fk"};
        public static String SearchUI_EditText_id;
        public static String SearchUI_EditText_ids[] = {"", "h2"};
        public static int ic_add;


        private static void init() {
            int index = WCVersion.versionNumber;
            LauncherUI_ViewPager_Id = LauncherUI_ViewPager_Ids[index];
            Conversation_ListView_Item_Id = Conversation_ListView_Item_Ids[index];
            Conversation_ListView_Item_Avatar_Id = Conversation_ListView_Item_Avatar_Ids[index];
            Firends_ListView_Item_Avatar_Id = Firends_ListView_Item_Avatar_Ids[index];
            Message_ListView_Item_Avator_Id = Message_ListView_Item_Avator_Ids[index];
            Discover_Avatar_Id = Discover_Avatar_Ids[index];
            ActionBar_id = ActionBar_ids[index];
            ActionBar_Divider_id = ActionBar_Divider_ids[index];
            ActionBar_Add_id = ActionBar_Add_ids[index];
            SearchUI_EditText_id = SearchUI_EditText_ids[index];
        }
    }

    public static class WCDrawable {
        public static String Conference_ListView_Item_Background;
        private static String[] Conference_ListView_Item_Backgrounds = {"ec", "ef"};

        private static void init() {
            Conference_ListView_Item_Background = Conference_ListView_Item_Backgrounds[WCVersion.versionNumber];
        }
    }

    public static class WCField {
        public static String ActionBarContainer_mBackground;
        public static String ActionBarContainer_mBackgrounds[] = {"", "Qd"};
        public static String LauncherUI_mHomeUi;
        public static String LauncherUI_mHomeUis[] = {"", "uyW"};
        public static String HomeUi_PopWindowAdapter;
        public static String HomeUi_PopWindowAdapters[] = {"", "uxq"};
        public static String HomeUi_PopWindowAdapter_SparseArray;
        public static String HomeUi_PopWindowAdapter_SparseArrays[] = {"", "uCY"};
        public static String HomeUi_PopWindowAdapter_Mapping;
        public static String HomeUi_PopWindowAdapter_Mappings[] = {"", "uDc"};

        private static void init() {
            int index = WCVersion.versionNumber;
            ActionBarContainer_mBackground = ActionBarContainer_mBackgrounds[index];
            LauncherUI_mHomeUi = LauncherUI_mHomeUis[index];
            HomeUi_PopWindowAdapter = HomeUi_PopWindowAdapters[index];
            HomeUi_PopWindowAdapter_SparseArray = HomeUi_PopWindowAdapter_SparseArrays[index];
            HomeUi_PopWindowAdapter_Mapping = HomeUi_PopWindowAdapter_Mappings[index];
        }
    }
}
