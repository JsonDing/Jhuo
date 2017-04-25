package com.yunma.jhuo.p;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.ModifyRecipientImpl;
import com.yunma.jhuo.m.ModifyRecipientInterface.*;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class ModifyRecipientPre {
    private ModifyRecipientModel mModel;
    private ModifyRecipientView mView;

    public ModifyRecipientPre(ModifyRecipientView modifyRecipientView) {
        this.mView = modifyRecipientView;
        this.mModel = new ModifyRecipientImpl();
    }
    public void modifyLocation(){
        mModel.modifyRecipient(mView.getContext(), mView.getModifyAddressBean(), new OnListener() {

            @Override
            public void onModifyLocation(SuccessResultBean successResultBean, String msg) {
                mView.toShowModifyResult(successResultBean,msg);
            }

        });
    }
}
