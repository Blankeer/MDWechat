package com.blanke.mdwechat.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ColorUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class ActionBarHook : BaseHookUi() {
    private val actionBarColorDrawable: ColorDrawable
    private val actionBarSplitDrawable: ColorDrawable

    init {
        actionBarColorDrawable = ColorDrawable()
        actionBarSplitDrawable = ColorDrawable()
        actionBarSplitDrawable.color = Color.TRANSPARENT
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!HookConfig.isHookactionbar) {
            return
        }
        xMethod(wxConfig.classes.ActionBarContainer,
                "onFinishInflate",
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        setObjectField(param!!.thisObject,
                                wxConfig.fields.ActionBarContainer_mBackground,
                                colorPrimaryDrawable)
                        val actionbar = param.thisObject as View
                        actionbar.elevation = 0f
                    }
                })
        //set statusBar color
        findAndHookMethod("com.android.internal.policy.PhoneWindow", lpparam.classLoader,
                "setStatusBarColor", C.Int, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                param!!.args[0] = getStatueBarColor()
            }
        })
        //hook ToolbarWidgetWrapper
        xMethod(wxConfig.classes.ToolbarWidgetWrapper,
                "setCustomView", C.View,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val view = param!!.args[0] as ViewGroup
//                        logViewTree(view)
                        for (i in 0..view.childCount) {
                            val leftImageLayout = view.getChildAt(i)
                            if (leftImageLayout != null && leftImageLayout is LinearLayout) {
                                if (leftImageLayout.childCount == 2
                                        && leftImageLayout.getChildAt(0) is ImageView
                                        && leftImageLayout.getChildAt(1) is ImageView) {
                                    val backImageView = leftImageLayout.getChildAt(0) as ImageView
                                    val dividerImageView = leftImageLayout.getChildAt(1) as ImageView
//                                log("backImageView=$backImageView,dividerImageView=$dividerImageView")
                                    dividerImageView.visibility = View.GONE
                                    break
                                }
                            }
                        }
                    }
                })
        //hook search actionbar
        xMethod(wxConfig.classes.ActionBarSearchView,
                wxConfig.methods.ActionBarSearchView_init, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val view = param!!.thisObject as ViewGroup
//                logViewTree(view)
                val contentLayout = view.getChildAt(0) as RelativeLayout
                val leftImageLayout = contentLayout.getChildAt(0)
                if (leftImageLayout != null && leftImageLayout is LinearLayout) {
                    if (leftImageLayout.childCount == 2
                            && leftImageLayout.getChildAt(0) is ImageView
                            && leftImageLayout.getChildAt(1) is ImageView) {
                        val backImageView = leftImageLayout.getChildAt(0) as ImageView
                        val dividerImageView = leftImageLayout.getChildAt(1) as ImageView
//                        log("backImageView=$backImageView,dividerImageView=$dividerImageView")
                        dividerImageView.visibility = View.GONE
                    }
                }
            }
        })
        findAndHookConstructor(wxConfig.classes.ActionBarEditText,
                lpparam.classLoader, C.Context, C.AttributeSet,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val editText = param!!.thisObject as EditText
                        editText.setTextColor(Color.WHITE)
                        editText.setHintTextColor(Color.WHITE)
                        editText.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                    }
                })
    }

    private fun getStatueBarColor(): Int {
        return ColorUtils.getDarkerColor(colorPrimary, 0.85f)
    }

    private fun isSetActionBarActivity(activity: String): Boolean {
        for (act in excludeStatusBarActivities) {
            if (act == activity) {
                return false
            }
        }
        return true
    }

    companion object {

        //不开启状态栏沉浸的 activities
        var excludeStatusBarActivities = arrayOf<String>()
    }
}
