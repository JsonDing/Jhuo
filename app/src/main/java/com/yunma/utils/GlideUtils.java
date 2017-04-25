package com.yunma.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.yunma.R;

import java.io.File;

import jp.wasabeef.glide.transformations.*;

/**
 * Created by Json on 2016/12/29.
 */

public class GlideUtils {

    public static void glidNormle(Context context, ImageView imageView, String url){

        ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha( 0f );
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                fadeAnim.setDuration( 1500 );
                fadeAnim.start();
            }
        };
        Glide.with(context).load(url)//
                .placeholder(R.drawable.default_pic)//
                .error(R.drawable.default_pic)//
                .animate(animationObject)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    public static void glidNormleFast(Context context, ImageView imageView, String url){

        ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha( 0f );
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                fadeAnim.setDuration( 500 );
                fadeAnim.start();
            }
        };

        Glide.with(context).load(url)//
                .placeholder(R.drawable.default_pic)//
                .error(R.drawable.default_pic)//
                .centerCrop()
                .animate(animationObject)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    public static void glidLocalRes(Context context, ImageView imageView, String name){
        ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha( 0f );
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                fadeAnim.setDuration( 1000 );
                fadeAnim.start();
            }
        };

        File file = new  File(Environment.getExternalStorageDirectory(),name);
        Glide.with(context)
                .load(file)
                .asBitmap()
                .placeholder(R.drawable.default_pic)//
                .error(R.drawable.default_pic)//
                .animate(animationObject)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    public static void glidLocalDrawable(Context context, ImageView imageView, int drawableId){
        ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha( 0f );
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                fadeAnim.setDuration( 1000 );
                fadeAnim.start();
            }
        };
        Glide.with(context)
                .load(drawableId)
                .asBitmap()
                .placeholder(R.drawable.default_pic)//
                .error(R.drawable.default_pic)//
                .animate(animationObject)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    /**
     *
     * @param context
     * @param imageView
     * @param url
     */
    public  static void glidBanner(Context context, ImageView imageView, String url){

        ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(2000);
                fadeAnim.start();
            }
        };

        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.banner_blank)//
                .error(R.drawable.banner_blank)//
                .animate(animationObject)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);

    }




    //圆形裁剪
    public static void glidRoundedCorners(Context context, ImageView imageView, String url){
        Glide.with(context).load(url)//
                .placeholder(R.drawable.default_pic)//
                .bitmapTransform(new RoundedCornersTransformation(context,20,0,
                        RoundedCornersTransformation.CornerType.ALL))
                .error(R.drawable.default_pic)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    //圆角处理
    public static void glidCropCircle(Context context, ImageView imageView, String url){
        Glide.with(context).load(url)//
                .placeholder(R.drawable.default_pic)//
                .bitmapTransform(new CropCircleTransformation(context))
                .error(R.drawable.default_pic)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    //灰度处理
    public static void glidGrayscale(Context context, ImageView imageView, String url){
        Glide.with(context).load(url)//
                .placeholder(R.drawable.default_pic)//
                .bitmapTransform(new GrayscaleTransformation(context))
                .error(R.drawable.default_pic)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//BlurTransformation
                .into(imageView);
    }

    //毛玻璃处理
    public static void glidBlur(Context context, ImageView imageView, String url,int radius){
        Glide.with(context).load(url)//
                .placeholder(R.drawable.default_pic)//
                .bitmapTransform(new BlurTransformation(context,radius))
                .error(R.drawable.default_pic)//
                .diskCacheStrategy(DiskCacheStrategy.ALL)//
                .into(imageView);
    }

    public static void loadSDImgs(Context mContext,ImageView imageView,String url){
        ViewPropertyAnimation.Animator animationObject = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha( 0f );
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                fadeAnim.setDuration( 1200 );
                fadeAnim.start();
            }
        };
        Glide.with(mContext)
                .load(url)
                .animate(animationObject)
                .placeholder(R.drawable.default_pic)
                .error(R.drawable.default_pic)
                .centerCrop()
                .into(imageView);

    }


    public static void glidClearMemory(Context context){
        // 必须在UI线程中调用
        Glide.get(context).clearMemory();
    }

    public static void glidClearDisk(final Context context){
        // 必须在后台线程中调用，建议同时clearMemory()
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

}
