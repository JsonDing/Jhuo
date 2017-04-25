package com.yunma.jhuo.p;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.DeleteRecipientImpl;
import com.yunma.jhuo.m.DeleteRecipientInteface.*;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class DeleteRecipientPre {
    private DeleteRecipientModel mModel;
    private DeleteRecipientView mView;

    public DeleteRecipientPre (DeleteRecipientView mView) {
        this.mView = mView;
        this.mModel = new DeleteRecipientImpl();
    }

    public void deleteLocation(){
        mModel.deleteRecipient(mView.getContext(), mView.getDeleteLocationBean(), new OnListener() {

            @Override
            public void onDeltetLocation(SuccessResultBean successResultBean, String msg) {
                mView.toShowDeleteResult(successResultBean,msg);
            }
        });
    }
}
