package com.yunma.jhuo.i;


import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GoodsRefundInterface;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2017/3/20.
 */

public class WriteExpressModelImpl implements GoodsRefundInterface.WriteExpressModel {
    private WriteExpressResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void toWriteExpress(final Context mContext, String serviceId, String expressName,
                               String expressCode, final GoodsRefundInterface.OnWriteExpressListener listener) {
        RequestParams params = new RequestParams(ConUtils.ADD_SERVICE_EXPRESS);
        final ExpressBean expressBean = new ExpressBean();
        expressBean.setToken(SPUtils.getToken(mContext));
        expressBean.setSid(serviceId);
        expressBean.setExpress(expressName);
        expressBean.setExpressnumber(expressCode);
        Gson gson = new Gson();
        String strExpressBean= gson.toJson(expressBean);
        LogUtils.json("strexpressBean: ------------>" + strExpressBean);
        params.setAsJsonContent(true);
        params.setBodyContent(strExpressBean);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;

                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.GsonToBean(result,
                                    WriteExpressResultBean.class);
                        } catch (Exception e) {
                            listener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        listener.onListener(resultBean,"物流添加成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            listener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        listener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                   listener.onListener(null,"网络异常...请稍后再试");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    listener.onListener(null,"服务器正忙...请稍后再试");
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
                 //   LogUtils.json("填写物流 result: --- 》" + result);
                }
            }
        });
    }

    public class ExpressBean{

        private String token;
        private String sid;
        private String express;
        private String expressnumber;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public String getExpressnumber() {
            return expressnumber;
        }

        public void setExpressnumber(String expressnumber) {
            this.expressnumber = expressnumber;
        }
    }
}
