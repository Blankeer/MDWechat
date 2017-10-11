package com.blanke.mdwechat.settings

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference

import com.blanke.mdwechat.Common
import com.blanke.mdwechat.R
import com.jaredrummler.android.colorpicker.ColorPreference


/**
 * Created by blanke on 2017/6/8.
 */

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    private var donatePreference: Preference? = null
    private var sp: SharedPreferences? = null
    private var hookSwitchPreference: SwitchPreference? = null
    private var colorPrimaryPreference: ColorPreference? = null
    private var hookActionBarPreference: SwitchPreference? = null
    private var hookAvatarPreference: SwitchPreference? = null
    private var hookRipplePreference: SwitchPreference? = null
    private var hookFloatButtonPreference: SwitchPreference? = null
    private var hookSearchPreference: SwitchPreference? = null
    private var hookTabPreference: SwitchPreference? = null
    private var hookMenuGamePreference: SwitchPreference? = null
    private var hookMenuShopPreference: SwitchPreference? = null
    private var hookMenuQrcodePreference: SwitchPreference? = null
    private var hookMenuShakePreference: SwitchPreference? = null
    private var hookMenuNearPreference: SwitchPreference? = null
    private var isPlayPreference: SwitchPreference? = null
    private var hideIconPreference: SwitchPreference? = null
    private var feedbackPreference: Preference? = null
    private var bubbleTintPreference: SwitchPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = activity.getSharedPreferences(Common.MOD_PREFS, Context.MODE_WORLD_READABLE)
        addPreferencesFromResource(R.xml.pref_settings)

        donatePreference = findPreference(getString(R.string.key_donate))
        donatePreference!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            donate()
            true
        }
        hookSwitchPreference = findPreference(getString(R.string.key_hook_switch)) as SwitchPreference
        colorPrimaryPreference = findPreference(getString(R.string.key_color_primary)) as ColorPreference
        hookSwitchPreference!!.onPreferenceChangeListener = this
        colorPrimaryPreference!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            val edit = sp!!.edit()
            edit.putInt(getString(R.string.key_color_primary), o as Int)
            edit.apply()
            true
        }
        hookActionBarPreference = findPreference(getString(R.string.key_hook_actionbar)) as SwitchPreference
        hookActionBarPreference!!.onPreferenceChangeListener = this
        hookAvatarPreference = findPreference(getString(R.string.key_hook_avatar)) as SwitchPreference
        hookAvatarPreference!!.onPreferenceChangeListener = this
        hookRipplePreference = findPreference(getString(R.string.key_hook_ripple)) as SwitchPreference
        hookRipplePreference!!.onPreferenceChangeListener = this
        hookFloatButtonPreference = findPreference(getString(R.string.key_hook_float_button)) as SwitchPreference
        hookFloatButtonPreference!!.onPreferenceChangeListener = this
        hookSearchPreference = findPreference(getString(R.string.key_hook_search)) as SwitchPreference
        hookSearchPreference!!.onPreferenceChangeListener = this
        hookTabPreference = findPreference(getString(R.string.key_hook_tab)) as SwitchPreference
        hookTabPreference!!.onPreferenceChangeListener = this
        hookMenuGamePreference = findPreference(getString(R.string.key_hook_menu_game)) as SwitchPreference
        hookMenuGamePreference!!.onPreferenceChangeListener = this
        hookMenuShopPreference = findPreference(getString(R.string.key_hook_menu_shop)) as SwitchPreference
        hookMenuShopPreference!!.onPreferenceChangeListener = this
        isPlayPreference = findPreference(getString(R.string.key_is_play)) as SwitchPreference
        isPlayPreference!!.onPreferenceChangeListener = this
        hideIconPreference = findPreference(getString(R.string.key_hide_launcher_icon)) as SwitchPreference
        hideIconPreference!!.onPreferenceChangeListener = this
        feedbackPreference = findPreference(getString(R.string.key_feedback))
        feedbackPreference!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            feedback()
            true
        }
        bubbleTintPreference = findPreference(getString(R.string.key_hook_bubble_tint)) as SwitchPreference
        bubbleTintPreference!!.onPreferenceChangeListener = this
        hookMenuQrcodePreference = findPreference(getString(R.string.key_hook_menu_qrcode)) as SwitchPreference
        hookMenuQrcodePreference!!.onPreferenceChangeListener = this
        hookMenuShakePreference = findPreference(getString(R.string.key_hook_menu_shake)) as SwitchPreference
        hookMenuShakePreference!!.onPreferenceChangeListener = this
        hookMenuNearPreference = findPreference(getString(R.string.key_hook_menu_near)) as SwitchPreference
        hookMenuNearPreference!!.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, o: Any): Boolean {
        if (preference.key == getString(R.string.key_hide_launcher_icon)) {
            showHideLauncherIcon(!(o as Boolean))
        } else if (preference is SwitchPreference) {
            val edit = sp!!.edit()
            edit.putBoolean(preference.getKey(), o as Boolean)
            edit.apply()
        }
        return true
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

    private val weChatVersion: String
        get() = "unKnow"

    private val isSupportWechat: Boolean
        get() = false

}