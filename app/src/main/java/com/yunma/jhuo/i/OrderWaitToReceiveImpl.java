package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.OrderWaitToReceiveInterface.*;
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

public class OrderWaitToReceiveImpl implements OrderWaitToReceiveModel {
    private OrderUnPayResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void orderUnReceive(final Context mContext,
                               final OrderWaitToReceiveListener orderListener) {
        RequestParams params = new RequestParams(ConUtils.ORDER_SEND);
        final UnReciveyOrderBean unReciveyOrderBean = new UnReciveyOrderBean();
        unReciveyOrderBean.setToken(SPUtils.getToken(mContext));
        unReciveyOrderBean.setSize("10");
        unReciveyOrderBean.setPage("1");
        String strBodyContent = new Gson().toJson(unReciveyOrderBean);
        LogUtils.log("未签收订单请求: ------------>" + strBodyContent);
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
                            resultBean = GsonUtils.getObject(result,
                                    OrderUnPayResultBean.class);
                        } catch (Exception e) {
                            orderListener.orderWaitToReceiveListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        orderListener.orderWaitToReceiveListener(resultBean,"获取成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            orderListener.orderWaitToReceiveListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        orderListener.orderWaitToReceiveListener(null,
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
                    orderListener.orderWaitToReceiveListener(null,"网络出错!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    orderListener.orderWaitToReceiveListener(null,"服务器错误");
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
                    LogUtils.log("未签收订单: " + result);
                }
            }
        });
    }

    private class UnReciveyOrderBean {
        private String token;
        private String size;

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

        private String page;
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
