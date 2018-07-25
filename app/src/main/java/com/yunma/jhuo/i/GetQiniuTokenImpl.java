package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GetQiniuTokenInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-17
 *
 * @author Json.
 */

public class GetQiniuTokenImpl implements GetQiniuTokenModel {
    private FailedResultBean failedResultBean = null;
    private QiniuResultBean resultBean = null;
    @Override
    public void getQiniuToken(final Context mContext, String type,
                              final OnGetQiniuTokenListener onGetQiniuTokenListener) {

        RequestParams params = new RequestParams(ConUtils.GET_QINIU_TOKEN);
        final QiniuBean qiniuBean = new QiniuBean();
        qiniuBean.setToken(SPUtils.getToken(mContext));
        qiniuBean.setType(type);
        Gson gson = new Gson();
        String strLogin = gson.toJson(qiniuBean);
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
                            resultBean = GsonUtils.GsonToBean(result,
                                    QiniuResultBean.class);
                        } catch (Exception e) {
                            onGetQiniuTokenListener.onListener(null,"数据解析错误");
                            ToastUtils.showError(mContext,e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                        LogUtils.json("七牛获取成功success: " );
                        onGetQiniuTokenListener.onListener(resultBean,"Token获取成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetQiniuTokenListener.onListener(null,"数据解析错误");
                            ToastUtils.showError(mContext,e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                        onGetQiniuTokenListener.onListener(null,failedResultBean.getFailed().getErrorMsg());
                        ToastUtils.showError(mContext,
                                failedResultBean.getFailed().getErrorMsg());
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
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                    onGetQiniuTokenListener.onListener(null,"网络异常");
                } else { // 其他错误
                    onGetQiniuTokenListener.onListener(null,"服务器未响应，请稍后再试");
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
                  //  LogUtils.json("七牛获取成功finish: " + result);
                }
            }
        });
    }
}
