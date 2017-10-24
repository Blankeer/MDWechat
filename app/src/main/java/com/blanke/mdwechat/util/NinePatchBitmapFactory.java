package com.blanke.mdwechat.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: bgriffey
 * Date: 12/27/12
 * Time: 2:37 PM
 */
public class NinePatchBitmapFactory {

    private static final int NO_COLOR = 0x00000001;

    private static final int TRANSPARENT_COLOR = 0x00000000;

    public static NinePatchDrawable createNinePatchDrawable(Resources res, Bitmap bitmap) {
        RangeLists rangeLists = checkBitmap(bitmap);
        Bitmap trimedBitmap = trimBitmap(bitmap);
        NinePatchDrawable drawable = createNinePatchWithCapInsets(res, trimedBitmap, rangeLists.rangeListX, rangeLists.rangeListY, null);
        return drawable;
    }


    public static NinePatchDrawable createNinePatchWithCapInsets(Resources res, Bitmap bitmap,
                                                                 List<Range> rangeListX, List<Range> rangeListY, String srcName) {
        ByteBuffer buffer = getByteBuffer(rangeListX, rangeListY);
        NinePatchDrawable drawable = new NinePatchDrawable(res, bitmap, buffer.array(), new Rect(), srcName);
        return drawable;
    }

    private static ByteBuffer getByteBuffer(List<Range> rangeListX, List<Range> rangeListY) {
        ByteBuffer buffer = ByteBuffer.allocate(4 + 4 * 7 + 4 * 2 * rangeListX.size() + 4 * 2 * rangeListY.size() + 4 * 9).order(ByteOrder.nativeOrder());
        buffer.put((byte) 0x01); // was serialised
        buffer.put((byte) (rangeListX.size() * 2)); // x div
        buffer.put((byte) (rangeListY.size() * 2)); // y div
        buffer.put((byte) 0x09); // color

        // skip
        buffer.putInt(0);
        buffer.putInt(0);

        // padding
        buffer.putInt(0);
        buffer.putInt(0);
        buffer.putInt(0);
        buffer.putInt(0);

        // skip 4 bytes
        buffer.putInt(0);

        for (Range range : rangeListX) {
            buffer.putInt(range.start);
            buffer.putInt(range.end);
        }
        for (Range range : rangeListY) {
            buffer.putInt(range.start);
            buffer.putInt(range.end);
        }
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);
        buffer.putInt(NO_COLOR);

        return buffer;


    }

    public static class RangeLists {
        public List<Range> rangeListX;
        public List<Range> rangeListY;
    }

    public static class Range {
        public int start;
        public int end;
    }

    public static RangeLists checkBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        List<Range> rangeListX = new ArrayList<Range>();

        int pos = -1;
        for (int i = 1; i < width - 1; i++) {
            int color = bitmap.getPixel(i, 0);
            int alpha = Color.alpha(color);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
//			System.out.println( String.valueOf(alpha) + "," + String.valueOf(red) + "," + String.valueOf(green) + "," + String.valueOf(blue) );
            if (alpha == 255 && red == 0 && green == 0 && blue == 0) {
                if (pos == -1) {
                    pos = i - 1;
                }
            } else {
                if (pos != -1) {
                    Range range = new Range();
                    range.start = pos;
                    range.end = i - 1;
                    rangeListX.add(range);
                    pos = -1;
                }
            }
        }
        if (pos != -1) {
            Range range = new Range();
            range.start = pos;
            range.end = width - 2;
            rangeListX.add(range);
        }
        for (Range range : rangeListX) {
            System.out.println("(" + range.start + "," + range.end + ")");
        }

        List<Range> rangeListY = new ArrayList<Range>();

        pos = -1;
        for (int i = 1; i < height - 1; i++) {
            int color = bitmap.getPixel(0, i);
            int alpha = Color.alpha(color);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            if (alpha == 255 && red == 0 && green == 0 && blue == 0) {
                if (pos == -1) {
                    pos = i - 1;
                }
            } else {
                if (pos != -1) {
                    Range range = new Range();
                    range.start = pos;
                    range.end = i - 1;
                    rangeListY.add(range);
                    pos = -1;
                }
            }

        }
        if (pos != -1) {
            Range range = new Range();
            range.start = pos;
            range.end = height - 2;
            rangeListY.add(range);
        }
        for (Range range : rangeListY) {
            System.out.println("(" + range.start + "," + range.end + ")");
        }

        RangeLists rangeLists = new RangeLists();
        rangeLists.rangeListX = rangeListX;
        rangeLists.rangeListY = rangeListY;

        return rangeLists;
    }

    public static Bitmap trimBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = Bitmap.createBitmap(bitmap, 1, 1, width - 2, height - 2);

        return result;

    }

    public static Bitmap loadBitmap(File file) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            return BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (Exception e) {

            }
        }
        return null;
    }

    public static String getDensityPostfix(Resources res) {
        String result = null;
        switch (res.getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                result = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                result = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                result = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                result = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                result = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                result = "xxxhdpi";
                break;
        }
        return result;
    }
}