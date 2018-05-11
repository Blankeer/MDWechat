package com.blanke.mdwechat.hookers

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.TextView
import com.blanke.mdwechat.Classes.NoDrawingCacheLinearLayout
import com.blanke.mdwechat.WeChatHelper.defaultImageRippleDrawable
import com.blanke.mdwechat.WeChatHelper.transparentDrawable
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.LogUtil.log
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object ListViewHooker : HookerProvider {
    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(listViewHook)
    }

    private val listViewHook = Hooker {
        XposedHelpers.findAndHookMethod(AbsListView::class.java, "setSelector", Drawable::class.java, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                param?.args!![0] = transparentDrawable
            }
        })
        XposedHelpers.findAndHookMethod(AbsListView::class.java, "obtainView", C.Int, BooleanArray::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val view = param?.result as View
                view.background = defaultImageRippleDrawable
//                log("--------------------")
//                LogUtil.logViewStackTraces(view)
//                log("--------------------")

                // TODO，移动到联系人页面 hook 实现
                // 联系人 item
                if (view.javaClass == NoDrawingCacheLinearLayout && view is ViewGroup) {
                    view.background = transparentDrawable
                    val contentView = view.getChildAt(1)
                    if (contentView != null && contentView is ViewGroup) {
                        contentView.background = defaultImageRippleDrawable
                        val innerView = contentView.getChildAt(0)
                        // 去掉分割线
                        innerView?.background = transparentDrawable
                    }
                }
                // 联系人 头部 新的朋友等
                if (view is LinearLayout && view.childCount == 7
                        && view.getChildAt(0).javaClass.name.contains("contact")) {
                    for (i in 0 until view.childCount) {
                        val headItemView = view.getChildAt(i)
                        log("----")
                        LogUtil.logViewStackTraces(headItemView)
                        log("----")
                        if (headItemView is ViewGroup) {
                            val contentView = headItemView.getChildAt(0)
                            contentView.background = defaultImageRippleDrawable
                            if (contentView is ViewGroup) {
                                // 去掉分割线
                                val innerView = contentView.getChildAt(0)
                                innerView?.background = transparentDrawable
                                log("innerView=${LogUtil.getViewLogInfo(innerView)}")
                                // 我的企业
                                if (innerView is TextView && innerView.text.contains("我的企业")) {
                                    val comLayout = contentView.getChildAt(1)
                                    if (comLayout != null && comLayout is LinearLayout) {
                                        for (j in 0 until comLayout.childCount) {
                                            val comItemView = comLayout.getChildAt(j)
                                            if (comItemView != null && comItemView is ViewGroup) {
                                                val comContentView = comItemView.getChildAt(0)
                                                if (comContentView != null && comContentView is ViewGroup) {
                                                    comContentView.background = defaultImageRippleDrawable
                                                    comContentView.getChildAt(0)?.background = transparentDrawable
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}