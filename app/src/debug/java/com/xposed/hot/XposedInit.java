package com.xposed.hot;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.blanke.mdwechat.BuildConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XCallback;

public class XposedInit implements IXposedHookLoadPackage {
    private final static String hotloadPluginEntry = "com.xposed.hot.HotLoadPackageEntry";

    private volatile boolean hooked = false;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (hooked) {
            return;
        }
        hooked = true;
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook(XCallback.PRIORITY_HIGHEST * 2) {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                hotLoadPlugin(lpparam.classLoader, (Context) param.args[0], lpparam);
            }
        });
    }

    private static ClassLoader replaceClassloader(Context context, XC_LoadPackage.LoadPackageParam lpparam) {
        ClassLoader classLoader = XposedInit.class.getClassLoader();
        if (!(classLoader instanceof PathClassLoader)) {
//            XposedBridge.log("classloader is not PathClassLoader: " + classLoader.toString());
            return classLoader;
        }

        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
//            XposedBridge.log("can not find packageManager");
            return classLoader;
        }

        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            //ignore
        }
        if (packageInfo == null) {
//            XposedBridge.log("can not find plugin install location for plugin: " + BuildConfig.APPLICATION_ID);
            return classLoader;
        }

        return createClassLoader(classLoader.getParent(), packageInfo);
    }

    @SuppressLint("PrivateApi")
    private void hotLoadPlugin(ClassLoader ownerClassLoader, Context context, XC_LoadPackage.LoadPackageParam lpparam) {
        ClassLoader hotClassLoader = replaceClassloader(context, lpparam);

        try {
            Class<?> aClass = hotClassLoader.loadClass(hotloadPluginEntry);
            Log.i("xposed", "invoke hot load entry");
            aClass.getMethod("entry", ClassLoader.class, ClassLoader.class, Context.class, XC_LoadPackage.LoadPackageParam.class)
                    .invoke(null, ownerClassLoader, hotClassLoader, context, lpparam);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    private static ConcurrentMap<String, PathClassLoader> classLoaderCache = new ConcurrentHashMap<>();

    private static PathClassLoader createClassLoader(ClassLoader parent, PackageInfo packageInfo) {
        if (classLoaderCache.containsKey(packageInfo.applicationInfo.sourceDir)) {
            return classLoaderCache.get(packageInfo.applicationInfo.sourceDir);
        }
        synchronized (XposedInit.class) {
            if (classLoaderCache.containsKey(packageInfo.applicationInfo.sourceDir)) {
                return classLoaderCache.get(packageInfo.applicationInfo.sourceDir);
            }
//            XposedBridge.log("create a new classloader for plugin with new apk path: " + packageInfo.applicationInfo.sourceDir);
            PathClassLoader hotClassLoader = new PathClassLoader(packageInfo.applicationInfo.sourceDir, parent);
            classLoaderCache.putIfAbsent(packageInfo.applicationInfo.sourceDir, hotClassLoader);
            return hotClassLoader;
        }
    }
}
