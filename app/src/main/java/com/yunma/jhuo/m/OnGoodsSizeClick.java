package com.yunma.jhuo.m;

import android.widget.TextView;

import java.util.List;

/**
 * Created on 2016-12-30
 *
 * @author Json.
 */

public interface OnGoodsSizeClick {
    void onSizeClickListener(int position, TextView tv, List<String> mSize);
}
