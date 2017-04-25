package com.yunma.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ScrollView;

/**
 * Created by Json on 2017/3/11.
 */

public class CustomScrollView  extends ScrollView {

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    public CustomScrollView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    public CustomScrollView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}
