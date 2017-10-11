package com.blanke.mdwechat.ui

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.*

/**
 * Created by blanke on 2017/8/1.
 */

class ContactHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (HookConfig.isHookripple) {
            xMethod(wxConfig.classes.ContactFragment,
                    wxConfig.methods.MainFragment_onTabCreate,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                            val listView = getObjectField(param!!.thisObject, wxConfig.fields.ContactFragment_mListView) as ListView
                            if (listView.headerViewsCount > 0) {
                                val mHeaderViewInfos = getObjectField(listView, "mHeaderViewInfos") as ArrayList<ListView.FixedViewInfo>
                                val header = mHeaderViewInfos[0].view
                                //                            log("header=" + header);
                                if (header != null) {
                                    //                                printViewTree(header, 0);
                                    val headLayout = header as ViewGroup
                                    for (i in 0..headLayout.childCount - 1) {
                                        val item = headLayout.getChildAt(i) as ViewGroup
                                        val itemContent = item.getChildAt(0) as ViewGroup
                                        if (itemContent != null) {
                                            itemContent.background = getRippleDrawable(headLayout.context)
                                            itemContent.getChildAt(0).background = transparentDrawable
                                        }
                                    }
                                }
                            }
                            val background = DrawableUtils.getExternalStorageAppBitmap(Common.CONTACT_BACKGROUND_FILENAME)
                            if (background != null) {
                                listView.background = BitmapDrawable(background)
                            } else {
                                listView.background = whiteDrawable
                            }
                        }
                    })
            xMethod(wxConfig.classes.ContactAdapter,
                    "getView", C.Int, C.View, C.ViewGroup,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                            val view = param!!.result as ViewGroup
                            //                        printViewTree(view, 0);
                            if (view != null) {
                                val itemContent = view.getChildAt(1) as ViewGroup
                                if (itemContent != null) {
                                    itemContent.background = transparentDrawable
                                    val item = itemContent.getChildAt(0)
                                    if (item != null) {
                                        item.background = transparentDrawable
                                    }
                                }
                                if (HookConfig.isHookripple) {
                                    view.background = getRippleDrawable(view.context)
                                }
                            }
                        }
                    })
        }
    }
}
