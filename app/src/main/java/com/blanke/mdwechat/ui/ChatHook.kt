package com.blanke.mdwechat.ui

import android.graphics.Color
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import com.blanke.mdwechat.Common
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.DrawableUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers.getObjectField
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/9/2.
 */

class ChatHook : BaseHookUi() {
    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        val bubbleLeft = DrawableUtils.getExternalStorageAppBitmap(Common.CHAT_BUBBLE_LEFT_FILENAME)
        val bubbleRight = DrawableUtils.getExternalStorageAppBitmap(Common.CHAT_BUBBLE_RIGHT_FILENAME)
        if (bubbleLeft != null || bubbleRight != null) {
            xMethod(wxConfig.classes.ChatViewHolder,
                    wxConfig.methods.ChatViewHolder_loadView, C.View, C.Boolean, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                    val viewHolder = param!!.result
                    val isOther = param.args[1] as Boolean
                    val xoi = getObjectField(viewHolder, wxConfig.fields.ChatViewHolder_mChatTextView)
                    val iq = xoi as View
                    if (isOther && bubbleLeft != null) {
                        iq.background = DrawableUtils.getNineDrawable(iq.resources, bubbleLeft)
                        if (HookConfig.isHookBubbleTint) {
                            DrawableCompat.setTint(iq.background, Color.WHITE)
                        }
                    }
                    if (!isOther && bubbleRight != null) {
                        iq.background = DrawableUtils.getNineDrawable(iq.resources, bubbleRight)
                        if (HookConfig.isHookBubbleTint) {
                            DrawableCompat.setTint(iq.background, colorPrimary)
                        }
                        //                                float[] hsv = new float[3];
                        //                                Color.colorToHSV(getColorPrimary(), hsv);
                        //                                if (((double) hsv[2]) < 0.8d) {
                        //                                    iq.setTextColor(Color.WHITE);
                        //                                }
                    }
                }
            })
        }
    }
}
