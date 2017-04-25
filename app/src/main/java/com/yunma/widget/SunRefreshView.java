package com.yunma.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.yunma.R;
import com.yunma.jhuo.m.PullHeader;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.ScreenUtils;


/**
 * Created by Json on 16/12/4.
 */
public class SunRefreshView extends RelativeLayout implements PullHeader {
    private SunView mSunView;
    private boolean isCanRefresh = false;
    private int sunSize;
    private float mDensity;
    private int mMaxHeight = 300;
    public SunRefreshView(Context mContext) {
        this(mContext, null);
    }

    public SunRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;
        final TypedArray array = context.
                obtainStyledAttributes(attrs, R.styleable.RefreshLayout, defStyleAttr,0);
        mMaxHeight = array.getDimensionPixelSize(
                R.styleable.RefreshLayout_maxheight, calPxFromDp(mMaxHeight));
        init(context);
    }

    public void init(Context context) {
        mSunView = new SunView(context);
        sunSize = DensityUtils.dp2px(context,30);
        mSunView.setSunColor(Color.parseColor("#E51F08"));
        mSunView.setmSize(sunSize);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mMaxHeight);
        layoutParams.setMargins(ScreenUtils.getScreenWidth(context)/4-sunSize,100,0,0);

        addView(mSunView,layoutParams);
    }

    public int calPxFromDp(int px){
        return (int)(px * mDensity);
    }

    @Override
    public boolean isMoveWithContent() {
        return true;
    }

    @Override
    public boolean isCanRefresh() {
        return isCanRefresh;
    }

    //准备刷新
    public void onUIReadyToRefresh() {
        if(null != mSunView) {
            mSunView.startAnimotion();
        }
    }

    //放弃刷新
    public void onUICancleRefresh() {
        if (null != mSunView) {
            mSunView.stopAnimotion();
        }
    }

    @Override
    public void onPullProgress(float percent, int status) {
        if (PullRefreshLayout.STATE_RELEASE == status){
            if (percent > 0.9f) {
                isCanRefresh = true;
            }
        }else {
            isCanRefresh = false;
        }
        if (percent >= 0.9f) {
            onUIReadyToRefresh();
        } else {
            onUICancleRefresh();
        }
    }

    @Override
    public void onRefreshComplete() {
        onUICancleRefresh();
    }
}
