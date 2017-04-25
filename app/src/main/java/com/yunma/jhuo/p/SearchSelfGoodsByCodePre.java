package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.SearchSelfGoodsByCodeImpl;
import com.yunma.jhuo.m.SearchGoodsInterface;

/**
 * Created on 2017-03-11
 *
 * @author Json.
 */

public class SearchSelfGoodsByCodePre{
    private SearchGoodsInterface.SearchSelfGoodsByCodeModel mModel;
    private SearchGoodsInterface.SearchSelfGoodsView mView;

    public SearchSelfGoodsByCodePre(SearchGoodsInterface.SearchSelfGoodsView mView) {
        this.mView = mView;
        this.mModel = new SearchSelfGoodsByCodeImpl();
    }

    public void searchSelfGoodsByCode(Context mContext,String number,String numbers){
        mModel.searchByCode(mContext, number,numbers,
                new SearchGoodsInterface.OnSearchSelfGoodsListener() {
            @Override
            public void onSearchListener(GetSelfGoodsResultBean resultBean, String msg) {
                mView.showSearchInfos(resultBean,msg);
            }
        });
    }
}
