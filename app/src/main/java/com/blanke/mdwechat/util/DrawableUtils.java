package com.blanke.mdwechat.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Environment;

import com.blanke.mdwechat.Common;

import java.io.File;

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
        return getExternalStorageBitmap(Common.APP_DIR + File.separator + filePath);
    }

    public static Drawable getNineDrawable(Resources resources, Bitmap bitmap) {
        return NinePatchBitmapFactory.createNinePatchDrawable(resources, bitmap);
    }

    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                normalColor
                        }
        );
    }

    public static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }
}
