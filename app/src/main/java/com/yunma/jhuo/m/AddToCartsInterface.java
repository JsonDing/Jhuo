package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.AddToCartsRequestBean;
import com.yunma.bean.SuccessResultBean;

/**
 * Created on 2017-04-24
 *
 * @author Json.
 */

public class AddToCartsInterface {
    public interface AddToCartsModel{
        void addToCarts(Context mContext, AddToCartsRequestBean cartsRequestBean,
                        OnAddToCartsListener onAddToCartsListener);
    }

    public interface AddToCartsView{
        void showAddCartsInfos(SuccessResultBean resultBean,String msg);
    }

    public interface OnAddToCartsListener{
        void onAddListener(SuccessResultBean resultBean,String msg);
    }
}
