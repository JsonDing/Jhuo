package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ModifyRecipientInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class ModifyRecipientImpl implements ModifyRecipientModel {
    private FailedResultBean failedResultBean = null;
    private SuccessResultBean successResultBean = null;
    @Override
    public void modifyRecipient(final Context mContext, ModifyAddressBean  modifyAddressBean,
                               final OnListener onListener) {
        RequestParams params = new RequestParams(ConUtils.RRECIPIENT_MODIFY);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(modifyAddressBean);
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
                            successResultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onListener.onModifyLocation(null,"数据解析出错");
                            return;
                        }
                        onListener.onModifyLocation(successResultBean,"修改成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.onModifyLocation(null,"数据解析出错");
                            return;
                        }
                        onListener.onModifyLocation(null,failedResultBean.getFailed().getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                LogUtils.log("onError: ---> " + ex.getMessage());
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    onListener.onModifyLocation(null,"服务器出错");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onListener.onModifyLocation(null,ex.getMessage());
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
                    LogUtils.log("修改收件人result: " + result);
                }
            }
        });
    }
}
