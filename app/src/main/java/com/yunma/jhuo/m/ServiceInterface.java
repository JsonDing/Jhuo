package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class ServiceInterface {

    public interface GetServiceModel{
        void toGetService(Context mContext,String refund, OnGetServiceListener onGetServiceListener);
    }

    public interface GetServiceView{
        void toShowGetInfos(ServiceResultBean resultBean,String msg);
    }

    public interface OnGetServiceListener{
        void toShowGetInfos(ServiceResultBean resultBean,String msg);
    }



    public interface AddServiceModel{
        void toAddService(Context mContext, UpLoadServiceBean requstBean,
                          OnAddServiceListener onAddServiceListener);
    }

    public interface AddServiceView{
        void toShowAddInfos(SuccessResultBean resultBean,String msg);
    }

    public interface OnAddServiceListener{
        void toShowAddInfos(SuccessResultBean resultBean,String msg);
    }
}
