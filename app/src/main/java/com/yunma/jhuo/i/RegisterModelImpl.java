package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.RegisterInterFace.OnRegisterListener;
import com.yunma.jhuo.m.RegisterInterFace.RegisterModel;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2016-12-02.
 * @author Json
 */

public class RegisterModelImpl implements RegisterModel {
    private RegisterSuccessResultBean resultBean = null;
    private FailedResultBean failBean = null;

    @Override
    public void register(Context mContext,String phone,String passwd,
                          OnRegisterListener registerListener) {
        registerJHuo(mContext,phone,passwd,registerListener);
    }

    private void registerJHuo(final Context mContext, String phone, String passwd,
                              final OnRegisterListener registerListener) {
        RequestParams params = new RequestParams(ConUtils.USER_REGISTER);
        RegisterBean mBean = new RegisterBean();
        mBean.setTel(phone);
        mBean .setPass(MD5Utils.getMD5(passwd));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(mBean);
        params.setBodyContent(strLogin);
        params.setConnectTimeout(1000*10);
        x.http().post(params, new Callback.CommonCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.getObject(result,
                                    RegisterSuccessResultBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(mContext, "数据解析出错!");
                            return;
                        }
                        registerListener.registerMesage(resultBean,null);
                    }else{
                        try {
                            failBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(mContext, "数据解析出错!");
                            return;
                        }
                        registerListener.registerMesage(null,failBean.getFailed().getErrorMsg());
                    }


                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    LogUtils.log("responseCode: " + responseCode + "\n" + "responseMsg: " +
                            responseMsg + "\n" + "errorResult: " + errorResult);
                    registerListener.registerMesage(null,"网络异常，请稍后重试");
                }{
                    registerListener.registerMesage(null,"服务器异常，请稍后重试");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    LogUtils.log("注册 result: " + result);
                }
            }
        });
    }

}
