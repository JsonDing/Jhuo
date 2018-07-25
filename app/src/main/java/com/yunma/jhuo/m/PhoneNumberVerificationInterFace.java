package com.yunma.jhuo.m;

/**
 * Created by Json on 2017/1/19.
 */

public class PhoneNumberVerificationInterFace {

    public interface PhoneMumberView{
        void showIsCorrent(String msg);
    }

    public interface PhoneMumberModel{
        void getVerificationCode(String number);

        void PhoneNumberVerification(String number,String code,
                                     CheckCorrentListener checkCorrentListener);

        void unregisterAllEventHandler();
    }

    public interface CheckCorrentListener{
        void showIsCorrent(String msg);
    }
}
