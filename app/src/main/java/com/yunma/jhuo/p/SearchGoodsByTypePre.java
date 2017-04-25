package com.yunma.jhuo.p;

import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.i.SearchGoodsByTypeImpl;
import com.yunma.jhuo.m.SearchGoodsInterface.*;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class SearchGoodsByTypePre {
    private SearchGoodsByTypeModel mModel;
    private SearchGoodsByTypeView mView;

    public SearchGoodsByTypePre(SearchGoodsByTypeView mView) {
        this.mView = mView;
        this.mModel = new SearchGoodsByTypeImpl();
    }

    public void searchGoodsByType(String type,String size,int pages){
        mModel.searchByType(mView.getContext(),type, size, pages,new OnSearchGoodsByTypeListener() {
            @Override
            public void onSearchListener(GoodsInfoResultBean resultBean, String msg) {
                mView.showSearchInfosByType(resultBean,msg);
            }
        });
    }
}
