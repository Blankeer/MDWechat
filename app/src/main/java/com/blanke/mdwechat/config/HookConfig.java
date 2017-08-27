package com.blanke.mdwechat.config;

import android.graphics.Color;

import com.blanke.mdwechat.WeChatHelper;

/**
 * Created by blanke on 2017/8/25.
 */

public class HookConfig {
    private static final String KEY_COLORPRIMARY = "colorPrimary";
    private static final String KEY_HOOKSWITCH = "hookSwitch";

    public static int getColorPrimary() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getInt(KEY_COLORPRIMARY, Color.parseColor("#009688"));
    }

    public static boolean isHookswitch() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_HOOKSWITCH, true);
    }
}
