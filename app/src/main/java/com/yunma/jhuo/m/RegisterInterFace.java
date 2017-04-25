package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-01-19
 *
 * @author Json.
 */

public class RegisterInterFace {

    public interface RegisterView {
        void onShowRegisterMsg(RegisterSuccessResultBean resultBean, String msg);
    }

    public interface RegisterModel {

        void register(Context mContext,String phone, String passwd, OnRegisterListener registerListener);
    }

    public interface OnRegisterListener {
        void registerMesage(RegisterSuccessResultBean resultBean, String msg);
    }

}
