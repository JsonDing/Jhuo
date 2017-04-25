package com.yunma.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by Json on 2016/12/29.
 */

public class MyRecyclerView extends RecyclerView {
    private Context context;
    public MyRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //每一条数据都是一个Map
        switch (state){
            case SCROLL_STATE_FLING:
             //   Log.i("Main","用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                Glide.with(context).pauseRequests();
                //刷新
                break;
            case SCROLL_STATE_IDLE:
             //   Log.i("Main", "视图已经停止滑动");
                Glide.with(context).resumeRequests();
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
              //  Log.i("Main","手指没有离开屏幕，视图正在滑动");
                Glide.with(context).resumeRequests();
                break;
        }
    }
}
