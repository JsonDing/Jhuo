package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.GoodsInfoResultBean;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class GetGoodsInterface {
    public interface GetGoodsRecommendView {
        Context getContext();
        void showRecommendGoodsInfos(GoodsInfoResultBean resultBean, String msg);
    }

    public interface GetGoodsRecommendModel {
       void getRecommenGoods(Context mContext,String size,int nextPage,
                             OnGetGoodsRecommendListener onListener);
    }

    public interface OnGetGoodsRecommendListener {
        void onListener(GoodsInfoResultBean resultBean,String msg);
    }

    public interface GetGoodsHotView {
        Context getContext();
        void showHotGoodsInfos(GoodsInfoResultBean resultBean,String msg);
    }

    public interface GetGoodsHotModel {
        void getHotGoods(Context mContext,String size,int nextPage, OnGetGoodsHotListener onListener);
    }

    public interface OnGetGoodsHotListener {
        void onListener(GoodsInfoResultBean resultBean,String msg);
    }

    public interface GetGoodsNewView {
        Context getContext();
        void showNewGoodsInfos(GoodsInfoResultBean resultBean,String msg);
    }

    public interface GetGoodsNewModel {
        void getNewGoods(Context mContext,String size,int nextPage,OnGetGoodsNewListener onListener);
    }

    public interface OnGetGoodsNewListener {
        void onListener(GoodsInfoResultBean resultBean,String msg);
    }

}
