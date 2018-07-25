package com.yunma.jhuo.m;

import com.yunma.bean.SelfGoodsListBean;

/**
 * Created on 2016-12-30
 *
 * @author Json.
 */

public interface OnBuyClick {
    void onBuyClickListener(int position,SelfGoodsListBean mBean);
    void onLookGoodDetial(int goodId, int itemId, SelfGoodsListBean mBean);
}
