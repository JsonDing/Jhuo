package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.AdInfoResultBean;
import com.yunma.bean.BannerResultBean;
import com.yunma.bean.BuyGradeBean;
import com.yunma.bean.BuySpecialGoodsResult;
import com.yunma.bean.GoodsBrandsResultBean;
import com.yunma.bean.GoodsRemindResultBean;
import com.yunma.bean.GoodsRemindResultBean.SuccessBean.NewRemindsBean;
import com.yunma.bean.GoodsSizeResultBean;
import com.yunma.bean.GoodsTypesBean;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.bean.SelfGoodsResultBean;
import com.yunma.bean.SuccessResultBean;

import java.util.List;

/**
 * Created on 2017-03-22
 *
 * @author Json.
 */

public class SelfGoodsInterFace {
    /****************************************************
     *                     获取自仓
     * nogoods为无货 sale正在出售 issue 待上架货品 before
     ****************************************************/
    public interface GetSelfGoodsModel {
        void getSpecialOfferGoods(Context mContext,String requestType,String size,int nextPage,
                                  String sort,String desc,
                                  OnGetSelfGoodsListener onGetSelfGoodsListener);
    }

    public interface GetSelfGoodsView {
        void showSpecialOfferGoods(SelfGoodsResultBean resultBean, String msg);
    }

    public interface OnGetSelfGoodsListener {
        void onGetGoodsListener(SelfGoodsResultBean resualtBean, String msg);
    }

    /****************************************************
     *                     获取自仓无货 -- type:nogoods
     ****************************************************/
    public interface GetSelfSaleOutModel {
        void getSelfSaleOutGoods(Context mContext,String size,int nextPage,
                                 OnSelfSaleOutListener onSelfSaleOutListener);
    }

    public interface GetSelfSaleOutView {
        void showSelfSaleOutGoods(SelfGoodsResultBean resultBean, String msg);
    }

    public interface OnSelfSaleOutListener {
        void onSelfSaleOutListener(SelfGoodsResultBean resultBean, String msg);
    }

    /****************************************************
     *                     获取自仓预售--根据日期显示 -- type:future
     ****************************************************/
    public interface GetFutureGoodsModel {
        void getFutureGoods(Context mContext,String size,int nextPage, String date,
                            OnFutureGoodsListener onFutureGoodsListener);
    }

    public interface GetFutureGoodsView{
        void showFutureGoods(SelfGoodsResultBean resultBean, String msg);
    }

    public interface OnFutureGoodsListener {
        void onFutureGoodsListener(SelfGoodsResultBean resultBean, String msg);
    }

    public interface GetTodayGoodsModel {
        void getTodayGoods(Context mContext,String size,int nextPage, String date,
                            OnFutureGoodsListener onFutureGoodsListener);
    }

    public interface GetTodayGoodsView{
        void showTodayGoods(SelfGoodsResultBean resultBean, String msg);
    }

    public interface GetTomorrowGoodsModel {
        void getTomorrowGoods(Context mContext,String size,int nextPage, String date,
                           OnFutureGoodsListener onFutureGoodsListener);
    }

    public interface GetTomorrowGoodsView{
        void showTomorrowGoods(SelfGoodsResultBean resultBean, String msg);
    }

    public interface GetBeforeGoodsModel {
        void getBeforeGoods(Context mContext,String size,int nextPage, String date,
                              OnFutureGoodsListener onFutureGoodsListener);
    }

    public interface GetBeforeGoodsView{
        void showBeforeGoods(SelfGoodsResultBean resultBean, String msg);
    }

    /****************************************************
     *                      获取闪屏页广告词、app版本
     ****************************************************/
    public interface GetSplashPageWordsView {
        void showNewsVersion(BannerResultBean bannerResultBean, String msg);
    }

    public interface GetSplashPageWordsModel {
        void getSplashPageWords(Context mContext,OnSplashPageWordsListener onSplashPageWordsListener);
    }

    public interface OnSplashPageWordsListener {
        void onNewsBannerListener(BannerResultBean bannerResultBean, String msg);
    }

    /****************************************************
     *                      获取顶部banner
     ****************************************************/
    public interface GetBannersModel {
        void getBanners(Context mContext,String version,OnGetBannerListener onGetBannerListener);
    }

    public interface GetBannersView {
        void showTopBannerInfo(AdInfoResultBean resultBean, String msg);
    }

    public interface OnGetBannerListener {
        void onGetBannerListener(AdInfoResultBean resultBean, String msg);
    }

    /****************************************************
     *                     获取到货提醒
     ****************************************************/
    public interface GoodsRemindView{
        void showGoodsRemind(GoodsRemindResultBean resultBean,
                             List<NewRemindsBean> newRemindsBean, String msg);
    }

    public interface GoodsRemindListener{
        void onGoodsRemindListener(GoodsRemindResultBean resultBean,
                                   List<NewRemindsBean> newRemindsBean,String msg);
    }

