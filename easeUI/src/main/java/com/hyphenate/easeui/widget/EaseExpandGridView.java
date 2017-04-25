package com.hyphenate.easeui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class EaseExpandGridView extends GridView {

    public EaseExpandGridView(Context context) {
        super(context);
    }

    public EaseExpandGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

}
