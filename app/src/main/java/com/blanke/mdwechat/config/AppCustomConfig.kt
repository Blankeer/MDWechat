package com.blanke.mdwechat.config

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.bean.FloatButtonConfig
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Created by blanke on 2017/10/13.
 */

object AppCustomConfig {
    fun getWxVersionConfig(version: String): WxVersionConfig {
        var configName = version + ".config"
        if (HookConfig.isPlay) {
            configName = version + "-play.config"
        }
        val `is` = FileInputStream(getWxConfigFile(configName))
        return Gson().fromJson(InputStreamReader(`is`), WxVersionConfig::class.java)
    }

    fun getWxConfigFile(fileName: String): String {
        return Common.APP_DIR_PATH + Common.CONFIG_DIR + File.separator + fileName
    }

    fun getTabIcon(index: Int): Bitmap? {
        return getIcon(Common.FILE_NAME_TAB_PREFIX + "$index.png")
    }

    fun getBubbleLeftIcon(): Bitmap? {
        return getIcon(Common.CHAT_BUBBLE_LEFT_FILENAME)
    }

    fun getBubbleRightIcon(): Bitmap? {
        return getIcon(Common.CHAT_BUBBLE_RIGHT_FILENAME)
    }

    fun getTabBg(index: Int): Bitmap? {
        return getIcon(Common.FILE_NAME_TAB_BG_PREFIX + "$index.png")
    }

    fun getFloatButtonConfig(): FloatButtonConfig? {
        val path = getWxConfigFile(Common.FILE_NAME_FLOAT_BUTTON)
        try {
            val `is` = FileInputStream(path)
            return Gson().fromJson(InputStreamReader(`is`), FloatButtonConfig::class.java)
        } catch (e: Exception) {
            return null
        }
    }

    fun getIcon(fileName: String): Bitmap? {
        val filePath = Common.APP_DIR_PATH + Common.ICON_DIR + File.separator + fileName
        return BitmapFactory.decodeFile(filePath)
    }

}
