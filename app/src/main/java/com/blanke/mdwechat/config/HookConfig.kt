package com.blanke.mdwechat.config

import android.graphics.Color

import com.blanke.mdwechat.WeChatHelper

/**
 * Created by blanke on 2017/8/25.
 */

object HookConfig {
    private val KEY_COLORPRIMARY = "colorPrimary"
    private val KEY_HOOKSWITCH = "hookSwitch"
    private val KEY_hook_actionbar = "key_hook_actionbar"
    private val KEY_hook_avatar = "key_hook_avatar"
    private val KEY_hook_ripple = "key_hook_ripple"
    private val KEY_hook_float_button = "key_hook_float_button"
    private val KEY_hook_search = "key_hook_search"
    private val KEY_hook_tab = "key_hook_tab"
    private val KEY_hook_menu_game = "key_hook_menu_game"
    private val KEY_hook_menu_shop = "key_hook_menu_shop"
    private val KEY_hook_menu_qrcode = "key_hook_menu_qrcode"
    private val KEY_hook_menu_shake = "key_hook_menu_shake"
    private val KEY_hook_menu_near = "key_hook_menu_near"
    private val KEY_IS_PLAY = "key_is_play"
    private val KEY_BUBBLE_TINT = "key_hook_bubble_tint"

    val colorPrimary: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(KEY_COLORPRIMARY, Color.parseColor("#009688"))
        }

    val isHookswitch: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_HOOKSWITCH, true)
        }

    val isPlay: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_IS_PLAY, false)
        }

    val isHookactionbar: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_actionbar, true)
        }

    val isHookavatar: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_avatar, true)
        }

    val isHookripple: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_ripple, true)
        }

    val isHookfloat_button: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_float_button, true)
        }

    val isHooksearch: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_search, true)
        }

    val isHooktab: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_tab, true)
        }

    val isHookmenu_game: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_game, true)
        }

    val isHookmenu_shop: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_shop, true)
        }

    val isHookmenu_qrcode: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_qrcode, true)
        }

    val isHookmenu_shake: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_shake, true)
        }

    val isHookmenu_near: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_hook_menu_near, true)
        }

    val isHookBubbleTint: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(KEY_BUBBLE_TINT, false)
        }
}
