package com.blanke.mdwechat.config;

import android.graphics.Color;

import com.blanke.mdwechat.WeChatHelper;

/**
 * Created by blanke on 2017/8/25.
 */

public class HookConfig {
    private static final String KEY_COLORPRIMARY = "colorPrimary";
    private static final String KEY_HOOKSWITCH = "hookSwitch";
    private static final String KEY_hook_actionbar = "key_hook_actionbar";
    private static final String KEY_hook_avatar = "key_hook_avatar";
    private static final String KEY_hook_ripple = "key_hook_ripple";
    private static final String KEY_hook_float_button = "key_hook_float_button";
    private static final String KEY_hook_search = "key_hook_search";
    private static final String KEY_hook_tab = "key_hook_tab";
    private static final String KEY_hook_menu_game = "key_hook_menu_game";
    private static final String KEY_hook_menu_shop = "key_hook_menu_shop";
    private static final String KEY_hook_menu_qrcode = "key_hook_menu_qrcode";
    private static final String KEY_hook_menu_shake = "key_hook_menu_shake";
    private static final String KEY_hook_menu_near = "key_hook_menu_near";
    private static final String KEY_IS_PLAY = "key_is_play";
    private static final String KEY_BUBBLE_TINT = "key_hook_bubble_tint";

    public static int getColorPrimary() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getInt(KEY_COLORPRIMARY, Color.parseColor("#009688"));
    }

    public static boolean isHookswitch() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_HOOKSWITCH, true);
    }

    public static boolean isPlay() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_IS_PLAY, false);
    }

    public static boolean isHookactionbar() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_actionbar, true);
    }

    public static boolean isHookavatar() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_avatar, true);
    }

    public static boolean isHookripple() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_ripple, true);
    }

    public static boolean isHookfloat_button() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_float_button, true);
    }

    public static boolean isHooksearch() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_search, true);
    }

    public static boolean isHooktab() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_tab, true);
    }

    public static boolean isHookmenu_game() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_game, true);
    }

    public static boolean isHookmenu_shop() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_shop, true);
    }

    public static boolean isHookmenu_qrcode() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_qrcode, true);
    }

    public static boolean isHookmenu_shake() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_shake, true);
    }

    public static boolean isHookmenu_near() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_near, true);
    }

    public static boolean isHookBubbleTint() {
        WeChatHelper.XMOD_PREFS.reload();
        return WeChatHelper.XMOD_PREFS.getBoolean(KEY_BUBBLE_TINT, false);
    }
}
