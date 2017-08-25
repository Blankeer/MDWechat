package com.blanke.mdwechat.config;

import android.graphics.Color;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by blanke on 2017/8/25.
 */

public class HookConfig {
    private static final String KEY_COLORPRIMARY = "colorPrimary";
    private static final String KEY_HOOKSWITCH = "hookSwitch";
    public static int colorPrimary;
    public static boolean hookSwitch;


    public static void load(XSharedPreferences sharedPreferences) {
        colorPrimary = sharedPreferences.getInt(KEY_COLORPRIMARY, Color.parseColor("#009688"));
        hookSwitch = sharedPreferences.getBoolean(KEY_HOOKSWITCH, true);
    }
}
