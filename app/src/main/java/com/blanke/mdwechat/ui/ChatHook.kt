package com.blanke.mdwechat.ui

import android.graphics.Color
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.config.WxClass
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/9/2.
 */

class ChatHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(WxClass.ChatViewHolder!!,
                wxConfig.methods.ChatViewHolder_loadView, C.View, C.Boolean, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val bubbleLeft = AppCustomConfig.getBubbleLeftIcon()
                val bubbleRight = AppCustomConfig.getBubbleRightIcon()
                if (bubbleLeft == null && bubbleRight == null) {
                    return
                }
                val viewHolder = param!!.result
                val isOther = param.args[1] as Boolean
                val textMsgView = getObjectField(viewHolder, wxConfig.fields.ChatViewHolder_mChatTextView)
                if (textMsgView == null || textMsgView !is View) {
                    return
                }
                val msgTextView = XposedHelpers.getObjectField(textMsgView, wxConfig.fields.CellTextView_mMsgView)
                if (msgTextView != null) {
                    XposedHelpers.callMethod(msgTextView, "setTextColor", if (isOther) Color.RED else Color.BLUE)
                }
                if (isOther && bubbleLeft != null) {
                    textMsgView.background = DrawableUtils.getNineDrawable(textMsgView.resources, bubbleLeft)
                    if (HookConfig.isHookBubbleTint) {
                        DrawableCompat.setTint(textMsgView.background, Color.WHITE)
                    }
                }
                if (!isOther && bubbleRight != null) {
                    textMsgView.background = DrawableUtils.getNineDrawable(textMsgView.resources, bubbleRight)
                    if (HookConfig.isHookBubbleTint) {
                        DrawableCompat.setTint(textMsgView.background, colorPrimary)
                    }
                }
            }
        })
        xMethod(WxClass.ChatAudioViewHolder!!, wxConfig.methods.ChatAudioViewHolder_loadView, C.View, C.Boolean, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val bubbleLeft = AppCustomConfig.getBubbleLeftIcon()
                val bubbleRight = AppCustomConfig.getBubbleRightIcon()
                if (bubbleLeft == null && bubbleRight == null) {
                    return
                }
                val viewHolder = param.result
                val isOther = param.args[1] as Boolean
                val audioMsgView = getObjectField(viewHolder, wxConfig.fields.ChatAudioViewHolder_mChatTextView)
                if (audioMsgView == null || audioMsgView !is View) {
                    return
                }
                if (isOther && bubbleLeft != null) {
                    audioMsgView.background = DrawableUtils.getNineDrawable(audioMsgView.resources, bubbleLeft)
                    if (HookConfig.isHookBubbleTint) {
                        DrawableCompat.setTint(audioMsgView.background, Color.WHITE)
                    }
                }
                if (!isOther && bubbleRight != null) {
                    audioMsgView.background = DrawableUtils.getNineDrawable(audioMsgView.resources, bubbleRight)
                    if (HookConfig.isHookBubbleTint) {
                        DrawableCompat.setTint(audioMsgView.background, colorPrimary)
                    }
                }
            }
        })
    }
}
