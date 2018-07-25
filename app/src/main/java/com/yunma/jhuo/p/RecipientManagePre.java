package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.RecipientManageBean;
import com.yunma.jhuo.i.RecipientManageModelImpl;
import com.yunma.jhuo.m.RecipientManageInterface.*;

/**
 * Created by Json on 2017/2/8.
 */

public class RecipientManagePre {
    private RecipientManageView mView;
    private RecipientManageModel mModel;

    public RecipientManagePre(RecipientManageView mView) {
        this.mView = mView;
        this.mModel = new RecipientManageModelImpl();
    }

    public void queryRecipient(Context context){
        mModel.onQueryRecipient(context, new OnListener() {
            @Override
            public void quaryRecipientListener(RecipientManageBean bean, String msg) {
                mView.toShowRecipientManage(bean,msg);
            }
        });
    }
}
