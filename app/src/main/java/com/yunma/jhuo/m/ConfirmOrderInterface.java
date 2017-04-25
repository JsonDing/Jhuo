package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.RecipientManageBean.SuccessBean.*;

/**
 * Created on 2017-01-02
 *
 * @author Json.
 */

public class ConfirmOrderInterface {

    public interface ConfirmOrderView{
        Context getContext();
        void toShowDefaultAddress(ListBean listBean,String msg);
    }

    public interface ConfirmOrderModel{
        void getDefaultAddress(Context mContext,OnGetDefaultAddress onGetDefaultAddress);
    }

    public  interface OnGetDefaultAddress{
        void onShowDefaultAddress(ListBean listBean,String msg);
    }
}
