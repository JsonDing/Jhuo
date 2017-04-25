package com.yunma.jhuo.m;

import com.yunma.widget.PullRefreshLayout;

/**
 * Created by santa on 16/6/24.
 */
public interface PullHandler {
    void onRefreshBegin(final PullRefreshLayout layout);

    void onRefreshFinshed();

}
