package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.AskForInvoiceInterFace;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-29
 *
 * @author Json.
 */

public class AskForInvoiceImpl implements AskForInvoiceInterFace.AskForInvoiceModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void askForInvoice(final Context mContext, AskForInvoiceBean askForInvoiceBean,
                              final AskForInvoiceInterFace.OnAskForInvoice onAskForInvoice) {
        RequestParams params = new RequestParams(ConUtils.ASK_FOR_INVOICE);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(askForInvoiceBean);
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
                            onAskForInvoice.onAskListener(null,"数据解析出错");
                            return;
                        }
                        onAskForInvoice.onAskListener(resultBean,"申请成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onAskForInvoice.onAskListener(null,"数据解析出错");
                            return;
                        }
                        onAskForInvoice.onAskListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onAskForInvoice.onAskListener(null,"网络错误");
                }else{
                    onAskForInvoice.onAskListener(null,"服务器错误");
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
                    LogUtils.log("索取发票 result: " + result);
                }
            }
        });
    }
}
