package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.SearchGoodsByCodeImpl;
import com.yunma.jhuo.m.SearchGoodsInterface.*;

/**
 * Created by Json on 2017/3/2.
 */

public class SearchGoodsByCodePre {
    private SearchGoodsByCodeModel mModel;
    private SearchGoodsView mView;

    public SearchGoodsByCodePre(SearchGoodsView mView) {
        this.mView = mView;
        this.mModel = new SearchGoodsByCodeImpl();
    }

    public void searchGoodsByCode(Context mContext,String number){
        mModel.searchByCode(mContext, number, new OnSearchGoodsListener() {
            @Override
            public void onSearchListener(GoodsInfoResultBean resultBean, String msg) {
                mView.showSearchInfos(resultBean,msg);
            }
        });
    }


}
