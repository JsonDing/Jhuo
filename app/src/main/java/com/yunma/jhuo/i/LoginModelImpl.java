package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.LoginBean;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.jhuo.m.LoginInterface.LoginModel;
import com.yunma.jhuo.m.LoginInterface.OnLoginListener;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.MobileUtils;
import com.yunma.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2016-12-02.
 * @author Json
 */

public class LoginModelImpl implements LoginModel {

    private LoginSuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void login(final Context mContext, String phone, String password,
                      final OnLoginListener loginListener) {
        RequestParams params = new RequestParams(ConUtils.USER_LOGIN);
        final LoginBean  loginBean = new LoginBean();
        loginBean.setUsername(phone);
        loginBean.setMobile("Android " + MobileUtils.getMobileOSVersion());
        loginBean.setPassword(password);
        Gson gson = new Gson();
        String strLogin = gson.toJson(loginBean);
        LogUtils.urlRequest("登录", "\n 帐号：" + phone + "\n 密码：" + password);
        params.setAsJsonContent(true);
        params.setBodyContent(strLogin);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        LogUtils.json("登录返回result: " + result);
                        try {
                            resultBean = GsonUtils.GsonToBean(result,
                                    LoginSuccessResultBean.class);
                        } catch (Exception e) {
                            loginListener.showLoginInfos(null,"数据解析出错!");
                            LogUtils.json("-----------> 数据获取成功，解析出错: " + e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                        if(resultBean.getSuccess()!= null){
                            loginListener.showLoginInfos(resultBean,"Token 获取成功！");
                        }else{
                            loginListener.showLoginInfos(null,"Token 获取失败！");
                        }

                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            loginListener.showLoginInfos(null,"数据解析出错!");
                            LogUtils.json("-----------> 数据解析出错: " + e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                        loginListener.showLoginInfos(null, failedResultBean.getFailed().getErrorMsg());
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
                    loginListener.showLoginInfos(null,"网络出错!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    loginListener.showLoginInfos(null,"服务器未响应");
                    LogUtils.json("登陆 -----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.showInfo(mContext,"cancelled");
            }

            @Override
            public void onFinished() {

            }
        });
    }

}
