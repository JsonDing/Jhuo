package com.yunma.widget;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created on 2017-03-21
 *
 * @author Json.
 */

public class URLDrawable extends BitmapDrawable {
    protected Bitmap bitmap;

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, getPaint());
        }
    }
}
