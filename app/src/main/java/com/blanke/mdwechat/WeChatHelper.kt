package com.blanke.mdwechat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import de.robv.android.xposed.XSharedPreferences
import java.io.File

object WeChatHelper {
    lateinit var XMOD_PREFS: XSharedPreferences

    val colorPrimary: Int by lazy {
        HookConfig.get_color_primary
    }

    val transparentDrawable: ColorDrawable
        get() = ColorDrawable(Color.TRANSPARENT)

    val colorPrimaryDrawable: ColorDrawable
        get() {
            val colorDrawable = ColorDrawable()
            colorDrawable.color = colorPrimary
            return colorDrawable
        }

    val defaultImageRippleDrawable: Drawable?
        get() {
            val context = Objects.Main.LauncherUI.get() ?: return null
            val attrs = intArrayOf(android.R.attr.selectableItemBackground)
            val ta = context.obtainStyledAttributes(attrs)
            val imageRippleDrawable = ta.getDrawable(0)
            ta.recycle()
            return imageRippleDrawable!!
        }

    val defaultImageRippleBorderDrawable: Drawable?
        get() {
            val context = Objects.Main.LauncherUI.get() ?: return null
            val attrs = intArrayOf(android.R.attr.selectableItemBackgroundBorderless)
            val ta = context.obtainStyledAttributes(attrs)
            val imageRippleDrawable = ta.getDrawable(0)
            ta.recycle()
            return imageRippleDrawable!!
        }

    fun startActivity(actName: String) {
        val context = Objects.Main.LauncherUI.get() ?: return
        val intent = Intent()
        intent.setClassName(context as Context, actName)
        context.startActivity(intent)
    }

    fun initPrefs() {
        XMOD_PREFS = XSharedPreferences(File(AppCustomConfig.getConfigFile(Common.MOD_PREFS + ".xml")))
        XMOD_PREFS.makeWorldReadable()
        XMOD_PREFS.reload()
    }

    fun reloadPrefs() {
        XMOD_PREFS.reload()
    }
}
