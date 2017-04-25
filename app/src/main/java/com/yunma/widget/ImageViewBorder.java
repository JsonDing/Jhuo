package com.yunma.widget;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created on 2017-03-09
 *
 * @author Json.
 */

public class ImageViewBorder extends android.support.v7.widget.AppCompatImageView {
    private int co;
    private int borderwidth;
    public ImageViewBorder(Context context) {
        super(context);
    }

    public ImageViewBorder(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewBorder(Context context,@Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置颜色
    public void setColour(int color){
        co = color;
    }
    //设置边框宽度
    public void setBorderWidth(int width){

        borderwidth = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画边框
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        //设置边框颜色
        paint.setColor(co);
        paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(borderwidth);
        canvas.drawRect(rec, paint);
    }
}
