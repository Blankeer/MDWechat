package com.blanke.mdwechat.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.R
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.util.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * Created by blanke on 2017/6/8.
 */

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fragmentManager.beginTransaction().replace(R.id.setting_fl_container,
                SettingsFragment()).commit()
        verifyStoragePermissions(this)
        copyConfig()
        findViewById(R.id.fab).setOnClickListener {
            copySharedPrefences()
            goToWechatSettingPage()
        }
        copySharedPrefences()
    }

    fun copySharedPrefences() {
        val sharedPrefsDir = File(filesDir, "../shared_prefs")
        val sharedPrefsFile = File(sharedPrefsDir, Common.MOD_PREFS + ".xml")
        if (sharedPrefsFile.exists()) {
            val outStream = FileOutputStream(File(AppCustomConfig.getConfigFile(Common.MOD_PREFS + ".xml")))
            FileUtils.copyFile(FileInputStream(sharedPrefsFile), outStream)
        }
    }

    private fun goToWechatSettingPage() {
        Toast.makeText(this, R.string.msg_kill_wechat, Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", Common.WECHAT_PACKAGENAME, null)
        startActivity(intent)
    }

    private fun copyConfig() {
        Thread {
            kotlin.run {
                FileUtils.copyAssets(this, Common.APP_DIR_PATH, Common.CONFIG_WECHAT_DIR)
                FileUtils.copyAssets(this, Common.APP_DIR_PATH, Common.CONFIG_VIEW_DIR)
                FileUtils.copyAssets(this, Common.APP_DIR_PATH, Common.ICON_DIR)
            }
        }.start()
    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    fun verifyStoragePermissions(activity: Activity) {
        try {
            val permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE")
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}