package com.yunma.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.*;
import android.view.View;

/**
 * Created on 2017-04-17
 *
 * @author Json.
 */

public class BlurUtils {
    public static void applyBlur(Context context, View view){
        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bmp = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        blur(bmp,view,context);
    }

    private static void blur(Bitmap bkg, View view,Context context) {
        float radius = 15;
        bkg = small(bkg);
        Bitmap bitmap = bkg.copy(bkg.getConfig(),true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        bitmap = big(bitmap);
        view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
        rs.destroy();
    }


    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(4f,4f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.25f,0.25f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
}
