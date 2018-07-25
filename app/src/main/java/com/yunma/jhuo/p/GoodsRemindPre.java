package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.GoodsRemindResultBean;
import com.yunma.bean.GoodsRemindResultBean.SuccessBean.NewRemindsBean;
import com.yunma.jhuo.i.GoodsRemindImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

import java.util.List;

/**
 * Created on 2017-04-20
 *
 * @author Json.
 */

public class GoodsRemindPre {
    private SelfGoodsInterFace.GoodsRemindView mView;
    private SelfGoodsInterFace.GoodsRemindModel mModel;

    public GoodsRemindPre(SelfGoodsInterFace.GoodsRemindView mView) {
        this.mView = mView;
        this.mModel = new GoodsRemindImpl();
    }

    public void getRemindGoods(Context mContext){
        mModel.getGoodsRemind(mContext,
                new SelfGoodsInterFace.GoodsRemindListener() {
                    @Override
                    public void onGoodsRemindListener(GoodsRemindResultBean resultBean,
                                                      List<NewRemindsBean> newRemindsBean, String msg) {
                        mView.showGoodsRemind(resultBean, newRemindsBean, msg);
                    }
                });
    }
}
