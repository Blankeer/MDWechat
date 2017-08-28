package com.blanke.mdwechat.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SettingsFragment extends PreferenceFragment {

    private Preference donatePreference;
    private SharedPreferences sp;
    private SwitchPreference hookSwitchPreference;
    private ColorPreference colorPrimaryPreference;

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
        hookSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean(getString(R.string.key_hook_switch), (Boolean) o);
                edit.apply();
                return true;
            }
        });
        colorPrimaryPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt(getString(R.string.key_color_primary), (int) o);
                edit.apply();
                return true;
            }
        });
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

    private String getWeChatVersion() {
        return "unKnow";
    }

    private boolean isSupportWechat() {
        return false;
    }

}