package com.blanke.mdwechat.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import java.util.Arrays;

/**
 * Created by blanke on 2017/9/2.
 */

public class DrawableUtils {

    public static Drawable getNineDrawable(Resources resources, Bitmap bitmap) {
        return NinePatchBitmapFactory.createNinePatchDrawable(resources, bitmap);
//        byte[] chunk = bitmap.getNinePatchChunk();
//        boolean result = NinePatch.isNinePatchChunk(chunk);
//        LogUtil.log("chunk=" + Arrays.toString(chunk) + "result=" + result);
//        LogUtil.log("h=" + bitmap.getHeight() + ",w=" + bitmap.getWidth());
//        if (chunk == null || !result) {
//            return new BitmapDrawable(bitmap);
//        }
//        return new NinePatchDrawable(resources, bitmap, chunk, new Rect(), null);
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
