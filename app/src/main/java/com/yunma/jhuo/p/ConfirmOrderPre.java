package com.yunma.jhuo.p;

import com.yunma.bean.*;
import com.yunma.jhuo.i.ConfirmOrderImpl;
import com.yunma.jhuo.m.ConfirmOrderInterface.*;

/**
 * Created on 2017-01-02
 *
 * @author Json.
 */

public class ConfirmOrderPre {
    private ConfirmOrderView mView;
    private ConfirmOrderModel mModel;
    public ConfirmOrderPre(ConfirmOrderView mView) {
        this.mView = mView;
        this.mModel = new ConfirmOrderImpl();
    }

    public void getReceiverInfo(){
        mModel.getDefaultAddress(mView.getContext(), new OnGetDefaultAddress() {

            @Override
            public void onShowDefaultAddress(RecipientManageBean.SuccessBean.ListBean listBean,
                                             String msg) {
                mView.toShowDefaultAddress(listBean,msg);
            }
        });
    }

}
