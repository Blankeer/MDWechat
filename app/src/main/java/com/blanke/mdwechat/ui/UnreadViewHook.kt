package com.blanke.mdwechat.ui

import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class UnreadViewHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {}
}
