package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.*;
import com.yunma.jhuo.m.ServiceInterface;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class GoodsServicePre {
    private ServiceInterface.AddServiceModel mModel;
    private ServiceInterface.AddServiceView mView;
    private ServiceInterface.GetServiceModel getModel;
    private ServiceInterface.GetServiceView getView;
    public GoodsServicePre(ServiceInterface.AddServiceView mView) {
        this.mView = mView;
        this.mModel = new AddServiceImpl();
    }

    public GoodsServicePre(ServiceInterface.GetServiceView getView) {
        this.getView = getView;
        this.getModel = new GetServiceModelImpl();
    }

    public void addToService(Context mContext, UpLoadServiceBean requstBean){
        mModel.toAddService(mContext, requstBean, new ServiceInterface.OnAddServiceListener() {
            @Override
            public void toShowAddInfos(SuccessResultBean resultBean,String msg) {
                mView.toShowAddInfos( resultBean, msg);
            }
        });
    }

    public void getService(Context mContext,String refund){
        getModel.toGetService(mContext,refund, new ServiceInterface.OnGetServiceListener() {

            @Override
            public void toShowGetInfos(ServiceResultBean resultBean, String msg) {
                getView.toShowGetInfos(resultBean,msg);
            }
        });
    }
}
