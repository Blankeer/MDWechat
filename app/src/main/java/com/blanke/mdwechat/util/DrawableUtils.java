package com.blanke.mdwechat.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
}
