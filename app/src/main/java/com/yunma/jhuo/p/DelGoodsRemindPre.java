package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.DelGoodsRemindImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

import java.util.List;

/**
 * Created on 2017-04-20
 *
 * @author Json.
 */

public class DelGoodsRemindPre {
    private SelfGoodsInterFace.DelGoodsRemindModel mModel;
    private SelfGoodsInterFace.DelGoodsRemindView mView;

    public DelGoodsRemindPre(SelfGoodsInterFace.DelGoodsRemindView mView) {
        this.mView = mView;
        this.mModel = new DelGoodsRemindImpl();
    }

    public void addGoodsRemind(Context mContext, List<String> goodsIds){
        mModel.delGoodsRemind(mContext, goodsIds,
                new SelfGoodsInterFace.DelGoodsRemindListener() {
                    @Override
                    public void delGoodsRemindListener(SuccessResultBean resultBean, String msg) {
                        mView.showDelGoodsRemindInfos(resultBean,msg);
                    }
                });
    }
}
