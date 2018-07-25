package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.OrderWaitToPayInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToPayImpl implements OrderWaitToPayModel {
    private OrderUnPayResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void orderUnPay(final Context mContext,String size,String page,
                           final OrderWaitToPayListener orderWaitToPayListener) {
        RequestParams params = new RequestParams(ConUtils.ORDER_UNPAY);
        final UnPayOrderBean unPayOrderBean = new UnPayOrderBean();
        unPayOrderBean.setToken(SPUtils.getToken(mContext));
        unPayOrderBean.setSize(size);
        unPayOrderBean.setPage(page);
        String strBodyContent = new Gson().toJson(unPayOrderBean);
        LogUtils.json("未付款订单请求: ------------>" + strBodyContent);
        params.setAsJsonContent(true);
        params.setBodyContent(strBodyContent);
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
                            resultBean = GsonUtils.GsonToBean(result,
                                    OrderUnPayResultBean.class);
                        } catch (Exception e) {
                            orderWaitToPayListener.orderWaitToPayListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }

                        if(resultBean.getSuccess().getList()!=null){
                            orderWaitToPayListener.orderWaitToPayListener(resultBean,"获取成功");
                        }else{
                            orderWaitToPayListener.orderWaitToPayListener(null,"暂无待支付订单");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            orderWaitToPayListener.orderWaitToPayListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        orderWaitToPayListener.orderWaitToPayListener(null,
                                failedResultBean.getFailed().getErrorMsg());
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
                    orderWaitToPayListener.orderWaitToPayListener(null,"服务器未响应!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    orderWaitToPayListener.orderWaitToPayListener(null,"服务器未响应，请稍后再试");
                    LogUtils.json("-----------> " + ex.getMessage() + "\n" + ex.getCause() );
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
                    LogUtils.json("未付款订单: " + result);
                }
            }
        });
    }

    private class UnPayOrderBean{
        private String token;
        private String size;
        private String page;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
