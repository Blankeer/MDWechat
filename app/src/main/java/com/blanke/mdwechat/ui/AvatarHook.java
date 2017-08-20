package com.blanke.mdwechat.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.blanke.mdwechat.util.ImageHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.blanke.mdwechat.WeChatHelper.WCClasses.AvatarUtil;
import static com.blanke.mdwechat.WeChatHelper.WCClasses.TouchImageView;
import static com.blanke.mdwechat.WeChatHelper.WCMethods.getAvatarBitmap;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by blanke on 2017/8/1.
 */

public class AvatarHook extends BaseHookUi {

    public AvatarHook() {
        init();
    }

    private void init() {
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        // hook avatar bitmap
        findAndHookMethod(AvatarUtil,
                getAvatarBitmap, String.class, boolean.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Object res = param.getResult();
                        if (res != null) {
                            Bitmap bitmap = (Bitmap) res;
                            Bitmap hookBitmap = ImageHelper.getRoundedCornerBitmap(bitmap, bitmap.getHeight() / 2);
                            param.setResult(hookBitmap);
                        }
                    }
                });
        // hook sns avatar imageview
        findAndHookMethod(TouchImageView, "init", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ImageView imageView = (ImageView) param.thisObject;
                imageView.setBackgroundColor(Color.TRANSPARENT);
                // setOnTouchListener is no use,will throw exception
//                imageView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        return false;
//                    }
//                });
            }
        });
    }
}
