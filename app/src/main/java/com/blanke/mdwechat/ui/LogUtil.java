package com.blanke.mdwechat.ui;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by blanke on 2017/10/3.
 */

public class LogUtil {
    public static void log(String msg) {
        XposedBridge.log("MDWechat:\t" + msg + "\tts=" + System.currentTimeMillis());
    }

    public static void log(Throwable e) {
        XposedBridge.log(e);
    }
}
