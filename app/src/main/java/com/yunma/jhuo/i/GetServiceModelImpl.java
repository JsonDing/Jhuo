package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ServiceInterface;
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

public class GetServiceModelImpl implements ServiceInterface.GetServiceModel {

    private FailedResultBean failedResultBean = null;
    private ServiceResultBean resultBean1 = null;
    private ServiceResultBean resultBean2 = null;

    @Override
    public void toGetService(final Context mContext, final String refund,
                             final ServiceInterface.OnGetServiceListener onGetServiceListener) {
        RequestParams params = new RequestParams(ConUtils.GET_SERVICE);
        ServicesBean requstBean = new ServicesBean();
        requstBean.setToken(SPUtils.getToken(mContext));
        requstBean.setRefund(refund);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(requstBean);
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
                            resultBean1 = GsonUtils.GsonToBean(result,
                                    ServiceResultBean.class);
                        } catch (Exception e) {
                            onGetServiceListener.toShowGetInfos(null,"数据解析出错");
                            return;
                        }
                        if(resultBean1.getSuccess().getList()!=null){
                            if(refund.equals("1")){
                                requestRefundTwo(mContext,"2",onGetServiceListener);
                            }else{
                                onGetServiceListener.toShowGetInfos(resultBean1,"添加成功");
                            }
                        }else{
                            onGetServiceListener.toShowGetInfos(null,"添加失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetServiceListener.toShowGetInfos(null,"数据解析出错");
                            return;
                        }
                        onGetServiceListener.toShowGetInfos(null, failedResultBean.getFailed().getErrorMsg());
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
                    onGetServiceListener.toShowGetInfos(null,"网络错误");
                }else{
                    onGetServiceListener.toShowGetInfos(null,"服务器错误");
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
                    LogUtils.json("获取售后 result: " + result);
                }
            }
        });
    }

    private void requestRefundTwo(Context mContext,String refund,
                                  final ServiceInterface.OnGetServiceListener onGetServiceListener) {
        RequestParams params = new RequestParams(ConUtils.GET_SERVICE);
        ServicesBean requstBean = new ServicesBean();
        requstBean.setToken(SPUtils.getToken(mContext));
        requstBean.setRefund(refund);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(requstBean);
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
                    //   LogUtils.json("获取售后 result: " + result);
                    if(result.contains("success")){
                        try {
                            resultBean2 = GsonUtils.GsonToBean(result,
                                    ServiceResultBean.class);
                        } catch (Exception e) {
                            onGetServiceListener.toShowGetInfos(null,"数据解析出错");
                            return;
                        }
                        if(resultBean2.getSuccess().getList()!=null){
                            resultBean2.getSuccess().getList().addAll(resultBean1.getSuccess().getList());
                            onGetServiceListener.toShowGetInfos(resultBean2,"添加成功");
                        }else{
                            onGetServiceListener.toShowGetInfos(null,"添加失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetServiceListener.toShowGetInfos(null,"数据解析出错");
                            return;
                        }
                        onGetServiceListener.toShowGetInfos(null, failedResultBean.getFailed().getErrorMsg());
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
                    onGetServiceListener.toShowGetInfos(null,"网络错误");
                }else{
                    onGetServiceListener.toShowGetInfos(null,"服务器错误");
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
                  //  LogUtils.json("获取售后 result: " + result);
                }
            }
        });

    }

    public class ServicesBean extends TokenBean{
        private String refund;

        public String getRefund() {
            return refund;
        }

        public void setRefund(String refund) {
            this.refund = refund;
        }
    }
}
