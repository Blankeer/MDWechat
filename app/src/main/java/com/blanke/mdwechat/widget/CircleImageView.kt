package com.blanke.mdwechat.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView

/**
 * Created by blanke on 2017/8/1.
 */

class CircleImageView : ImageView {
    private var paint: Paint? = null
    internal var path = Path()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onDraw(canvas: Canvas) {
        if (paint == null) {
            paint = Paint()
            paint!!.color = Color.RED
            //            paint.setColor(Color.TRANSPARENT);
            //            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            //            path.setFillType(Path.FillType.INVERSE_WINDING);
            val w = measuredWidth
            val h = measuredHeight
            val r = Math.min(w, h) / 2
            Log.d("imageview", "r=$r,w=$w,h=$h")
            path.addCircle((w / 2).toFloat(), (h / 2).toFloat(), r.toFloat(), Path.Direction.CW)
        }
        canvas.clipPath(path)
        super.onDraw(canvas)
        //        canvas.drawRect(0, 0, w, h, paint);
        //        canvas.drawCircle(w / 2, h / 2, r, paint);
        //        paint.setXfermode(null);
    }
}
