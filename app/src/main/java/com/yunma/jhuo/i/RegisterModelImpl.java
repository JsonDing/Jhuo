package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.RegisterBean;
import com.yunma.bean.RegisterSuccessResultBean;
import com.yunma.jhuo.m.RegisterInterFace.OnRegisterListener;
import com.yunma.jhuo.m.RegisterInterFace.RegisterModel;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.MD5Utils;

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
    public void register(final Context mContext, String phone, String passwd, String code, String intro,
                         final OnRegisterListener registerListener) {
        RequestParams params = new RequestParams(ConUtils.USER_REGISTER);
        RegisterBean mBean = new RegisterBean();
        mBean.setTel(phone);
        mBean .setPass(MD5Utils.getMD5(passwd));
        mBean.setCode(code);
        mBean.setSales(intro);
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

                            resultBean = GsonUtils.GsonToBean(result,
                                    RegisterSuccessResultBean.class);
                            registerListener.registerMesage(resultBean,null);
                        } catch (Exception e) {
                            registerListener.registerMesage(null,"数据解析出错");
                        }
                    }else{
                        try {
                            failBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                            registerListener.registerMesage(null,failBean.getFailed().getErrorMsg());
                        } catch (Exception e) {
                            registerListener.registerMesage(null,"数据解析出错");
                        }
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
                    LogUtils.json("responseCode: " + responseCode + "\n" + "responseMsg: " +
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
                 //   LogUtils.json("注册 result: " + result);
                }
            }
        });
    }

}
