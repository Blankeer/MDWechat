package com.blanke.mdwechat.settings

import android.app.Activity
import android.os.Bundle

import com.blanke.mdwechat.R

/**
 * Created by blanke on 2017/6/8.
 */

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fragmentManager.beginTransaction().replace(R.id.setting_fl_container,
                SettingsFragment()).commit()
    }

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
    }
}