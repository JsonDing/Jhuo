package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ModifypassWdInterface;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-15
 *
 * @author Json.
 */

public class ModifyPassWdImpl implements ModifypassWdInterface.ModifyPassWdModel {
    private SuccessResultBean successResultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void modifyPassWd(final Context context, String originalPassWd, String newPassWd,
                             final ModifypassWdInterface.OnModifyPassWdListener onModifyPassWdListener) {
        RequestParams params = new RequestParams(ConUtils.MODIFY_PASSWORD);
        final ModifyPassWdBean modifyPassWdBean = new ModifyPassWdBean();
        modifyPassWdBean.setOldpass(MD5Utils.getMD5(originalPassWd));
        modifyPassWdBean.setNewpass(MD5Utils.getMD5(newPassWd));
        modifyPassWdBean.setToken(SPUtils.getToken(context));
        Gson gson = new Gson();
        String strLogin = gson.toJson(modifyPassWdBean);
        LogUtils.log("strLogin: ------------>" + strLogin);
        params.setAsJsonContent(true);
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
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            successResultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onModifyPassWdListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onModifyPassWdListener.onListener(successResultBean,successResultBean.getSuccess());
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onModifyPassWdListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onModifyPassWdListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onModifyPassWdListener.onListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onModifyPassWdListener.onListener(null,"服务器未响应，请稍后再试");
                    LogUtils.log("-----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.showInfo(context,"cancelled");
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("Modify result: " + result);
                }
            }
        });
    }
}
