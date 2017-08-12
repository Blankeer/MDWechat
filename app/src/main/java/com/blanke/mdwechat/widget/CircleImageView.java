package com.blanke.mdwechat.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import static android.R.attr.path;

/**
 * Created by blanke on 2017/8/1.
 */

public class CircleImageView extends ImageView {
    private Paint paint;
    Path path = new Path();

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.RED);
//            paint.setColor(Color.TRANSPARENT);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//            path.setFillType(Path.FillType.INVERSE_WINDING);
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            int r = Math.min(w, h) / 2;
            Log.d("imageview", "r=" + r + ",w=" + w + ",h=" + h);
            path.addCircle(w / 2, h / 2, r, Path.Direction.CW);
        }
        canvas.clipPath(path);
        super.onDraw(canvas);
//        canvas.drawRect(0, 0, w, h, paint);
//        canvas.drawCircle(w / 2, h / 2, r, paint);
//        paint.setXfermode(null);
    }
}
