package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class DelRefundImpl implements GoodsRefundInterface.DelRefundModel {
    private SuccessResultBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    public void delRefund(final Context mContext, String id,
                          final GoodsRefundInterface.OndelListener ondelListener) {
        RequestParams params = new RequestParams(ConUtils.DEL_SERVICE);
        DelBean delBean = new DelBean();
        delBean.setToken(SPUtils.getToken(mContext));
        delBean.setSid(id);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(delBean);
        params.setBodyContent(strParams);
        LogUtils.log("requst: " + strParams);
        params.setConnectTimeout(1000*5);
        x.http().post(params, new Callback.CommonCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            ondelListener.onDelInfo(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            ondelListener.onDelInfo(resultBean,"取消成功");
                        }else{
                            ondelListener.onDelInfo(null,"添加失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            ondelListener.onDelInfo(null,"数据解析出错");
                            return;
                        }
                        ondelListener.onDelInfo(null, failedResultBean.getFailed().getErrorMsg());
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
                    ondelListener.onDelInfo(null,"网络错误");
                }else{
                    ondelListener.onDelInfo(null,"服务器错误");
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("删除售后 result: " + result);
                }
            }
        });
    }

    public class DelBean extends TokenBean{
        private String sid;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }
}
