package com.blanke.mdwechat.hookers

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.blanke.mdwechat.Classes.NoDrawingCacheLinearLayout
import com.blanke.mdwechat.WeChatHelper.defaultImageRippleDrawable
import com.blanke.mdwechat.WeChatHelper.transparentDrawable
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

                // ContactFragment 联系人 item
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


            }
        })
    }
}