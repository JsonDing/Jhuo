package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.AdInfoResultBean;
import com.yunma.bean.BannerResultBean;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.bean.GoodsRemindResultBean;
import com.yunma.bean.IssueBean;
import com.yunma.bean.ModifySelfResultBean;
import com.yunma.bean.ModifyYunmasBean;
import com.yunma.bean.NewModSelfGoodsBean;
import com.yunma.bean.GoodsRemindResultBean.SuccessBean.NewRemindsBean;
import com.yunma.bean.PublishGoodsBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.bean.YunmasBeanResult;

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
                                  OnGetSelfGoodsListener onGetSelfGoodsListener);
    }

    public interface GetSelfGoodsView {
        void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg);
    }

    public interface OnGetSelfGoodsListener {
        void onGetGoodsListener(GetSelfGoodsResultBean resualtBean, String msg);
    }

    /****************************************************
     *                      获取Banner
     ****************************************************/
    public interface GetBannerView {
        void showNewsBanner(BannerResultBean bannerResultBean, String msg);
    }

    public interface GetBannerModel {
        void getNewsBanner(Context mContext,OnBananerListener onBananerListener);
    }

    public interface OnBananerListener{
        void onNewsBannerListener(BannerResultBean bannerResultBean, String msg);
    }

    /****************************************************
     *                      获取Ads
     ****************************************************/
    public interface GetAdsModel {
        void getAd(Context mContext,OnGetAdListener onGetAdListener);
    }

    public interface GetAdsView {
        void showAdsInfo(AdInfoResultBean resultBean, String msg);
    }

    public interface OnGetAdListener{
        void onGetAdListener(AdInfoResultBean resultBean, String msg);
    }

    /****************************************************
     *                     获取自仓上新
     ****************************************************/
    public interface GetIssueView {
        void showIssueGoods(GetSelfGoodsResultBean resultBean, String msg);
    }

    public interface GetSoldOutView {
        void showSoldOutGoods(GetSelfGoodsResultBean resultBean, String msg);
    }

    /****************************************************
     *                     添加
     ****************************************************/

    public interface AddSelfGoodsModel{
        void addSelfGoods(Context mContext, PublishGoodsBean yunmasBean,AddSelfGoodsListener onListener);
    }

    public interface AddSelfGoodsView{
        void showAddSelfGoods(YunmasBeanResult resultBean, String msg);
    }

    public interface AddSelfGoodsListener{
        void addSelfGoodsListener(YunmasBeanResult resultBean, String msg);
    }

    /****************************************************
     *                     删除
     ****************************************************/
    public interface DelSelfGoodsModel{
        void delSelfGoods(Context mContext,String ids,DelSelfGoodsListener delSelfGoodsListener);
    }

    public interface DelSelfGoodsView{
        void showDelInfos(SuccessResultBean resultBean,String msg);
    }

    public interface DelSelfGoodsListener{
        void delSelfGoodsListene(SuccessResultBean resultBean,String msg);
    }

    /****************************************************
     *                     下架
     ****************************************************/
    public interface ShelveSelfGoodsModel{
        void shelveSelfGoods(Context mContext,IssueBean issueBean,ShelveSelfGoodsListener mListener);
    }

    public interface ShelveSelfGoodsView{
        void shelveSelfGoods(SuccessResultBean resultBean,String msg);
    }

    public interface ShelveSelfGoodsListener{
        void onShelveListener(SuccessResultBean resultBean,String msg);
    }

    /****************************************************
     *                     修改
     ****************************************************/
    public interface ModSelfGoodsModel{
        void modifySelfGoods(Context mContext ,NewModSelfGoodsBean newModSelfGoodsBean,
                             ModSelfGoodsListener modSelfGoodsListener);
        void modifySelfGoodsById(Context mContext ,ModifyYunmasBean modifyYunmasBean,
                                 ModSelfGoodsByIdListener modSelfGoodsListener);
    }

    public interface ModSelfGoodsView{
        void modSelfGoodsView(ModifySelfResultBean resultBean,String msg);
    }

    /****************************************************
     *                     修改自仓商品byID
     ****************************************************/

    public interface ModSelfGoodsByIdView{
        void modSelfGoodsByIdView(SuccessResultBean resultBean,String msg);
    }

    public interface ModSelfGoodsByIdListener{
        void onModifyByIdListener(SuccessResultBean resultBean,String msg);
    }

    public interface ModSelfGoodsListener{
        void onModifyListener(ModifySelfResultBean resultBean,String msg);
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
}

