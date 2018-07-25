package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.VolumeListInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-04-11
 *
 * @author Json.
 */

public class VolumeListModelImpl implements VolumeListModel {
    private VolumeResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;

    @Override
    public void getUnuseVolumeList(final Context mContext,
                                   final UnUsestListener onVolumeListListener) {
        RequestParams params = new RequestParams(ConUtils.GET_VOLUME_LIST);
        final VolumeBean  volumeBean = new VolumeBean();
        volumeBean.setToken(SPUtils.getToken(mContext));
        volumeBean.setIsPastDue("0");
        String strBodyContent = new Gson().toJson(volumeBean);
        params.setAsJsonContent(true);
        params.setBodyContent(strBodyContent);
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
                                    VolumeResultBean.class);
                        } catch (Exception e) {
                            onVolumeListListener.onUnUseVolumeList(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onVolumeListListener.onUnUseVolumeList(resultBean,"Success");
                        }else{
                            onVolumeListListener.onUnUseVolumeList(null,"还没有优惠卷");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onVolumeListListener.onUnUseVolumeList(null,"数据解析出错");
                            return;
                        }
                        onVolumeListListener.onUnUseVolumeList(null,
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
                    onVolumeListListener.onUnUseVolumeList(null,"网络出异常，请稍后再试");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onVolumeListListener.onUnUseVolumeList(null,"服务器未响应，请稍后再试");
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
                    LogUtils.json("未使用优惠卷: " + result);
                }
            }
        });

    }

    @Override
    public void getTimeOutVolumeList(final Context mContext,
                                   final TimeOutListener onVolumeListListener) {
        RequestParams params = new RequestParams(ConUtils.GET_VOLUME_LIST);
        final VolumeBean  volumeBean = new VolumeBean();
        volumeBean.setToken(SPUtils.getToken(mContext));
        volumeBean.setIsPastDue("1");
        String strBodyContent = new Gson().toJson(volumeBean);
        LogUtils.json("获取已过期优惠卷请求: ------------>" + strBodyContent);
        params.setAsJsonContent(true);
        params.setBodyContent(strBodyContent);
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
                                    VolumeResultBean.class);
                        } catch (Exception e) {
                            onVolumeListListener.onTimeOutVolumeList(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onVolumeListListener.onTimeOutVolumeList(resultBean,"Success");
                        }else{
                            onVolumeListListener.onTimeOutVolumeList(null,"还没有优惠卷");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onVolumeListListener.onTimeOutVolumeList(null,"数据解析出错");
                            return;
                        }
                        onVolumeListListener.onTimeOutVolumeList(null,
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
                    onVolumeListListener.onTimeOutVolumeList(null,"网络出异常，请稍后再试");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onVolumeListListener.onTimeOutVolumeList(null,"服务器未响应，请稍后再试");
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
                  //  LogUtils.json("已过期优惠卷: " + result);
                }
            }
        });

    }


    private class VolumeBean extends TokenBean{
        private String isPastDue;

        public String getIsPastDue() {
            return isPastDue;
        }

        public void setIsPastDue(String isPastDue) {
            this.isPastDue = isPastDue;
        }
    }
}
