package com.blanke.mdwechat.ui

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/9/2.
 */

class ChatHook : BaseHookUi() {
    private var leftAudioAnimView: View? = null
    private var rightAudioAnimView: View? = null

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(C.View, "setBackgroundResource", C.Int, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val view = param.thisObject as View
                if (view.id == leftAudioAnimView?.id) {
                    view.background = getLeftBubble(view.resources)
                    if (HookConfig.is_hook_bubble_tint) {
                        DrawableCompat.setTint(view.background, HookConfig.get_hook_bubble_tint_left)
                    }
                } else if (view.id == rightAudioAnimView?.id) {
                    view.background = getRightBubble(view.resources)
                    if (HookConfig.is_hook_bubble_tint) {
                        DrawableCompat.setTint(view.background, HookConfig.get_hook_bubble_tint_right)
                    }
                }
            }
        })
        xMethod(wxConfig.classes.ChatViewHolder,
                wxConfig.methods.ChatViewHolder_loadView, C.View, C.Boolean, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                val viewHolder = param!!.result
                val isOther = param.args[1] as Boolean
                val textMsgView = getObjectField(viewHolder, wxConfig.fields.ChatViewHolder_mChatTextView)
                if (textMsgView == null || textMsgView !is View) {
                    return
                }
                val msgTextView = XposedHelpers.getObjectField(textMsgView, wxConfig.fields.CellTextView_mMsgView)
                if (msgTextView != null) {
                    val textColor = if (isOther) HookConfig.get_hook_chat_text_color_left else HookConfig.get_hook_chat_text_color_right
                    XposedHelpers.callMethod(msgTextView, "setTextColor", textColor)
                }
                val bubbleLeft = getLeftBubble(textMsgView.resources)
                val bubbleRight = getRightBubble(textMsgView.resources)
                if (bubbleLeft == null && bubbleRight == null) {
                    return
                }
                if (!HookConfig.is_hook_bubble) {
                    return
                }
                (textMsgView.parent as View).elevation = 1F
                if (isOther && bubbleLeft != null) {
                    textMsgView.background = bubbleLeft
                    if (HookConfig.is_hook_bubble_tint) {
                        DrawableCompat.setTint(textMsgView.background, HookConfig.get_hook_bubble_tint_left)
                    }
                }
                if (!isOther && bubbleRight != null) {
                    textMsgView.background = bubbleRight
                    if (HookConfig.is_hook_bubble_tint) {
                        DrawableCompat.setTint(textMsgView.background, HookConfig.get_hook_bubble_tint_right)
                    }
                }
            }
        })
        xMethod(wxConfig.classes.ChatAudioViewHolder, wxConfig.methods.ChatAudioViewHolder_loadView, C.View, C.Boolean, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val viewHolder = param.result
                val isOther = param.args[1] as Boolean
                val audioMsgView = getObjectField(viewHolder, wxConfig.fields.ChatAudioViewHolder_mChatTextView)
                if (audioMsgView == null || audioMsgView !is View) {
                    return
                }
                val bubbleLeft = getLeftBubble(audioMsgView.resources)
                val bubbleRight = getRightBubble(audioMsgView.resources)
                if (bubbleLeft == null && bubbleRight == null) {
                    return
                }
                if (!HookConfig.is_hook_bubble) {
                    return
                }
                (audioMsgView.parent as View).elevation = 1F
                if (isOther && bubbleLeft != null) {
                    audioMsgView.background = bubbleLeft
                    if (HookConfig.is_hook_bubble_tint) {
                        DrawableCompat.setTint(audioMsgView.background, HookConfig.get_hook_bubble_tint_left)
                    }
                }
                if (!isOther && bubbleRight != null) {
                    audioMsgView.background = bubbleRight
                    if (HookConfig.is_hook_bubble_tint) {
                        DrawableCompat.setTint(audioMsgView.background, HookConfig.get_hook_bubble_tint_right)
                    }
                }
                leftAudioAnimView = getObjectField(viewHolder, wxConfig.fields.ChatAudioViewHolder_mLeftAudioAnimImageView) as View
                rightAudioAnimView = getObjectField(viewHolder, wxConfig.fields.ChatAudioViewHolder_mRightAudioAnimImageView) as View
            }
        })
    }

    private fun getLeftBubble(resources: Resources): Drawable? {
        val bubbleLeft = AppCustomConfig.getBubbleLeftIcon()
        return DrawableUtils.getNineDrawable(resources, bubbleLeft)
    }

    private fun getRightBubble(resources: Resources): Drawable? {
        val bubble = AppCustomConfig.getBubbleRightIcon()
        return DrawableUtils.getNineDrawable(resources, bubble)
    }
}
