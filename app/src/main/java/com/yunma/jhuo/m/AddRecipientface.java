package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-01-02
 *
 * @author Json.
 */

public class AddRecipientface {
    public interface AddLocationView{
        Context getContext();
        AddAddressBean getAddAddressBean();
        void toShowAddResult(AddAddressResultBean resultBean, String msg);
    }

    public interface AddLocationModel{
        void addRecipient(Context mContext,AddAddressBean addAddressBean,
                         OnListener onListener);
    }

    public interface OnListener{
        void onAddLocation(AddAddressResultBean resultBean, String msg);
    }
}