    public interface GoodsRemindModel{
        void getGoodsRemind(Context mContext,GoodsRemindListener goodsRemindListener);
    }

    /****************************************************
     *                     添加到货提醒
     ****************************************************/
    public interface AddSelfGoodsRemindModel{
        void addSelfGoodsRemind(Context mContext,String goodsNumbers,
                                AddSelfGoodsRemindListener mListener);
    }

    public interface AddSelfGoodsRemindView{
        void showSelfGoodsRemindInfos(SuccessResultBean resultBean,String msg);
    }

    public interface AddSelfGoodsRemindListener{
        void addSelfGoodsRemindListener(SuccessResultBean resultBean,String msg);
    }

    /****************************************************
     *                     删除到货提醒
     ****************************************************/
    public interface DelGoodsRemindModel {
        void delGoodsRemind(Context mContext,List<String> goodsIds,
                                DelGoodsRemindListener mListener);
    }

    public interface DelGoodsRemindView{
        void showDelGoodsRemindInfos(SuccessResultBean resultBean,String msg);
    }

    public interface DelGoodsRemindListener {
        void delGoodsRemindListener(SuccessResultBean resultBean,String msg);
    }

    /***************************************************
     *                     获取商品类型
     ****************************************************/
    public interface GainGoodsTypeModel{
        void gainGoodsType(Context context, OnGainGoodsTypeListener onGainGoodsTypeListener);
    }

    public interface GainGoodsTypeView{
        void showGoodsType(GoodsTypesBean resultBean, String msg);
    }

    public interface OnGainGoodsTypeListener{
        void setGoodsType(GoodsTypesBean resultBean, String msg);
    }

    /***************************************************
     *                     获取商品品牌
     ****************************************************/
    public interface GainGoodsBrandModel{
        void gainGoodsBrand(Context context, OnGainGoodsBrandListener onGainGoodsBrandListener);
    }

    public interface GainGoodsBrandView{
        void showGoodsBrand(GoodsBrandsResultBean resultBean, String msg);
    }

    public interface OnGainGoodsBrandListener{
        void setGoodsBrand(GoodsBrandsResultBean resultBean, String msg);
    }

    /***************************************************
     *                     获取商品尺码
     ****************************************************/
    public interface GainGoodsSizeModel{
        void gainGoodsSize(Context context, OnGainGoodsSizeListener onListener);
    }

    public interface GainGoodsSizeView{
        void showGoodsSize(GoodsSizeResultBean resultBean, String msg);
    }

    public interface OnGainGoodsSizeListener{
        void setGoodsSize(GoodsSizeResultBean resultBean, String msg);
    }

    /***************************************************
     *                     按需求搜索商品
     ****************************************************/
    public interface SearchGoodsModel {
        void searchSelfGoods(Context context,String page,String amount,String type,String brand, String number,
                             String sPrice,String ePrice,String sex,String stock,String name,String sort,String desc,
                             String stocksize,String spprice,OnSearchGoodsListener onSearchGoodsListener);
        void searchRationsGoods(Context context,String size, String page, String repoType,OnSearchGoodsListener onSearchGoodsListener);
    }

    public interface SearchGoodsView {
        void showSearchInfos(SelfGoodsResultBean resultBean, String msg);
    }

    public interface OnSearchGoodsListener {
        void onSearchListener(SelfGoodsResultBean resultBean, String msg);
    }

    /***************************************************
     *                     根据ID搜索商品详情
     ****************************************************/
    public interface SearchGoodsDetialModel {
        void searchGoodsDetials(Context context,String id,OnSearchGoodsDetialsListener mListener);
    }

    public interface SearchGoodsDetialView {
        void showGoodsDetials(SelfGoodsListBean resultBean, String msg);
    }

    public interface OnSearchGoodsDetialsListener {
        void onSearchDetialsListener(SelfGoodsListBean resultBean, String msg);
    }

    /***************************************************
     *                     获取特殊货品
     ****************************************************/

    public interface SpecialGoodsModel{
        void getSpecialGoods(OnGetSpecialGoodsListener onListener);
    }

    public interface SpecialGoodsView{
        void showSpecialGoods(SelfGoodsResultBean result, String msg);
    }

    public interface OnGetSpecialGoodsListener{
        void onSpecialGoodsListener(SelfGoodsResultBean result, String msg);
    }

    /***************************************************
     *                     购买特殊货品
     ****************************************************/

    public interface BuySpecialGoodsModel{
        void getSpecialGoods(BuyGradeBean mBean, OnBuySpecialGoodsListener onListener);
    }

    public interface BuySpecialGoodsView{
        void showSpecialGoods(BuySpecialGoodsResult resultBean, String msg);
    }

    public interface OnBuySpecialGoodsListener{
        void onSpecialGoodsListener(BuySpecialGoodsResult resultBean, String msg);
    }
}

