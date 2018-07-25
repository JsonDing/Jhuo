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

public class AddServiceImpl implements ServiceInterface.AddServiceModel {
    private SuccessResultBean resultBean;
    private FailedResultBean failedResultBean = null;
    @Override
    public void toAddService(Context mContext, UpLoadServiceBean requstBean,
                             final ServiceInterface.OnAddServiceListener onAddServiceListener) {
        RequestParams params = new RequestParams(ConUtils.ADD_SERVICE);
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
                            resultBean = GsonUtils.GsonToBean(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onAddServiceListener.toShowAddInfos(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onAddServiceListener.toShowAddInfos(resultBean,"申请成功");
                        }else{
                            onAddServiceListener.toShowAddInfos(null,"添加失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onAddServiceListener.toShowAddInfos(null,"数据解析出错");
                            return;
                        }
                        onAddServiceListener.toShowAddInfos(null, failedResultBean.getFailed().getErrorMsg());
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
                    onAddServiceListener.toShowAddInfos(null,"网络错误");
                }else{
                    onAddServiceListener.toShowAddInfos(null,"服务器错误");
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
                   // LogUtils.json("添加售后 result: " + result);
                }
            }
        });
    }
}
