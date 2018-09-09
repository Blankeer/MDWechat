package com.blanke.mdwechat.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Environment;

import com.blanke.mdwechat.Common;

import java.io.File;
import java.util.Arrays;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by blanke on 2017/9/2.
 */

public class DrawableUtils {
    private static Bitmap getExternalStorageBitmap(String filePath) {
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
//        LogUtil.log("bitmap path=" + sd + File.separator + filePath);
        return BitmapFactory.decodeFile(sd + File.separator + filePath);
    }

    public static Bitmap getExternalStorageAppBitmap(String filePath) {
        return getExternalStorageBitmap(Common.INSTANCE.getAPP_DIR() + File.separator + filePath);
    }

    public static Drawable getNineDrawable(Resources resources, Bitmap bitmap) {
//        return NinePatchBitmapFactory.createNinePatchDrawable(resources, bitmap);
        byte[] chunk = bitmap.getNinePatchChunk();
        boolean result = NinePatch.isNinePatchChunk(chunk);
        XposedBridge.log("chunk=" + Arrays.toString(chunk) + "result=" + result);
//        if (chunk == null || !result) {
//            return null;
//        }
        return new NinePatchDrawable(resources, bitmap, chunk, new Rect(), null);
    }

    public static RippleDrawable getTransparentColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(ColorStateList.valueOf(pressedColor), null, getRippleMask(normalColor));
    }

    public static RippleDrawable getColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(ColorStateList.valueOf(pressedColor), new ColorDrawable(normalColor), getRippleMask(normalColor));
    }

    private static Drawable getRippleMask(int color) {
        float[] outerRadii = new float[8];
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        Arrays.fill(outerRadii, 3);

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }
}
