package com.blanke.mdwechat.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
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
                param!!.args[0] = ColorUtils.getDarkerColor(colorPrimary, 0.85f)
            }
        })
        //hook ToolbarWidgetWrapper
        xMethod(wxConfig.classes.ToolbarWidgetWrapper,
                "setCustomView", C.View,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val view = param!!.args[0] as View
                        //                        logViewTree(view);
                        val backIv = findViewByIdName(view, wxConfig.views.ActionBar_BackImageView) as ImageView
                        if (backIv != null) {
                            //TODO 删除selector 按下
                            //                            Drawable drawable = backIv.getDrawable();
                            //                            log("back drawable =" + drawable + "," + drawable.getClass().getName());
                            //                            if (drawable instanceof StateListDrawable) {
                            //                                log("back image is StateListDrawable");
                            //                                StateListDrawable stateListDrawable = (StateListDrawable) drawable;
                            //                                stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_long_pressable}, new ColorDrawable(Color.RED));
                            //                                backIv.setImageDrawable(stateListDrawable);
                            //                            }
                            backIv.background = getRippleDrawable(view.context)
                        }
                        val divider = findViewByIdName(view, wxConfig.views.ActionBar_Divider)
                        if (divider != null) {
                            divider.visibility = View.GONE
                        }
                    }
                })
        //hook search actionbar
        xMethod(wxConfig.classes.ActionBarSearchView,
                wxConfig.methods.ActionBarSearchView_init, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val view = param!!.thisObject as View
                val divider2 = findViewByIdName(view, wxConfig.views.SearchActionBar_Divider)
                if (divider2 != null) {
                    //                            log("search actionbar divider2=" + getViewMsg(divider2));
                    divider2.visibility = View.GONE
                }
                val backIv = findViewByIdName(view, wxConfig.views.SearchActionBar_BackImageView) as ImageView
                if (backIv != null) {
                    backIv.background = getRippleDrawable(view.context)
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
