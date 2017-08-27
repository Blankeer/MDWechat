package com.blanke.mdwechat.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blanke.mdwechat.R;

/**
 * Created by blanke on 2017/6/8.
 */

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.setting_fl_container,
                new SettingsFragment()).commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }
}