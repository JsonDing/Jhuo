package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.PersonInfosMoifyInterface.*;
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

public class PersonInfosMoifyImpl implements PersonInfosMoifyModel {
    private SuccessResultBean successResultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void PersonInfosMoify(final Context mContext, String nickName, String gender, String realName,String qq,
                                 final OnPersonInfosMoify onPersonInfosMoify) {
        RequestParams params = new RequestParams(ConUtils.USER_INFOS_MODIFY);
        final PersonInfosMoifyBean paramsBean = new PersonInfosMoifyBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setName(nickName);
        paramsBean.setQq(qq);
        Gson gson = new Gson();
        String strModify = gson.toJson(paramsBean);
        LogUtils.test("修改个人信息请求：" + strModify);
        params.setAsJsonContent(true);
        params.setBodyContent(strModify);
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
                            successResultBean = GsonUtils.GsonToBean(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onPersonInfosMoify.onMoifyListenter(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onPersonInfosMoify.onMoifyListenter(successResultBean,"修改成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onPersonInfosMoify.onMoifyListenter(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onPersonInfosMoify.onMoifyListenter(null, failedResultBean.getFailed().getErrorMsg());
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
                    onPersonInfosMoify.onMoifyListenter(null,"网络异常!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onPersonInfosMoify.onMoifyListenter(null,"服务器未响应，请稍后再试");
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
                 //   LogUtils.json("Login result: " + result);
                }
            }
        });

    }
}
