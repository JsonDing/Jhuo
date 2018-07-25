package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created by Json on 2017/2/8.
 */

public class RecipientManageInterface {
    public interface RecipientManageView{
        void toShowRecipientManage(RecipientManageBean bean, String msg);
    }

    public interface RecipientManageModel{
        void onQueryRecipient(Context mContext, OnListener onListener);
    }

    public interface OnListener{
        void quaryRecipientListener(RecipientManageBean bean,String msg);
    }

    public interface OnModifyRecipient{
        void onModifyListener(int position,RecipientManageBean.SuccessBean.ListBean mBean);
    }
}
