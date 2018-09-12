package com.blanke.mdwechat.hookers

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ListView
import com.blanke.mdwechat.CC
import com.blanke.mdwechat.Classes
import com.blanke.mdwechat.Classes.ConversationWithAppBrandListView
import com.blanke.mdwechat.Fields.ConversationFragment_mListView
import com.blanke.mdwechat.Methods.ConversationWithAppBrandListView_isAppBrandHeaderEnable
import com.blanke.mdwechat.Version
import com.blanke.mdwechat.WeChatHelper.defaultImageRippleDrawable
import com.blanke.mdwechat.WeChatHelper.whiteDrawable
import com.blanke.mdwechat.WechatGlobal
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.hookers.base.Hooker
import com.blanke.mdwechat.hookers.base.HookerProvider
import com.blanke.mdwechat.util.LogUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object ConversationHooker : HookerProvider {
    const val keyInit = "key_init"

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(resumeHook, disableAppBrand, headViewHook)
    }

    private val resumeHook = Hooker {
        XposedHelpers.findAndHookMethod(Classes.Fragment, "performResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
//                LogUtil.log("Fragment.performResume f=${param!!.thisObject::class.java.name}")
                val fragment = param?.thisObject ?: return
                if (fragment.javaClass.name != Classes.ConversationFragment.name) {
                    return
                }
//                LogUtil.logSuperClasses(param!!.thisObject::class.java)
                val isInit = XposedHelpers.getAdditionalInstanceField(fragment, keyInit)
                if (isInit != null) {
                    LogUtil.log("ConversationFragment 已经hook过")
                    return
                }
                XposedHelpers.setAdditionalInstanceField(fragment, keyInit, true)
                init(fragment)
            }

            private fun init(fragment: Any) {
                val listView = ConversationFragment_mListView.get(fragment)
                if (listView != null && listView is View) {
                    val background = AppCustomConfig.getTabBg(0)
                    listView.background = if (background != null) BitmapDrawable(background) else whiteDrawable
                }
            }
        })
    }

    private val disableAppBrand = Hooker {
        XposedHelpers.findAndHookMethod(ConversationWithAppBrandListView,
                ConversationWithAppBrandListView_isAppBrandHeaderEnable.name, CC.Boolean, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (HookConfig.is_hook_remove_appbrand) {
                    try {
                        if (WechatGlobal.wxVersion!! >= Version("6.7.2")) {
                            val listView = param.thisObject as ListView
                            val mHeaderViewInfos = XposedHelpers.getObjectField(listView, "mHeaderViewInfos") as List<*>
                            if (mHeaderViewInfos.isNotEmpty()) {
                                val firstHeadView = mHeaderViewInfos[0] as ListView.FixedViewInfo
                                val mAppBrandDesktopHalfContainer = firstHeadView.view as ViewGroup
//                                LogUtil.log("firstHeadView=${firstHeadView.view}")
                                if (mAppBrandDesktopHalfContainer::class.java.name.contains("AppBrandDesktopHalfContainer")) {
                                    mAppBrandDesktopHalfContainer.getChildAt(1)?.visibility = View.GONE
                                }
                            }
                        }
                    } catch (t: Throwable) {
                        LogUtil.log(t)
                    }
                    param.result = false
                }
            }
        })
    }

    private val headViewHook = Hooker {
        XposedHelpers.findAndHookMethod(CC.ListView, "addHeaderView", CC.View, CC.Object, CC.Boolean,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        val listView = param?.thisObject
                        if (listView?.javaClass?.name != ConversationWithAppBrandListView.name) {
                            return
                        }
                        val view = param?.args!![0] as View
//                        LogUtil.log("ConversationWithAppBrandListView addHeadView = ${view}")
                        if (view is ViewGroup && view.getChildAt(0) != null) {
                            view.getChildAt(0).viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    val oldBackground = view.getChildAt(0).background
                                    if (oldBackground == defaultImageRippleDrawable) {
                                        view.getChildAt(0).viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    } else {
                                        view.getChildAt(0).background = defaultImageRippleDrawable
                                    }
                                }
                            })
                        }
                    }
                })
    }
}