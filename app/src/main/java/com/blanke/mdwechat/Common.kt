package com.blanke.mdwechat

import android.os.Environment
import java.io.File

/**
 * Created by blanke on 2017/7/29.
 */

object Common {
    val MY_APPLICATION_PACKAGE = "com.blanke.mdwechat"
    val WECHAT_PACKAGENAME = "com.tencent.mm"
    val MOD_PREFS = "md_wechat_settings"
    val APP_DIR = "mdwechat"
    val CONFIG_DIR = "config"
    val ICON_DIR = "icon"
    val CONVERSATION_BACKGROUND_FILENAME = "conversation.png"
    val CONTACT_BACKGROUND_FILENAME = "contact.png"
    val DISCOVER_BACKGROUND_FILENAME = "discover.png"
    val SETTINGS_BACKGROUND_FILENAME = "settings.png"
    val CHAT_BUBBLE_LEFT_FILENAME = "bubble_left.9.png"
    val CHAT_BUBBLE_RIGHT_FILENAME = "bubble_right.9.png"
    val FILE_NAME_TAB_PREFIX = "tab"
    val FILE_NAME_TAB_BG_PREFIX = "tab_bg"

    val APP_DIR_PATH: String
        get() {
            return Environment.getExternalStorageDirectory().absolutePath + File.separator + APP_DIR + File.separator
        }
}
