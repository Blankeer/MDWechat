package com.blanke.mdwechat.ui;

import android.graphics.drawable.Drawable;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by blanke on 2017/8/1.
 */

public class SettingsHook extends BaseHookUi {
    private boolean hookListViewBackground = false;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {

    }

    private Drawable getBackground() {
        return getColorPrimaryDrawable();
    }
}
