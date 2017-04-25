package com.yunma.jhuo.m;

import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;

/**
 * Created on 2016-12-30
 *
 * @author Json.
 */

public interface OnBuyClick {
    void onBuyClickListener(int position,ListBean mBean);
    void onLookGoodDetial(int goodId, int itemId, ListBean mBean);
}
