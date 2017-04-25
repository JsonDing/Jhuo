package com.yunma.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.*;

import com.yunma.R;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * Created on 2017-01-20
 *
 * @author Json.
 */

public class CustomProgressDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int mResid;
    public CustomProgressDialog(Context mContext,String content, int id) {
        super(mContext);
        this.mLoadingTip = content;
        this.mResid = id;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

        mImageView.setBackgroundResource(mResid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();

            }
        });
        mLoadingTv.setText(mLoadingTip);
        JumpingBeans.with(mLoadingTv)
                .appendJumpingDots()
                .build();
    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mAnimation.start();
        super.onWindowFocusChanged(hasFocus);
    }*/

    private void initView() {
        setContentView(R.layout.progress_dialog);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }
}
