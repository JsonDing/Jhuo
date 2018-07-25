package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.RegisterSuccessResultBean;
import com.yunma.jhuo.i.RegisterModelImpl;
import com.yunma.jhuo.m.RegisterInterFace.*;

/**
 * Created by Json on 2016-12-02.
 * @author Json
 */

public class RegisterPresenter {
    private RegisterModel registerModel;
    private RegisterView registerView;

    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        this.registerModel = new RegisterModelImpl();
    }

    public void register(Context mContext, String phone, String passwd,String code,String intro){
        registerModel.register(mContext,phone, passwd,code,intro, new OnRegisterListener() {
                    @Override
                    public void registerMesage(RegisterSuccessResultBean resultBean, String msg) {
                        registerView.onShowRegisterMsg(resultBean,msg);
                    }
                });
    }
}
