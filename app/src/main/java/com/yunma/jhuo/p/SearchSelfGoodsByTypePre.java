package com.yunma.jhuo.p;

import com.yunma.bean.*;
import com.yunma.jhuo.i.SearchSelfGoodsByTypeImpl;
import com.yunma.jhuo.m.SearchGoodsInterface;

/**
 * Created on 2017-03-11
 *
 * @author Json.
 */

public class SearchSelfGoodsByTypePre {
    private SearchGoodsInterface.SearchSelfGoodsByTypeModel mModel;
    private SearchGoodsInterface.SearchSelfGoodsByTypeView mView;

    public SearchSelfGoodsByTypePre(SearchGoodsInterface.SearchSelfGoodsByTypeView mView) {
        this.mView = mView;
        this.mModel = new SearchSelfGoodsByTypeImpl();
    }

    public void searchSelfGoodsByType(String type,String size,int pages){
        mModel.searchSelfByType(mView.getContext(),type, size, pages,
                new SearchGoodsInterface.OnSearchSelfGoodsByTypeListener() {
            @Override
            public void onSearchListener(GetSelfGoodsResultBean resultBean, String msg) {
                mView.showSearchInfosByType(resultBean,msg);
            }
        });
    }
}
