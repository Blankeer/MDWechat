package com.blanke.mdwechat.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.blanke.mdwechat.Common;
import com.blanke.mdwechat.R;
import com.jaredrummler.android.colorpicker.ColorPreference;


/**
 * Created by blanke on 2017/6/8.
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private Preference donatePreference;
    private SharedPreferences sp;
    private SwitchPreference hookSwitchPreference;
    private ColorPreference colorPrimaryPreference;
    private SwitchPreference hookActionBarPreference;
    private SwitchPreference hookAvatarPreference;
    private SwitchPreference hookRipplePreference;
    private SwitchPreference hookFloatButtonPreference;
    private SwitchPreference hookSearchPreference;
    private SwitchPreference hookTabPreference;
    private SwitchPreference hookMenuGamePreference;
    private SwitchPreference hookMenuShopPreference;
    private SwitchPreference isPlayPreference;
    private SwitchPreference hideIconPreference;
    private Preference feedbackPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(Common.MOD_PREFS, Context.MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.pref_settings);

        donatePreference = findPreference(getString(R.string.key_donate));
        donatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                donate();
                return true;
            }
        });
        hookSwitchPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_switch));
        colorPrimaryPreference = (ColorPreference) findPreference(getString(R.string.key_color_primary));
        hookSwitchPreference.setOnPreferenceChangeListener(this);
        colorPrimaryPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt(getString(R.string.key_color_primary), (int) o);
                edit.apply();
                return true;
            }
        });
        hookActionBarPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_actionbar));
        hookActionBarPreference.setOnPreferenceChangeListener(this);
        hookAvatarPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_avatar));
        hookAvatarPreference.setOnPreferenceChangeListener(this);
        hookRipplePreference = (SwitchPreference) findPreference(getString(R.string.key_hook_ripple));
        hookRipplePreference.setOnPreferenceChangeListener(this);
        hookFloatButtonPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_float_button));
        hookFloatButtonPreference.setOnPreferenceChangeListener(this);
        hookSearchPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_search));
        hookSearchPreference.setOnPreferenceChangeListener(this);
        hookTabPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_tab));
        hookTabPreference.setOnPreferenceChangeListener(this);
        hookMenuGamePreference = (SwitchPreference) findPreference(getString(R.string.key_hook_menu_game));
        hookMenuGamePreference.setOnPreferenceChangeListener(this);
        hookMenuShopPreference = (SwitchPreference) findPreference(getString(R.string.key_hook_menu_shop));
        hookMenuShopPreference.setOnPreferenceChangeListener(this);
        isPlayPreference = (SwitchPreference) findPreference(getString(R.string.key_is_play));
        isPlayPreference.setOnPreferenceChangeListener(this);
        hideIconPreference = (SwitchPreference) findPreference(getString(R.string.key_hide_launcher_icon));
        hideIconPreference.setOnPreferenceChangeListener(this);
        feedbackPreference = findPreference(getString(R.string.key_feedback));
        feedbackPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                feedback();
                return true;
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals(getString(R.string.key_hide_launcher_icon))) {
            showHideLauncherIcon(!(Boolean) o);
        } else if (preference instanceof SwitchPreference) {
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean(preference.getKey(), (Boolean) o);
            edit.apply();
        }
        return true;
    }

    private void showHideLauncherIcon(boolean show) {
        PackageManager p = getActivity().getPackageManager();
        ComponentName componentName = new ComponentName(getActivity(), Common.MY_APPLICATION_PACKAGE + ".SettingsLauncher");
        p.setComponentEnabledSetting(componentName,
                show ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void donate() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        String payUrl = "HTTPS://QR.ALIPAY.COM/FKX02968MD7TU2OGNMIW5D";
        intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + payUrl));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
            return;
        }
        intent.setData(Uri.parse(payUrl.toLowerCase()));
        startActivity(intent);
    }

    private void feedback() {
        try {
            String str = "market://details?id=" + Common.MY_APPLICATION_PACKAGE;
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setPackage("com.coolapk.market");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            startActivity(new Intent("android.intent.action.VIEW",
                    Uri.parse("http://www.coolapk.com/apk/" + Common.MY_APPLICATION_PACKAGE)));
        }
    }

    private String getWeChatVersion() {
        return "unKnow";
    }

    private boolean isSupportWechat() {
        return false;
    }

}