package com.yunma.widget;

import android.content.Context;
import android.support.v7.widget.*;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Json on 16/6/26.
 */
public class NestVerticalListView extends LinearLayout {
    private MyRecyclerView mSubListView;
    private LinearLayoutManager mLayoutManager;
    private boolean move = false;

    public NestVerticalListView(Context context) {
        super(context);
        init(context);
    }

    public NestVerticalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestVerticalListView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
/*
    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE && mRadioGroup.getCheckedRadioButtonId() == R.id.smoothScroll){
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move && mRadioGroup.getCheckedRadioButtonId() == R.id.scroll){
                move = false;
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    }*/
}


