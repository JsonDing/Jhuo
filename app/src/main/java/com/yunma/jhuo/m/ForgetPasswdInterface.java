package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created by Json on 2017/2/2.
 */

public class ForgetPasswdInterface {
    public interface ForgetPasswdView {
        void showModifyInfos (SuccessResultBean successResultBean,String msg);
    }

    public interface ForgetPasswdModel {
        void modifyPassWd(Context mContext,String phoneNumber,String passwd,String code,
                          OnForgetPassWdListener onModifyPassWdListener);
    }

    public interface OnForgetPassWdListener {
        void modify(SuccessResultBean successResultBean, String msg);
    }

}
