package com.yunma.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Json on 16/6/26.
 */
public class NestListView extends LinearLayout {
    public MyRecyclerView mSubListView;
    private LinearLayoutManager mLayoutManager;
    private boolean move = false;

    public NestListView(Context context) {
        super(context);
        init(context);
    }

    public NestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        setOrientation(VERTICAL);
        initListView(context);

    }

    public void initListView(Context context) {
        mSubListView = new MyRecyclerView(context);
        LayoutParams l = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mSubListView.setOverScrollMode(OVER_SCROLL_NEVER);
        mSubListView.setLayoutParams(l);
        mSubListView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSubListView.setLayoutManager(mLayoutManager);
        addView(mSubListView);
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        if (null != mSubListView) {
            mSubListView.setAdapter(adapter);
        }
    }
    public void smoothMoveToPosition(int n) {

        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem ){
            mSubListView.smoothScrollToPosition(n);
        }else if ( n <= lastItem ){
            int top = mSubListView.getChildAt(n - firstItem).getTop();
            mSubListView.smoothScrollBy(0, top);
        }else{
            mSubListView.smoothScrollToPosition(n);
            move = true;
        }

    }

    public void stopScroll(){
        mSubListView.stopScroll();
    }
}


