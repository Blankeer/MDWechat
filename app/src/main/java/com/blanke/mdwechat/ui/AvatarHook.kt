package com.blanke.mdwechat.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import com.blanke.mdwechat.WeChatHelper.wxConfig
import com.blanke.mdwechat.WeChatHelper.xMethod
import com.blanke.mdwechat.config.C
import com.blanke.mdwechat.config.HookConfig
import com.blanke.mdwechat.util.ImageHelper
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by blanke on 2017/8/1.
 */

class AvatarHook : BaseHookUi() {

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!HookConfig.isHookavatar) {
            return
        }
        // hook avatar bitmap
        xMethod(wxConfig.classes.AvatarUtils2,
                wxConfig.methods.AvatarUtils2_getAvatarHDBitmap,
                C.String,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val res = param!!.result
                        if (res != null) {
                            val bitmap = res as Bitmap
                            val hookBitmap = ImageHelper.getRoundedCornerBitmap(bitmap, bitmap.height / 2)
                            param.result = hookBitmap
                        }
                    }
                })
        xMethod(wxConfig.classes.AvatarUtils2,
                wxConfig.methods.AvatarUtils2_getAvatarBitmap,
                C.String,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val res = param!!.result
                        if (res != null) {
                            val bitmap = res as Bitmap
                            val hookBitmap = ImageHelper.getRoundedCornerBitmap(bitmap, bitmap.height / 2)
                            param.result = hookBitmap
                        }
                    }
                })
        xMethod(wxConfig.classes.AvatarUtils2,
                wxConfig.methods.AvatarUtils2_getDefaultAvatarBitmap,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val res = param!!.result
                        if (res != null) {
                            val bitmap = res as Bitmap
                            val hookBitmap = ImageHelper.getRoundedCornerBitmap(bitmap, bitmap.height / 2)
                            param.result = hookBitmap
                        }
                    }
                })
        // hook sns avatar imageview
        xMethod(wxConfig.classes.TouchImageView,
                wxConfig.methods.TouchImageView_init,
                object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                        val imageView = param!!.thisObject as ImageView
                        imageView.setBackgroundColor(Color.TRANSPARENT)
                        // setOnTouchListener is no use,will throw exception
                        //                imageView.setOnTouchListener(new View.OnTouchListener() {
                        //                    @Override
                        //                    public boolean onTouch(View v, MotionEvent event) {
                        //                        return false;
                        //                    }
                        //                });
                    }
                })
    }
}
