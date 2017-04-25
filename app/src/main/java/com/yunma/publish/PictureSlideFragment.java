package com.yunma.publish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.yunma.R;
import com.yunma.bean.PathBean;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Json on 2017/3/10.
 */

public class PictureSlideFragment extends Fragment {
    private String url;
    private PhotoViewAttacher mAttacher;
    private ImageView imageView;

    public static PictureSlideFragment newInstance(PathBean url) {
        PictureSlideFragment f = new PictureSlideFragment();

        Bundle args = new Bundle();
        args.putString("url", url.getImgsPath());
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments() != null ? getArguments().getString("url") : "http://www.zhagame.com/wp-content/uploads/2016/01/JarvanIV_6.jpg";

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_picture_slide,container,false);

        imageView= (ImageView) v.findViewById(R.id.iv_main_pic);
        mAttacher = new PhotoViewAttacher(imageView);//使用PhotoViewAttacher为图片添加支持缩放、平移的属性

        Glide.with(getActivity()).load(url).crossFade().into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mAttacher.update();
            }
        });
        return v;
    }
}
