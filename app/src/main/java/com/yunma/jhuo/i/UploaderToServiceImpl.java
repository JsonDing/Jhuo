package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.UploaderToServiceInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-18
 *
 * @author Json.
 */

public class UploaderToServiceImpl implements UploaderToServiceModel {

    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;

    @Override
    public void uploader(final Context mContext, String path,
                         final OnUploaderToServiceListener onListener) {
        RequestParams params = new RequestParams(ConUtils.UPLOADER_PHOTO);
        final ModifyPhotoBean  paramsBean = new ModifyPhotoBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setPic(path);
        Gson gson = new Gson();
        String strLogin = gson.toJson(paramsBean);
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
                            resultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onListener.OnUploaderListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onListener.OnUploaderListener(resultBean,"头像更新成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.OnUploaderListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onListener.OnUploaderListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onListener.OnUploaderListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onListener.OnUploaderListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.log("UpLoader result: " + result);
                }
            }
        });
    }

    private class ModifyPhotoBean{

        private String token;
        private String pic;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
