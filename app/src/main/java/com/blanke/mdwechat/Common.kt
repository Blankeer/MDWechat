package com.blanke.mdwechat

import android.os.Environment
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.VXPUtils
import java.io.File

/**
 * Created by blanke on 2017/7/29.
 */

object Common {
    val MY_APPLICATION_PACKAGE = "com.blanke.mdwechat"
    val WECHAT_PACKAGENAME = "com.tencent.mm"
    val MOD_PREFS = "md_wechat_settings"
    val APP_DIR = "mdwechat"
    val APP_VXP_DIR = "mdwechat_vxp_debug"
    val CONFIG_DIR = "config"
    val CONFIG_WECHAT_DIR = "config" + File.separator + "wechat"
    val CONFIG_VIEW_DIR = "config" + File.separator + "view"
    val LOGS_DIR = "logs"
    val ICON_DIR = "icon"
    val CONVERSATION_BACKGROUND_FILENAME = "conversation.png"
    val CONTACT_BACKGROUND_FILENAME = "contact.png"
    val DISCOVER_BACKGROUND_FILENAME = "discover.png"
    val SETTINGS_BACKGROUND_FILENAME = "settings.png"
    val CHAT_BUBBLE_LEFT_FILENAME = "bubble_left.9.png"
    val CHAT_BUBBLE_RIGHT_FILENAME = "bubble_right.9.png"
    val FILE_NAME_TAB_PREFIX = "tab_icon"
    val FILE_NAME_TAB_BG_PREFIX = "tab_bg"
    val FILE_NAME_FLOAT_BUTTON = "floatbutton.txt"
    val URL_HELP_FLOAT_BUTTON = "https://raw.githubusercontent.com/Blankeer/MDWechat/v3.0/data/help/float_button.md"
    val URL_HELP_ICON = "https://raw.githubusercontent.com/Blankeer/MDWechat/v3.0/data/help/icon.md"
    val URL_HELP_BUBBLE = "https://raw.githubusercontent.com/Blankeer/MDWechat/v3.0/data/help/bubble.md"
    val URL_JOIN_GROUP = "https://raw.githubusercontent.com/Blankeer/MDWechat/v3.0/data/help/join_group.md"

    val isVXPEnv: Boolean by lazy {
        VXPUtils.isVXPEnv()
    }

    val APP_DIR_PATH: String by lazy {
        // debug VXP 环境下，区分目录
        val appDir = if (isVXPEnv && BuildConfig.DEBUG) APP_VXP_DIR else APP_DIR
//        LogUtil.log("isVXPEnv = $isVXPEnv")
        LogUtil.log("app dir = $appDir")
        Environment.getExternalStorageDirectory().absolutePath + File.separator + appDir + File.separator
    }
}
