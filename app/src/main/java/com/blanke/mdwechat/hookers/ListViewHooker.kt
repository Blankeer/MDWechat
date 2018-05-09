package com.blanke.mdwechat.hookers

import android.view.View
import android.widget.AbsListView
import android.widget.ListView
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
        XposedHelpers.findAndHookConstructor(C.ListView, C.Context, C.AttributeSet, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val listView = param?.thisObject as ListView
                listView.selector = transparentDrawable
            }
        })
        XposedHelpers.findAndHookMethod(AbsListView::class.java, "obtainView", C.Int, BooleanArray::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val view = param?.result as View
                LogUtil.logView(view)
                val listView = param.thisObject as AbsListView
                val context = listView.context
                log("context=$context")
//                if (context.javaClass == LauncherUI) {
                view.background = defaultImageRippleDrawable
//                }
            }
        })
    }
}