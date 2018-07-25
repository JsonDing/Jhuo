package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.MineFragmentInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-16
 *
 * @author Json.
 */

public class UserInfosImpl implements GetUserInfosModel {
    private UserInfosResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;

    @Override
    public void getUserInfos(final Context mContext,
                             final OnGetUserInfosListener onGetUserInfosListener) {
        RequestParams params = new RequestParams(ConUtils.USER_INFOS_QUERY);
        final TokenBean tokenBean = new TokenBean();
        tokenBean.setToken(SPUtils.getToken(mContext));
        Gson gson = new Gson();
        String strLogin = gson.toJson(tokenBean);
        params.setAsJsonContent(true);
        params.setCacheMaxAge(10*1024);
        params.setBodyContent(strLogin);
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
                                    UserInfosResultBean.class);
                        } catch (Exception e) {
                            onGetUserInfosListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onGetUserInfosListener.onListener(resultBean,"获取成功");
                        }else{
                            onGetUserInfosListener.onListener(null,"该用户不存在");
                        }

                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetUserInfosListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onGetUserInfosListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onGetUserInfosListener.onListener(null,"网络异常，请稍后重试");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onGetUserInfosListener.onListener(null,"服务器正忙，请稍后重试");
                    LogUtils.json("-----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.json("用户信息: " + result);
                }
            }
        });
    }
}
