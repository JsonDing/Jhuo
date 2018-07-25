package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.SaveOrderInterface.*;
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

public class SaveOrderImpl implements SaveOrderModel{
    private SaveOrderResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void saveOrder(final Context mContext, SaveOrderBean saveOrderBean,
                          final OnSaveOrderListener onSaveOrderListener) {
        RequestParams params = new RequestParams(ConUtils.SAVE_ORDERS);
        Gson gson = new Gson();
        String strParams = gson.toJson(saveOrderBean);
        params.setAsJsonContent(true);
        params.setBodyContent(strParams);
        LogUtils.json("提交请求参数：" + strParams);
        params.setConnectTimeout(10*1000);
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
                                    SaveOrderResultBean.class);
                        } catch (Exception e) {
                            onSaveOrderListener.onListener(null,"数据解析出错!");
                            LogUtils.json("数据解析出错--------------> " + e.getMessage() + "\n" +
                            e.getLocalizedMessage() + "\n" + e.getCause());
                            e.printStackTrace();
                            return;
                        }
                        onSaveOrderListener.onListener(resultBean,"订单保存成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onSaveOrderListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onSaveOrderListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onSaveOrderListener.onListener(null,"网络异常!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onSaveOrderListener.onListener(null,"服务器未响应，请稍后再试");
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
                //    LogUtils.json("SaveOrder result: " + result);
                }
            }
        });
    }

}
