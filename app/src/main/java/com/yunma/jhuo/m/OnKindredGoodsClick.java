package com.yunma.jhuo.m;

import com.yunma.bean.*;

/**
 * Created on 2016-12-30
 *
 * @author Json.
 */

public interface OnKindredGoodsClick {
    void onSeeGoodsDatialListener(int position, GoodsInfoResultBean.SuccessBean.ListBean listBean);
    void onSeeMoreListener(int position,String type);
    void onSeeSelfGoodsDatialListener(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean);
    void onSeeSelfMoreListener(int position,String type);
}
