package com.blanke.mdwechat.util;

import android.graphics.Color;

/**
 * Created by blanke on 2017/10/5.
 */

public class ColorUtils {
    public static int getDarkerColor(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * factor;
        return Color.HSVToColor(hsv);
    }
}
