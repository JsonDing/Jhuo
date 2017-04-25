package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class DeleteRecipientInteface {
    public interface DeleteRecipientModel{
        void deleteRecipient(Context mContext, DeleteLocationBean deleteLocationBean,
                            OnListener onListener);
    }

    public interface DeleteRecipientView{
        Context getContext();
        DeleteLocationBean getDeleteLocationBean();
        void toShowDeleteResult(SuccessResultBean successResultBean,String msg);
    }

    public interface OnListener{
        void onDeltetLocation(SuccessResultBean successResultBean, String msg);
    }
}
