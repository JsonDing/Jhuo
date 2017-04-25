package com.yunma.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yunma.utils.DensityUtils;

/**
 * Created by Json on 2017/3/9.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration  {
    private int space;

    public SpacesItemDecoration(Context context,int space) {
        this.space = DensityUtils.dp2px(context,space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = space;
    }
}
