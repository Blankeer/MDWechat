package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.*

/**
 * Created by blanke on 2017/8/1.
 */

class ContactHook : BaseHookUi() {
    private val headViews: WeakHashMap<Int, View> = WeakHashMap()

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {

        xMethod(wxConfig.classes.ContactFragment,
                wxConfig.methods.MainFragment_onTabCreate,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val listView = getObjectField(param!!.thisObject, wxConfig.fields.ContactFragment_mListView) as ListView
                        //设置背景
                        val background = AppCustomConfig.getTabBg(1)
                        if (background != null) {
                            listView.background = BitmapDrawable(background)
                        } else {
                            listView.background = whiteDrawable
                        }
                        if (HookConfig.is_hook_ripple) {
                            if (listView.headerViewsCount > 0) {
                                val mHeaderViewInfos = getObjectField(listView, "mHeaderViewInfos") as ArrayList<ListView.FixedViewInfo>
                                for (j in 0 until mHeaderViewInfos.size) {
                                    val header = mHeaderViewInfos[j].view
                                    if (header != null) {
//                                        printViewTree(header, 0)
                                        if (header is ViewGroup) {
                                            val headLayout = header
                                            for (i in 0 until headLayout.childCount) {
                                                val item = headLayout.getChildAt(i)
                                                if (item !is ViewGroup || item.childCount == 0) {
                                                    continue
                                                }
                                                val itemContent = item.getChildAt(0)
                                                if (itemContent != null) {
                                                    // 新的朋友 等几个 item
                                                    itemContent.background = getTransparentRippleDrawable()
                                                    if (itemContent is ViewGroup) {
                                                        val childView = itemContent.getChildAt(0)
                                                        childView.background = transparentDrawable
                                                        if (childView is TextView) {// 企业号
                                                            itemContent.background = transparentDrawable
                                                            val lll = (itemContent.getChildAt(1) as ViewGroup)
                                                            for (m in 0 until lll.childCount) {
                                                                val comItem = (lll.getChildAt(m) as ViewGroup)
                                                                val ll = comItem.getChildAt(0) as ViewGroup
                                                                ll.background = getTransparentRippleDrawable()
                                                                // 去掉分割线
                                                                ll.getChildAt(0).background = transparentDrawable
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
                    }
                })
//        xMethod(C.View, "setBackgroundDrawable", C.Drawable, object : XC_MethodHook() {
//            override fun beforeHookedMethod(param: MethodHookParam?) {
//                val view = param?.thisObject as View
//                if (HookConfig.isHookripple && headViews.containsValue(view)) {
//                    log("view= $view setBackgroundDrawable")
////                    param.args[0] = getDefaultRippleDrawable(view.context)
//                    param.args[0] = ColorDrawable(Color.RED)
//                }
//            }
//        })
        xMethod(wxConfig.classes.ContactAdapter,
                "getView", C.Int, C.View, C.ViewGroup,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val view = param!!.result as ViewGroup
                        //                        printViewTree(view, 0);
                        if (HookConfig.is_hook_ripple) {
                            val itemContent = view.getChildAt(1) as ViewGroup
                            if (itemContent != null) {
                                itemContent.background = transparentDrawable
                                val item = itemContent.getChildAt(0)
                                if (item != null) {
                                    item.background = transparentDrawable
                                }
                            }
                            view.background = getTransparentRippleDrawable()
                        }
                    }
                })
    }
}
