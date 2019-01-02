package com.xposed.hot;

import android.content.Context;
import android.util.Log;

import com.blanke.mdwechat.WechatHook;

import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class HotLoadPackageEntry {
    public static boolean entry(ClassLoader masterClassLoader, ClassLoader pluginClassLoader, Context context, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            new WechatHook().handleLoadPackage(loadPackageParam);
        } catch (Throwable throwable) {
            Log.e("xposed-hot", "xposed-hot", throwable);
        }
        return true;
    }
}
