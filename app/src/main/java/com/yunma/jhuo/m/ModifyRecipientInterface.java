package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class ModifyRecipientInterface {
    public interface ModifyRecipientModel{
        void modifyRecipient(Context mContext,ModifyAddressBean modifyAddressBean,
                            OnListener onListener);
    }

    public interface ModifyRecipientView{
        Context getContext();
        ModifyAddressBean getModifyAddressBean();
        void toShowModifyResult(SuccessResultBean successResultBean, String msg);
    }

    public interface OnListener{
        void onModifyLocation(SuccessResultBean successResultBean, String msg);
    }

}
