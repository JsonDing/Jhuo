package com.yunma.jhuo.i;

import android.util.Log;

import com.yunma.jhuo.m.PhoneNumberVerificationInterFace.CheckCorrentListener;
import com.yunma.jhuo.m.PhoneNumberVerificationInterFace.PhoneMumberModel;
import com.yunma.utils.LogUtils;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Json on 2017/1/20.
 */

@SuppressWarnings("unchecked")
public class PhoneNumberVerificationImpl implements PhoneMumberModel {

    private String number;
    private CheckCorrentListener isCorrect;
    @Override
    public void getVerificationCode(String number) {
        SMSSDK.getVerificationCode("+86", number);
    }

    @Override
    public void PhoneNumberVerification(String number,String code,CheckCorrentListener isCorrect) {
        this.number = number;
        this.isCorrect = isCorrect;
        SMSSDK.registerEventHandler(eh);
        SMSSDK.submitVerificationCode("+86", number, code);//国家号，手机号码，验证码
    }

    private EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                //提交验证码成功,如果验证成功会在data里返回数据。data数据类型为HashMap<number,code>
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Log.e("TAG", "提交验证码成功" + data.toString());
                    HashMap<String, Object> mData = (HashMap<String, Object>) data;
                    String country = (String) mData.get("country");//返回的国家编号
                    String phone = (String) mData.get("phone");//返回用户注册的手机号
                    if (phone.equals(number)) {
                        isCorrect.showIsCorrent("验证成功");
                    } else {
                        isCorrect.showIsCorrent("验证失败");
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                    LogUtils.log("获取验证码成功");

                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            }
        }
    };

    @Override
    public void unregisterAllEventHandler() {
        //要在activity销毁时反注册，否侧会造成内存泄漏问题
        SMSSDK.unregisterAllEventHandler();
    }
}
