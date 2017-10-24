package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
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
                                val header = mHeaderViewInfos[0].view
                                if (header != null) {
//                                    printViewTree(header, 0)
                                    val headLayout = header as ViewGroup
                                    for (i in 0..headLayout.childCount - 1) {
                                        val item = headLayout.getChildAt(i) as ViewGroup
                                        if (item.childCount == 0) {
                                            continue
                                        }
                                        val itemContent = item.getChildAt(0)
//                                        headViews.put(i, itemContent)
                                        itemContent.background = getTransparentRippleDrawable()
                                        if (itemContent != null) {
                                            val childView = (itemContent as ViewGroup).getChildAt(0)
                                            if (childView != null) {
                                                childView.background = transparentDrawable
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
