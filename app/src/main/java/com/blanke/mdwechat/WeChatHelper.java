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
import com.blanke.mdwechat.ui.MainHook;
import com.blanke.mdwechat.ui.StatusBarHook;

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
        WCClasses.init(lpparam);
        WCMethods.init();
        WCId.init();
        WCDrawable.init();
        colorPrimary = Color.parseColor("#ff009688");
        initHookUis();
        initApplication();
        return true;
    }

    private static void initApplication() {
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                findAndHookMethod(LauncherUI,
                        "onCreate", Bundle.class, new XC_MethodHook() {
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
        hookUis.add(new StatusBarHook());
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
        private static String MM_PLUGINSDK_UI_PACKNAME = "com.tencent.mm.pluginsdk.ui.";
        private static String MM_MODEL_PACKAGENAME = "com.tencent.mm.model.";


        private static void init(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
            LauncherUI = XposedHelpers.findClass(LauncherUIName, lpparam.classLoader);
            MMFragmentActivity = XposedHelpers.findClass(MM_UI_PACKAGENAME + "MMFragmentActivity", lpparam.classLoader);


        }
    }

    public static class WCMethods {
        private static String[] startMainUINames = {"bFE", "bNP"};
        public static String startMainUI;

        private static void init() throws Throwable {
            startMainUI = startMainUINames[WCVersion.versionNumber];
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

        private static void init() {
            int index = WCVersion.versionNumber;
            LauncherUI_ViewPager_Id = LauncherUI_ViewPager_Ids[index];
            Conversation_ListView_Item_Id = Conversation_ListView_Item_Ids[index];
            Conversation_ListView_Item_Avatar_Id = Conversation_ListView_Item_Avatar_Ids[index];
            Firends_ListView_Item_Avatar_Id = Firends_ListView_Item_Avatar_Ids[index];
        }
    }

    public static class WCDrawable {
        public static String Conference_ListView_Item_Background;
        private static String[] Conference_ListView_Item_Backgrounds = {"ec", "ef"};

        private static void init() {
            Conference_ListView_Item_Background = Conference_ListView_Item_Backgrounds[WCVersion.versionNumber];
        }
    }
}
