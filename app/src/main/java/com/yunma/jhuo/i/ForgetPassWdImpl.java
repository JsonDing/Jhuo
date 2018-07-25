package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ForgetPasswdInterface.ForgetPasswdModel;
import com.yunma.jhuo.m.ForgetPasswdInterface.OnForgetPassWdListener;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2016-12-02.
 * @author Json
 */

public class ForgetPassWdImpl implements ForgetPasswdModel {
    private FailedResultBean failedResultBean = null;
    private SuccessResultBean successResultBean = null;

    @Override
    public void modifyPassWd(final Context mContext, String phoneNumber, String passwd,String code,
                             final OnForgetPassWdListener onForgetPassWdListener) {
        RequestParams params = new RequestParams(ConUtils.FORGET_PASSWORD);
        ForgetPassWdBean  forgetPassWdBean = new ForgetPassWdBean();
        forgetPassWdBean.setTel(phoneNumber);
        forgetPassWdBean.setCode(code);
        forgetPassWdBean.setVersion("v2");
        forgetPassWdBean.setPass(MD5Utils.getMD5(passwd));
        Gson gson = new Gson();
        String strLogin = gson.toJson(forgetPassWdBean);
        params.setAsJsonContent(true);
        params.setBodyContent(strLogin);
        x.http().post(params, new Callback.CacheCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public boolean onCache(String result) {
                this.result = result;
                return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            successResultBean = GsonUtils.GsonToBean(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onForgetPassWdListener.modify(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onForgetPassWdListener.modify(successResultBean,successResultBean.getSuccess());
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onForgetPassWdListener.modify(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onForgetPassWdListener.modify(null, failedResultBean.getFailed().getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                if (ex instanceof HttpException) {
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    onForgetPassWdListener.modify(null,"网络异常!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onForgetPassWdListener.modify(null,"服务器未响应，请稍后再试");
                    LogUtils.json("-----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.showInfo(mContext,"cancelled");
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                 //   LogUtils.json("ForgetPassWdActivity result: " + result);
                }
            }
        });

    }
}
