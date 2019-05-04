package com.blanke.mdwechat.hookers.main

import android.view.View
import android.view.ViewGroup
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.ViewUtils
import com.blanke.mdwechat.util.waitInvoke
import de.robv.android.xposed.XposedHelpers
import org.jooq.tools.reflect.Reflect.on

object HomeActionBarHook {
    fun fix(viewPagerLinearLayout: ViewGroup) {
        val cb = { actionHeight: Int ->
            val viewpager = viewPagerLinearLayout.getChildAt(0)
            val layoutParams = viewpager.layoutParams as ViewGroup.MarginLayoutParams
            val offset = -4
            if (!HookConfig.is_hook_hide_actionbar && HookConfig.is_hook_tab) {
                layoutParams.topMargin = actionHeight + offset
            } else if (HookConfig.is_hook_hide_actionbar && !HookConfig.is_hook_tab) {
                layoutParams.topMargin = -actionHeight + offset
            } else {
                layoutParams.topMargin = offset
            }
            viewpager.layoutParams = layoutParams
            viewpager.requestLayout()
        }
        val mActionBar = Objects.Main.HomeUI_mActionBar.get()
        waitInvoke(10, true, {
            XposedHelpers.callMethod(mActionBar, "getHeight") as Int > 0
        }, {
            val actionHeight = XposedHelpers.callMethod(mActionBar, "getHeight") as Int
            LogUtil.log("actionHeight=$actionHeight")
            cb(actionHeight)
            if (HookConfig.is_hook_hide_actionbar) {
                LogUtil.log("隐藏 actionBar $mActionBar")
                XposedHelpers.callMethod(mActionBar, "hide")
                val actionView = on(mActionBar).call("getCustomView").get<View>()
                ViewUtils.getParentView(actionView, 1)?.visibility = View.GONE
            }
        })
    }
}