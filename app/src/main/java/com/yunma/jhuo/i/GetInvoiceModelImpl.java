package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.InvoiceInterface;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-20
 *
 * @author Json.
 */

public class GetInvoiceModelImpl implements InvoiceInterface.GetInvoiceModel {

    private FailedResultBean failedResultBean = null;
    private InvoiceBean resultBean = null;

    @Override
    public void getInvoice(final Context mContext,String nums,int page,
                           final InvoiceInterface.GetInvoiceListener onListener) {
        RequestParams params = new RequestParams(ConUtils.GET_INVOICE);
        PageBean pageBean = new PageBean();
        pageBean.setPage(String.valueOf(page));
        pageBean.setSize(nums);
        pageBean.setToken(SPUtils.getToken(mContext));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(pageBean);
        params.setBodyContent(strParams);
        LogUtils.json("requst: " + strParams);
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
                            resultBean = GsonUtils.GsonToBean(result,
                                    InvoiceBean.class);
                        } catch (Exception e) {
                            onListener.onListener(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess().getList()!=null){
                            onListener.onListener(resultBean,"获取成功");
                        }else{
                            onListener.onListener(null,"获取失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.onListener(null,"数据解析出错");
                            return;
                        }
                        onListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onListener.onListener(null,"网络错误");
                }else{
                    onListener.onListener(null,"服务器错误");
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
                 //   LogUtils.json("获取增票 result: " + result);
                }
            }
        });
    }
}
