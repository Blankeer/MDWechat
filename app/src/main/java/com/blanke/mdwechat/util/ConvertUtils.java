package com.blanke.mdwechat.util;

import android.content.Context;

public class ConvertUtils {
    /**
     * dp转px * * @param dpValue dp值 * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp * * @param pxValue px值 * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}