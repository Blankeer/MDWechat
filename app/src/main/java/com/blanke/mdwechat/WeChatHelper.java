package com.blanke.mdwechat;

import android.app.Application;
import android.content.Context;

import com.blanke.mdwechat.config.HookConfig;
import com.blanke.mdwechat.config.WxVersionConfig;
import com.blanke.mdwechat.ui.ActionBarHook;
import com.blanke.mdwechat.ui.AvatarHook;
import com.blanke.mdwechat.ui.BaseHookUi;
import com.blanke.mdwechat.ui.ContactHook;
import com.blanke.mdwechat.ui.ConversationHook;
import com.blanke.mdwechat.ui.ListViewHook;
import com.blanke.mdwechat.ui.MainHook;
import com.blanke.mdwechat.ui.UnreadViewHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.Common.MY_APPLICATION_PACKAGE;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by blanke on 2017/7/29.
 */

public class WeChatHelper {
    private static XC_LoadPackage.LoadPackageParam loadPackageParam;
    public static XSharedPreferences XMOD_PREFS;
    public static Context MD_CONTEXT;
    private static List<BaseHookUi> hookUis;
    public static WxVersionConfig wxConfig;

    public static void init(String ver, XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        initApplication(ver, lpparam);
        loadPackageParam = lpparam;
//        versionNumber = versionNumber;
//        WCVersion.version = ver;
    }

    private static void initApplication(final String ver, final XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Application application = (Application) param.thisObject;
                MD_CONTEXT = application.createPackageContext(MY_APPLICATION_PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
                try {
                    wxConfig = WxVersionConfig.loadConfig(MD_CONTEXT, ver);
                } catch (Exception e) {
                    log("不支持的版本:" + ver);
                    return;
                }
                HookConfig.load(XMOD_PREFS);
                if (HookConfig.hookSwitch) {
                    initHookUis();
                    executeHookUi();
                }
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
        hookUis.add(new ContactHook());
        hookUis.add(new UnreadViewHook());
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

    public static XC_MethodHook.Unhook xMethod(String className, String methodName, Object... parameterTypesAndCallback) {
        return findAndHookMethod(className, loadPackageParam.classLoader, methodName, parameterTypesAndCallback);
    }

    public static Class<?> xClass(String className) {
        return findClass(className, loadPackageParam.classLoader);
    }
}
