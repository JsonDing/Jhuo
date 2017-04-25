package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.AddRecipientface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created on 2017-01-02
 *
 * @author Json.
 */

public class AddRecipientImpl implements AddLocationModel {

    private AddAddressResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void addRecipient(final Context mContext, final AddAddressBean addAddressBean,
                            final OnListener onListener) {
        RequestParams params = new RequestParams(ConUtils.RRECIPIENT_ADD);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(addAddressBean);
        params.setBodyContent(strLogin);
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
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.getObject(result,
                                    AddAddressResultBean.class);
                        } catch (Exception e) {
                            onListener.onAddLocation(null,"数据解析出错");
                            return;
                        }
                        onListener.onAddLocation(resultBean,"添加成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.onAddLocation(null,"数据解析出错");
                            return;
                        }
                        onListener.onAddLocation(null,failedResultBean.getFailed().getErrorMsg());
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
                    onListener.onAddLocation(null,"网络出错");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    LogUtils.log("------------> " + ex.getMessage());
                    onListener.onAddLocation(null,"服务器错误");
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
                    LogUtils.log("添加收件人result: " + result);
                }
            }
        });

    }
}
