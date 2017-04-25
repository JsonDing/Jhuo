package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ALiPayInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class ALiPayImpl implements ALiPayModel {
    private SuccessResultBean successResultBean;
    private FailedResultBean failedResultBean;
    @Override
    public void getPayInfos(final Context mContext, String orderId,
                            final OnALiPayListener onALiPayListener) {
        RequestParams params = new RequestParams(ConUtils.GET_PAY_ORDER_INFO);
        final ALiPayBean  aLiPayBean = new ALiPayBean();
        aLiPayBean.setToken(SPUtils.getToken(mContext));
        aLiPayBean.setOid(orderId);
        Gson gson = new Gson();
        String strLogin = gson.toJson(aLiPayBean);
        LogUtils.log("params: ------------> " + strLogin);
        params.setAsJsonContent(true);
        params.setBodyContent(strLogin);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            successResultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onALiPayListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onALiPayListener.onListener(successResultBean,"获取成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onALiPayListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onALiPayListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onALiPayListener.onListener(null,"网络出错!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onALiPayListener.onListener(null,"服务器错误");
                    LogUtils.log("-----------> " + ex.getMessage() + "\n" + ex.getCause());
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
                    LogUtils.log("ALiPayInfos result: " + result);
                }
            }
        });

    }

    public class ALiPayBean{

        /**
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjYxLCJpYXQiOjE0ODY5Njg4NTUyMjAsImV4dCI6MTQ4ODQ0MDA4NDE0OH0.sfXgIdbkuul11qexU1pHCM9D88wwZZYZxGMKOyGVqvY
         * oid : 23482
         */

        private String token;
        private String oid;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }
    }
}
