package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.OrderWaitToSendInterface.*;
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

public class OrderWaitToSendImpl implements OrderWaitToSendModel {
    private OrderUnPayResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void orderUnSend(final Context mContext,String nums,String page,
                           final OrderWaitToSendListener orderWaitToSendListener) {
        RequestParams params = new RequestParams(ConUtils.ORDER_PAY_UNSEND);
        final UnSendOrderBean unSendOrderBean = new UnSendOrderBean();
        unSendOrderBean.setToken(SPUtils.getToken(mContext));
        unSendOrderBean.setSize(nums);
        unSendOrderBean.setPage(page);
        String strBodyContent = new Gson().toJson(unSendOrderBean);
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
                            orderWaitToSendListener.orderWaitToSendListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        orderWaitToSendListener.orderWaitToSendListener(resultBean,"获取成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            orderWaitToSendListener.orderWaitToSendListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        orderWaitToSendListener.orderWaitToSendListener(null,
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
                    orderWaitToSendListener.orderWaitToSendListener(null,"网络异常!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    orderWaitToSendListener.orderWaitToSendListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.json("未发货订单: " + result);
                }
            }
        });
    }

    private class UnSendOrderBean {
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
