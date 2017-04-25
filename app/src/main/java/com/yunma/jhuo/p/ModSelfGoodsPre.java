package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.ModSelfGoodsModelImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-04-10
 *
 * @author Json.
 */

public class ModSelfGoodsPre {
    private SelfGoodsInterFace.ModSelfGoodsModel mModel;
    private SelfGoodsInterFace.ModSelfGoodsView mView;
    private SelfGoodsInterFace.ModSelfGoodsByIdView nView;

    public ModSelfGoodsPre(SelfGoodsInterFace.ModSelfGoodsView mView) {
        this.mView = mView;
        this.mModel = new  ModSelfGoodsModelImpl();
    }

    public ModSelfGoodsPre(SelfGoodsInterFace.ModSelfGoodsByIdView nView) {
        this.nView = nView;
        this.mModel = new  ModSelfGoodsModelImpl();
    }

    public void modifyRepertory(Context mContext , NewModSelfGoodsBean newModSelfGoodsBean){
        mModel.modifySelfGoods(mContext, newModSelfGoodsBean,
                new SelfGoodsInterFace.ModSelfGoodsListener() {
            @Override
            public void onModifyListener(ModifySelfResultBean resultBean, String msg) {
                mView.modSelfGoodsView(resultBean,msg);
            }
        });
    }

    public void modifyRepertoryById(Context mContext , ModifyYunmasBean modifyYunmasBean){
        mModel.modifySelfGoodsById(mContext, modifyYunmasBean,
                new SelfGoodsInterFace.ModSelfGoodsByIdListener() {
                    @Override
                    public void onModifyByIdListener(SuccessResultBean resultBean, String msg) {
                        nView.modSelfGoodsByIdView(resultBean,msg);
                    }

                });
    }
}
