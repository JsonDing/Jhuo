package com.yunma.jhuo.p;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.ModifyPassWdImpl;
import com.yunma.jhuo.m.ModifypassWdInterface.*;

/**
 * Created on 2017-02-15
 *
 * @author Json.
 */

public class ModifyPassWdPre {
    private ModifyPassWdModel mModel;
    private ModifypassWdView mView;

    public ModifyPassWdPre(ModifypassWdView mView) {
        this.mView = mView;
        this.mModel = new ModifyPassWdImpl();
    }

    public void modifyPassWd(){
        mModel.modifyPassWd(mView.getContext(), mView.getOriginalPasswd(), mView.getNewPassWd(),
                new OnModifyPassWdListener() {
                    @Override
                    public void onListener(SuccessResultBean successResultBean, String msg) {
                        mView.showModifyInfos(successResultBean,msg);
                    }
                });
    }
}
