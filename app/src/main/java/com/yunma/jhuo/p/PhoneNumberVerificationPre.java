package com.yunma.jhuo.p;

import com.yunma.jhuo.i.PhoneNumberVerificationImpl;
import com.yunma.jhuo.m.PhoneNumberVerificationInterFace.*;

/**
 * Created by Json on 2017/1/20.
 */

public class PhoneNumberVerificationPre {
    private PhoneMumberView mView;
    private PhoneMumberModel mModel;

    public PhoneNumberVerificationPre(PhoneMumberView mView) {
        this.mView = mView;
        this.mModel = new PhoneNumberVerificationImpl();
    }

    public void getVerificationCode(String phoneNumber){
        mModel.getVerificationCode(phoneNumber);
    }

    public void verifyingPhoneNumber(String phoneNumber,String code){
        mModel.PhoneNumberVerification(phoneNumber,code,
                new CheckCorrentListener() {
            @Override
            public void showIsCorrent(String msg) {
                mView.showIsCorrent(msg);
            }
        });
    }

    public void unregisterAllEventHandler(){
        mModel.unregisterAllEventHandler();
    }
}
