package com.yunma.jhuo.i;

import android.content.Context;

import com.yunma.bean.AdInfoResultBean;
import com.yunma.bean.FailedResultBean;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-04-19
 *
 * @author Json.
 */

public class GetAdsModelImpl implements SelfGoodsInterFace.GetAdsModel {
    private AdInfoResultBean adInfoResultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void getAd(Context mContext,
                      final SelfGoodsInterFace.OnGetAdListener onGetAdListener) {
        RequestParams params = new RequestParams(ConUtils.AD);
        x.http().get(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            adInfoResultBean = GsonUtils.getObject(result,
                                    AdInfoResultBean.class);
                        } catch (Exception e) {
                            onGetAdListener.onGetAdListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(adInfoResultBean.getSuccess()!=null){
                            onGetAdListener.onGetAdListener(adInfoResultBean,"获取成功");
                        }else{
                            onGetAdListener.onGetAdListener(null,"无数据");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetAdListener.onGetAdListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onGetAdListener.onGetAdListener(null,
                                failedResultBean.getFailed().getErrorMsg());
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
                    LogUtils.log("responseCode: " + responseCode + "\n" + "responseMsg: " + responseMsg
                            + "\n" + "errorResult: " + errorResult);
                    onGetAdListener.onGetAdListener(null,"网络异常");
                } else { // 其他错误
                    LogUtils.log("errorResult: " + "服务器未响应，请稍后再试");
                    onGetAdListener.onGetAdListener(null,"服务器未响应，请稍后再试");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("获取插播广告数据：------> " + result);
                }
            }
        });
    }
}
