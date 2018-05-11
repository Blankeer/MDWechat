package com.blanke.mdwechat.hookers

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AbsListView
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
//                LogUtil.logView(view)
//                val listView = param.thisObject as AbsListView
//                val context = listView.context
            }
        })
    }
}