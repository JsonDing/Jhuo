package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.CancleOrderInterface;
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

public class CancleOrderImpl implements CancleOrderInterface.CancleOrderModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void cancleOrder(final Context mContext, String id,
                            final CancleOrderInterface.CancleOrderListener cancleOrderListener) {
        RequestParams params = new RequestParams(ConUtils.ORDER_DEL);
        final DelBean delBean = new DelBean();
        delBean.setToken(SPUtils.getToken(mContext));
        delBean.setIds(id);
        String strBodyContent = new Gson().toJson(delBean);
        LogUtils.log("删除订单请求: ------------>" + strBodyContent);
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
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            cancleOrderListener.cancleOrderListener(null,"数据解析出错");
                            return;
                        }
                        cancleOrderListener.cancleOrderListener(resultBean,resultBean.getSuccess());
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            cancleOrderListener.cancleOrderListener(null,"数据解析出错");
                            return;
                        }
                        cancleOrderListener.cancleOrderListener(null,failedResultBean.getFailed().getErrorMsg());
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
                    cancleOrderListener.cancleOrderListener(null,"网络出异常");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    cancleOrderListener.cancleOrderListener(null,"服务器未响应，请稍后再试");
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
                      LogUtils.log("删除订单: " + result);
                }
            }
        });
    }

    public class DelBean{

        private String token;
        private String ids;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }
    }
}
