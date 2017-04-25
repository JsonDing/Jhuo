package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.ForgetPassWdImpl;
import com.yunma.jhuo.m.ForgetPasswdInterface.*;

/**
 * Created on 2016-12-06.
 * @author Json
 */

public class ForgetPassWdPre {
    private ForgetPasswdModel model;
    private ForgetPasswdView view;

    public ForgetPassWdPre(ForgetPasswdView modifyPasswdView) {
        this.view = modifyPasswdView;
        this.model = new ForgetPassWdImpl();
    }

    public void modifyPasswd(Context mContext,String PhoneNumber,String passwd){
        model.modifyPassWd(mContext,PhoneNumber,passwd,
                new OnForgetPassWdListener() {
                    @Override
                    public void modify(SuccessResultBean successResultBean,String msg) {
                    view.showModifyInfos(successResultBean,msg);
                    }
                });
    }
}
