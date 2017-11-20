package com.blanke.mdwechat.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.AppCustomConfig
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ColorUtils
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/9/2.
 */

class ChatHook : BaseHookUi() {
    private var AUDIO_VIEW_TAG = 0
    private var audioAnimView: View? = null
    private var audioingView: View? = null

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        xMethod(C.View, "setBackgroundResource", C.Int, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val view = param.thisObject as View
                if (view.id == audioAnimView?.id || view.id === audioingView?.id) {
                    val isOther = view.getTag(AUDIO_VIEW_TAG)
                    if (isOther != null && isOther is Boolean) {
                        view.background = if (isOther) getLeftBubble(view.resources) else getRightBubble(view.resources)
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
                if (isOther && bubbleLeft != null) {
                    textMsgView.background = bubbleLeft
                }
                if (!isOther && bubbleRight != null) {
                    textMsgView.background = bubbleRight
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
                if (isOther && bubbleLeft != null) {
                    audioMsgView.background = bubbleLeft
                }
                if (!isOther && bubbleRight != null) {
                    audioMsgView.background = bubbleRight
                }
                audioAnimView = getObjectField(viewHolder, wxConfig.fields.ChatAudioViewHolder_mAudioAnimImageView) as View
                AUDIO_VIEW_TAG = audioAnimView!!.id
                audioAnimView!!.setTag(AUDIO_VIEW_TAG, isOther)
                audioingView = getObjectField(viewHolder, wxConfig.fields.ChatAudioViewHolder_mAudioingImageView) as View
                audioingView!!.setTag(AUDIO_VIEW_TAG, isOther)
            }
        })
    }

    private fun getDarkColor(color: Int): Int {
        return ColorUtils.getDarkerColor(color, 0.8F)
    }

    private fun getLeftBubble(resources: Resources,
                              isTint: Boolean = HookConfig.is_hook_bubble_tint,
                              tintColor: Int = HookConfig.get_hook_bubble_tint_left): Drawable? {
        val bubble = AppCustomConfig.getBubbleLeftIcon() ?: return null
        return getBubble(bubble, resources, isTint, tintColor)
    }

    private fun getRightBubble(resources: Resources,
                               isTint: Boolean = HookConfig.is_hook_bubble_tint,
                               tintColor: Int = HookConfig.get_hook_bubble_tint_right): Drawable? {
        val bubble = AppCustomConfig.getBubbleRightIcon() ?: return null
        return getBubble(bubble, resources, isTint, tintColor)
    }

    private fun getBubble(sourceBitmap: Bitmap,
                          resources: Resources,
                          isTint: Boolean = HookConfig.is_hook_bubble_tint,
                          tintColor: Int = HookConfig.get_hook_bubble_tint_right): Drawable? {
        val bubbleDrawable = DrawableUtils.getNineDrawable(resources, sourceBitmap)
        val drawable = StateListDrawable()
        val pressBubbleDrawable = bubbleDrawable.constantState!!.newDrawable().mutate()
        if (isTint) {
            bubbleDrawable.setTint(tintColor)
            pressBubbleDrawable.setTint(getDarkColor(tintColor))
        } else {
            pressBubbleDrawable.setTint(getDarkColor(Color.WHITE))
        }
        drawable.addState(intArrayOf(android.R.attr.state_pressed), pressBubbleDrawable)
        drawable.addState(intArrayOf(android.R.attr.state_focused), pressBubbleDrawable)
        drawable.addState(intArrayOf(), bubbleDrawable)
        return drawable
    }
}
