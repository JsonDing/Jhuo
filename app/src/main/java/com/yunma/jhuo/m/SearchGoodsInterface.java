package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created by Json on 2017/3/2.
 */

public class SearchGoodsInterface {

    /******************************************************************/
    public interface SearchGoodsByCodeModel {
        void searchByCode(Context mContext,String code,OnSearchGoodsListener onSearchGoodsListener);
    }

    public interface SearchGoodsView{
        void showSearchInfos(GoodsInfoResultBean resultBean, String msg);
    }

    public interface OnSearchGoodsListener{
        void onSearchListener(GoodsInfoResultBean resultBean, String msg);
    }

    /******************************************************************/

    public interface SearchGoodsByTypeModel {
        void searchByType(Context mContext,String type,String size,int pages,
                          OnSearchGoodsByTypeListener onSearchGoodsListener);
    }

    public interface SearchGoodsByTypeView{
        Context getContext();
        void showSearchInfosByType(GoodsInfoResultBean resultBean, String msg);
    }

    public interface OnSearchGoodsByTypeListener{
        void onSearchListener(GoodsInfoResultBean resultBean, String msg);
    }

    /******************************************************************/

    public interface SearchSelfGoodsByTypeModel {
        void searchSelfByType(Context mContext,String type,String size,int pages,
                          OnSearchSelfGoodsByTypeListener onSearchSelfGoodsListener);
    }

    public interface SearchSelfGoodsByTypeView{
        Context getContext();
        void showSearchInfosByType(GetSelfGoodsResultBean resultBean, String msg);
    }

    public interface OnSearchSelfGoodsByTypeListener{
        void onSearchListener(GetSelfGoodsResultBean resultBean, String msg);
    }

    /******************************************************************/

    public interface SearchSelfGoodsByCodeModel {
        void searchByCode(Context mContext,String code,String numbers,OnSearchSelfGoodsListener onSearchGoodsListener);
    }

    public interface SearchSelfGoodsView{
        void showSearchInfos(GetSelfGoodsResultBean resultBean, String msg);
    }

    public interface OnSearchSelfGoodsListener{
        void onSearchListener(GetSelfGoodsResultBean resultBean, String msg);
    }
}
