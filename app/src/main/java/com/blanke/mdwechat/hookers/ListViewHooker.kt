package com.blanke.mdwechat.hookers

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import com.blanke.mdwechat.Classes.NoDrawingCacheLinearLayout
import com.blanke.mdwechat.ViewTreeRepo
import com.blanke.mdwechat.WeChatHelper.defaultImageRippleDrawable
import com.blanke.mdwechat.WeChatHelper.transparentDrawable
import com.blanke.mdwechat.util.LogUtil
import com.blanke.mdwechat.util.LogUtil.log
import com.blanke.mdwechat.util.ViewTreeUtils
import com.blanke.mdwechat.util.ViewUtils
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
                log("--------------------")
                LogUtil.logViewStackTraces(view)
                log("--------------------")

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

                // ConversationFragment 聊天列表 item
                if (ViewTreeUtils.equals(ViewTreeRepo.ConversationListViewItem, view)) {
                    val chatNameView = ViewUtils.getChildView(view, 1, 0, 0, 0)
                    val chatTimeView = ViewUtils.getChildView(view, 1, 0, 1)
                    val recentMsgView = ViewUtils.getChildView(view, 1, 1, 0, 1)
                    val unreadCountView = ViewUtils.getChildView(view, 0, 1) as TextView
                    val unreadView = ViewUtils.getChildView(view, 0, 2) as ImageView

                    LogUtil.log("chatNameView=$chatNameView,chatTimeView=$chatTimeView,recentMsgView=$recentMsgView")
                    XposedHelpers.callMethod(chatNameView, "setTextColor", Color.RED)
                    XposedHelpers.callMethod(chatTimeView, "setTextColor", Color.BLUE)
                    XposedHelpers.callMethod(recentMsgView, "setTextColor", Color.YELLOW)
                    unreadCountView.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                    unreadView.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                }
            }
        })
    }
}