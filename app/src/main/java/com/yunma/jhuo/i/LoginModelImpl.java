package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yunma.bean.*;
import com.yunma.emchat.DemoHelper;
import com.yunma.emchat.db.DemoDBManager;
import com.yunma.jhuo.general.MyApplication;
import com.yunma.jhuo.m.LoginInterface.LoginModel;
import com.yunma.jhuo.m.LoginInterface.OnLoginListener;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

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
        loginServer("jhuo" + phone);
        RequestParams params = new RequestParams(ConUtils.USER_LOGIN);
        final LoginBean  loginBean = new LoginBean();
        loginBean.setUsername(phone);
        loginBean.setMobile("Android " + MobileUtils.getMobileOSVersion());
        loginBean.setPassword(MD5Utils.getMD5(password));
        Gson gson = new Gson();
        String strLogin = gson.toJson(loginBean);
        LogUtils.log("登录参数: ------------> \n 帐号：" + phone +
                "\n 密码：" + MD5Utils.getMD5(password) + "\n 环信密码：" + MD5Utils.getMD5("jhuo" + phone));
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
                            resultBean = GsonUtils.getObject(result,
                                    LoginSuccessResultBean.class);
                        } catch (Exception e) {
                            loginListener.showLoginInfos(null,"数据解析出错!");
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
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            loginListener.showLoginInfos(null,"数据解析出错!");
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
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    loginListener.showLoginInfos(null,"服务器未响应");
                    LogUtils.log("-----------> " + ex.getMessage() + "\n" + ex.getCause());
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
                    LogUtils.log("Login result: " + result);
                }
            }
        });
    }


    private void loginServer(final String username) {
        DemoDBManager.getInstance().closeDB();
        DemoHelper.getInstance().setCurrentUserName(username);
        EMClient.getInstance().login(username, MD5Utils.getMD5(username), new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if(!usernames.contains("jhuo13333333333")){
                        EMClient.getInstance().contactManager()
                                .addContact("jhuo13333333333", "用户" + username);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    LogUtils.log("LoginActivity" + "update current user nick fail");
                }
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                LogUtils.log("登录参数:---> "+"登录聊天服务器成功！");
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.log("LoginModelImpl:---> "+"登录聊天服务器失败！" + "\n" +
                "reason:---> " + s);

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
