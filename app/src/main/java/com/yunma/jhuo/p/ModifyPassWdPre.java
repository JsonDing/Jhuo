package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.ModifyPassWdImpl;
import com.yunma.jhuo.m.ModifypassWdInterface.ModifyPassWdModel;
import com.yunma.jhuo.m.ModifypassWdInterface.ModifypassWdView;
import com.yunma.jhuo.m.ModifypassWdInterface.OnModifyPassWdListener;

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

    public void modifyPassWd(Context context,String oldPasswd,String newPasswd){
        mModel.modifyPassWd(context, oldPasswd, newPasswd,
                new OnModifyPassWdListener() {
                    @Override
                    public void onListener(SuccessResultBean successResultBean, String msg) {
                        mView.showModifyInfos(successResultBean,msg);
                    }
                });
    }
}
