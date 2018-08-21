package com.blanke.mdwechat.settings

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.blanke.mdwechat.BuildConfig
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.R
import com.blanke.mdwechat.auto_search.Main
import com.blanke.mdwechat.auto_search.bean.LogEvent
import com.blanke.mdwechat.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


/**
 * Created by blanke on 2017/6/8.
 */

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
//        preferenceManager.setSharedPreferencesMode(Context.MODE_WORLD_READABLE)
        preferenceManager.setSharedPreferencesName(Common.MOD_PREFS)
        addPreferencesFromResource(R.xml.pref_settings)

        findPreference(getString(R.string.key_hide_launcher_icon))?.onPreferenceChangeListener = this
        findPreference(getString(R.string.key_donate))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_feedback))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_wechat_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_view_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_reset_icon_config))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_feedback_email))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_github))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_hook_conversation_bg))?.onPreferenceClickListener = this
        findPreference(getString(R.string.key_generate_wechat_config))?.onPreferenceClickListener = this
        if (BuildConfig.VERSION_NAME.endsWith("Beta", true)) {
            AlertDialog.Builder(activity)
                    .setTitle("警告")
                    .setMessage("当前版本为${BuildConfig.VERSION_NAME}版，不是正式版本，可能会遇到各种问题/无法预估的风险等。" +
                            "如果你想反馈问题，请打开最底部的调试开关，重启微信，将/sdcard/mdwechat/logs/目录下当天的日志发我邮箱。" +
                            "谢谢合作！")
                    .setPositiveButton("我知道了", null)
                    .setCancelable(false)
                    .show()
        }
    }

    override fun onPreferenceChange(preference: Preference, o: Any): Boolean {
        when (preference.key) {
            getString(R.string.key_hide_launcher_icon) -> showHideLauncherIcon(!(o as Boolean))
        }
        return true
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.key_donate) -> donate()
            getString(R.string.key_feedback) -> feedback()
            getString(R.string.key_reset_wechat_config) -> copyWechatConfig()
            getString(R.string.key_reset_view_config) -> copyViewConfig()
            getString(R.string.key_reset_icon_config) -> copyIcons()
            "key_feedback_email" -> sendEmail()
            "key_github" -> gotoGithub()
            "key_hook_main_bg" -> {

            }
            getString(R.string.key_generate_wechat_config) -> {
                generateWechatFile()
            }
        }
        return true
    }

    private var generateWechatLogView: TextView? = null
    private var generateWechatLogScrollView:ScrollView ?=null

    private fun generateWechatFile() {
        generateWechatLogScrollView = ScrollView(activity)
        generateWechatLogView = TextView(activity)
        generateWechatLogView?.setPadding(15, 0, 15, 0)
        generateWechatLogScrollView?.addView(generateWechatLogView)
        AlertDialog.Builder(activity)
                .setView(generateWechatLogScrollView)
                .setTitle(R.string.text_generate_wechat_config)
                .setCancelable(false)
                .setPositiveButton(R.string.text_confirm, null)
                .show()
        val outputPath = Common.APP_DIR_PATH + Common.CONFIG_WECHAT_DIR
        val pm = activity.packageManager
        try {
            val ai = pm.getApplicationInfo(Common.WECHAT_PACKAGENAME, 0)
            val wechatApkPath = ai.publicSourceDir
            Main().main(activity.applicationContext, wechatApkPath, outputPath)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort(R.string.msg_wechat_notfound)
            generateWechatLogView?.append(getString(R.string.msg_wechat_notfound) + "\n\n")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGerateWechatLogEvent(e: LogEvent) {
        generateWechatLogView?.append(e.msg + "\n\n")
        generateWechatLogScrollView?.fullScroll(View.FOCUS_DOWN)
    }

    private fun copyWechatConfig() {
        Thread {
            kotlin.run {
                FileUtils.copyAssets(activity, Common.APP_DIR_PATH, Common.CONFIG_WECHAT_DIR, true)
            }
        }.start()
        Toast.makeText(activity, R.string.msg_reset_ok, Toast.LENGTH_SHORT).show()
    }

    private fun copyViewConfig() {
        Thread {
            kotlin.run {
                FileUtils.copyAssets(activity, Common.APP_DIR_PATH, Common.CONFIG_VIEW_DIR, true)
            }
        }.start()
        Toast.makeText(activity, R.string.msg_reset_ok, Toast.LENGTH_SHORT).show()
    }

    private fun copyIcons() {
        Thread {
            kotlin.run {
                FileUtils.copyAssets(activity, Common.APP_DIR_PATH, Common.ICON_DIR, true)
                val nomediaFile = File(Common.APP_DIR_PATH + Common.ICON_DIR + File.separator + ".nomedia")
                if (!nomediaFile.exists()) {
                    nomediaFile.createNewFile()
                }
            }
        }.start()
        Toast.makeText(activity, R.string.msg_reset_ok, Toast.LENGTH_SHORT).show()
    }

    private fun showHideLauncherIcon(show: Boolean) {
        val p = activity.packageManager
        val componentName = ComponentName(activity, Common.MY_APPLICATION_PACKAGE + ".SettingsLauncher")
        p.setComponentEnabledSetting(componentName,
                if (show) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    private fun donate() {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val payUrl = "HTTPS://QR.ALIPAY.COM/FKX02968MD7TU2OGNMIW5D"
        intent.data = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl)
        if (intent.resolveActivity(activity.packageManager) != null) {
            startActivity(intent)
            return
        }
        intent.data = Uri.parse(payUrl.toLowerCase())
        startActivity(intent)
    }

    private fun feedback() {
        try {
            val str = "market://details?id=" + Common.MY_APPLICATION_PACKAGE
            val intent = Intent("android.intent.action.VIEW")
            intent.data = Uri.parse(str)
            intent.`package` = "com.coolapk.market"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } catch (e: Exception) {
            startActivity(Intent("android.intent.action.VIEW",
                    Uri.parse("http://www.coolapk.com/apk/" + Common.MY_APPLICATION_PACKAGE)))
        }
    }

    private fun sendEmail() {
        try {
            val info = "mailto:blanke.master+mdwechat@gmail.com?subject=[MDWechat] 请简明描述该问题" +
                    "&body=请按以下步骤填写,不按此填写的邮件可能会被忽略,谢谢!%0d%0a[问题描述] 请描述遇到了什么问题%0d%0a[环境]请写明安卓版本 手机 rom xp 版本%0d%0a[日志]可以传附件"
            val uri = Uri.parse(info)
            startActivity(Intent(Intent.ACTION_SENDTO, uri))
        } catch (e: Exception) {

        }
    }

    private fun gotoGithub() {
        val uri = Uri.parse("https://github.com/Blankeer/MDWechat")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private val weChatVersion: String
        get() = "unKnow"

    private val isSupportWechat: Boolean
        get() = false

}