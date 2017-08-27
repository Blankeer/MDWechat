package com.blanke.mdwechat.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.blanke.mdwechat.R;


/**
 * Created by blanke on 2017/6/8.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
    }

    private String getWeChatVersion() {
        return "unKnow";
    }

    private boolean isSupportWechat() {
        return false;
    }

}