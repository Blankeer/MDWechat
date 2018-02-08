package com.blanke.mdwechat.config

import android.graphics.Color

import com.blanke.mdwechat.WeChatHelper

/**
 * Created by blanke on 2017/8/25.
 */

object HookConfig {
    private val key_hook_switch = "hookSwitch"
    private val key_color_primary = "colorPrimary"
    private val key_hook_actionbar = "key_hook_actionbar"
    private val key_hook_avatar = "key_hook_avatar"
    private val key_hook_ripple = "key_hook_ripple"
    private val key_hook_float_button = "key_hook_float_button"
    private val key_hook_search = "key_hook_search"
    private val key_hook_tab = "key_hook_tab"
    private val key_hook_hide_wx_tab = "key_hook_hide_wx_tab"
    private val key_hook_hide_wx_tab_2 = "key_hook_hide_wx_tab_2"
    private val key_hook_hide_wx_tab_3 = "key_hook_hide_wx_tab_3"
    private val key_hook_tab_elevation = "key_hook_tab_elevation"
    private val key_hook_menu_game = "key_hook_menu_game"
    private val key_hook_menu_shop = "key_hook_menu_shop"
    private val key_is_play = "key_is_play"
    private val key_hook_bubble_tint = "key_hook_bubble_tint"
    private val key_hook_menu_qrcode = "key_hook_menu_qrcode"
    private val key_hook_menu_shake = "key_hook_menu_shake"
    private val key_hook_menu_near = "key_hook_menu_near"
    private val key_color_ripple = "key_color_ripple"
    private val key_hook_hide_actionbar = "key_hook_hide_actionbar"
    private val key_hook_float_button_move = "key_hook_float_button_move"
    private val key_color_conversation_top = "key_color_conversation_top"
    private val key_hook_remove_appbrand = "key_hook_remove_appbrand"
    private val key_hook_remove_foot_view = "key_hook_remove_foot_view"
    private val key_hook_menu_sns = "key_hook_menu_sns"
    private val key_hook_menu_appbrand = "key_hook_menu_appbrand"
    private val key_hook_bubble = "key_hook_bubble"
    private val key_hook_bubble_tint_left = "key_hook_bubble_tint_left"
    private val key_hook_bubble_tint_right = "key_hook_bubble_tint_right"
    private val key_hook_chat_text_color_left = "key_hook_chat_text_color_left"
    private val key_hook_chat_text_color_right = "key_hook_chat_text_color_right"
    private val key_hook_statusbar_transparent = "key_hook_statusbar_transparent"

    val is_hook_switch: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_switch, true)
        }
    val get_color_primary: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_color_primary, Color.BLACK)
        }
    val is_hook_actionbar: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_actionbar, true)
        }
    val is_hook_avatar: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_avatar, true)
        }
    val is_hook_ripple: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_ripple, true)
        }
    val is_hook_float_button: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_float_button, true)
        }
    val is_hook_search: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_search, true)
        }
    val is_hook_tab: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_tab, true)
        }
    val is_hook_tab_elevation: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_tab_elevation, true)
        }
    val is_hook_hide_wx_tab: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_hide_wx_tab, true)
        }
    val is_hook_hide_wx_tab_2: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_hide_wx_tab_2, false)
        }
    val is_hook_hide_wx_tab_3: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_hide_wx_tab_3, false)
        }
    val is_hook_menu_game: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_game, true)
        }
    val is_hook_menu_shop: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_shop, true)
        }
    val is_play: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_is_play, false)
        }
    val is_hook_bubble_tint: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_bubble_tint, false)
        }
    val is_hook_menu_qrcode: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_qrcode, true)
        }
    val is_hook_menu_shake: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_shake, true)
        }
    val is_hook_menu_near: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_near, true)
        }
    val get_color_ripple: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_color_ripple, Color.GRAY)
        }
    val is_hook_hide_actionbar: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_hide_actionbar, true)
        }
    val is_hook_float_button_move: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_float_button_move, true)
        }
    val get_color_conversation_top: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_color_conversation_top, Color.GRAY)
        }
    val is_hook_remove_appbrand: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_remove_appbrand, false)
        }
    val is_hook_remove_foot_view: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_remove_foot_view, true)
        }
    val is_hook_menu_sns: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_sns, false)
        }
    val is_hook_menu_appbrand: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_menu_appbrand, false)
        }
    val is_hook_bubble: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_bubble, true)
        }
    val is_hook_statusbar_transparent: Boolean
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getBoolean(key_hook_statusbar_transparent, false)
        }
    val get_hook_bubble_tint_left: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_hook_bubble_tint_left, Color.WHITE)
        }
    val get_hook_bubble_tint_right: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_hook_bubble_tint_right, Color.WHITE)
        }
    val get_hook_chat_text_color_left: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_hook_chat_text_color_left, Color.BLACK)
        }
    val get_hook_chat_text_color_right: Int
        get() {
            WeChatHelper.XMOD_PREFS.reload()
            return WeChatHelper.XMOD_PREFS.getInt(key_hook_chat_text_color_right, Color.BLACK)
        }
}
