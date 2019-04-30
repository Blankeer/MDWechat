package com.blanke.mdwechat.hookers.main

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.blanke.mdwechat.Objects
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.*
import com.blankj.utilcode.util.BarUtils
import de.robv.android.xposed.XposedHelpers

object HomeActionBarHook {
    fun fix(viewPagerLinearLayout: ViewGroup) {
        val resContext = viewPagerLinearLayout.context

        val px48 = ConvertUtils.dp2px(resContext, 48f)
        val tabBarHeight = if (HookConfig.is_hook_tab) px48 else 0
        val statusBarHeight = BarUtils.getStatusBarHeight() - 4 //
//        LogUtil.log("statusBarHeight = $statusBarHeight")
        val cb = { actionHeight: Int ->

            val viewpager = viewPagerLinearLayout.getChildAt(0)
            val layoutParams = viewpager.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = tabBarHeight + statusBarHeight + actionHeight
            viewpager.layoutParams = layoutParams
            viewpager.requestLayout()
            LogUtil.log("viewpager = ")
            LogUtil.logView(viewpager!!)
            waitInvoke(10, true, {
                ViewUtils.getChildView(viewpager, 0, 0, 3) != null
            }, {
                val conListView = ViewUtils.getChildView(viewpager, 0, 0, 3)
                LogUtil.logView(conListView!!)
                val params = conListView!!.layoutParams as RelativeLayout.LayoutParams
                params.setMargins(0, 0, 0, 0)
                conListView.requestLayout()
            })
            waitInvoke(10, true, {
                ViewUtils.getChildView(viewpager, 1) != null
            }, {
                mainThread(0 * 1000) {
                    val contentView = ViewUtils.getChildView(viewpager, 1)
                    LogUtil.log("contentView")
                    LogUtil.logView(contentView!!)
                    val lp = contentView.layoutParams
                    LogUtil.log("contentVIew width = ${lp.width} ${lp.height}")
                    LogUtil.logViewStackTraces(contentView!!)
                    val contentView2 = ViewUtils.getChildView(contentView, 0)
                    LogUtil.log("contentView2")
                    LogUtil.logView(contentView2!!)
                    val layoutParams1 = contentView2?.layoutParams as FrameLayout.LayoutParams
//                layoutParams1.setMargins(0, 0, 0, 0)
//                layoutParams1.gravity = Gravity.CENTER
                    layoutParams1.height = 500
                    layoutParams1.width = FrameLayout.LayoutParams.MATCH_PARENT

//                contentView2.requestLayout()
                    contentView2.layoutParams = layoutParams1
                    LogUtil.log("contentView2 = $layoutParams1")
//                contentView.grav
//                contentView.setPadding(0, 0, 0, 0)
//                val params = contentView!!.layoutParams as ViewGroup.MarginLayoutParams
//                params.setMargins(0, 0, 0, 0)
//                contentView.requestLayout()
                }
            })


        }
        if (!HookConfig.is_hook_hide_actionbar) {
            val mActionBar = Objects.Main.HomeUI_mActionBar.get()
            waitInvoke(10, true, {
                XposedHelpers.callMethod(mActionBar, "getHeight") as Int > 0
            }, {
                val actionHeight = XposedHelpers.callMethod(mActionBar, "getHeight") as Int
//                LogUtil.log("actionHeight=$actionHeight")
                cb(actionHeight)
            })
        } else {
            cb(0)
        }
    }
}