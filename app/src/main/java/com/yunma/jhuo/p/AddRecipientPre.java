package com.yunma.jhuo.p;

import com.yunma.bean.*;
import com.yunma.jhuo.i.AddRecipientImpl;
import com.yunma.jhuo.m.AddRecipientface.*;

/**
 * Created on 2017-01-02
 *
 * @author Json.
 */

public class AddRecipientPre {
    private AddLocationModel mModel;
    private AddLocationView mView;

    public AddRecipientPre(AddLocationView mView) {
        this.mView = mView;
        this.mModel = new AddRecipientImpl();
    }

    public void addLocition(){
        mModel.addRecipient(mView.getContext(), mView.getAddAddressBean(), new OnListener() {
            @Override
            public void onAddLocation(AddAddressResultBean resultBean, String msg) {
                mView.toShowAddResult(resultBean,msg);
            }
        });

    }


}
