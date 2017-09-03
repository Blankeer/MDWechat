package com.blanke.mdwechat.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;

import com.blanke.mdwechat.Common;

import java.io.File;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by blanke on 2017/9/2.
 */

public class DrawableUtils {
    private static Bitmap getExternalStorageBitmap(String filePath) {
        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
        XposedBridge.log("bitmap path=" + sd + File.separator + filePath);
        return BitmapFactory.decodeFile(sd + File.separator + filePath);
    }

    public static Bitmap getExternalStorageAppBitmap(String filePath) {
        return getExternalStorageBitmap(Common.APP_DIR + File.separator + filePath);
    }

    public static Drawable getNineDrawable(Resources resources,Bitmap bitmap) {
        return new NinePatchDrawable(resources,bitmap, bitmap.getNinePatchChunk(), new Rect(), null);
    }
}
