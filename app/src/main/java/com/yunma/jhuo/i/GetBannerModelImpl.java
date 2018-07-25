package com.yunma.jhuo.i;

import android.content.Context;

import com.yunma.bean.BannerResultBean;
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

public class GetBannerModelImpl implements SelfGoodsInterFace.GetSplashPageWordsModel {
    private FailedResultBean failedResultBean = null;
    private BannerResultBean bannerResultBean = null;

    @Override
    public void getSplashPageWords(Context mContext,
                              final SelfGoodsInterFace.OnSplashPageWordsListener onSplashPageWordsListener) {
        RequestParams params = new RequestParams(ConUtils.AD_WORDS);
        x.http().post(params, new Callback.CommonCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            bannerResultBean = GsonUtils.GsonToBean(result,
                                    BannerResultBean.class);
                            if(bannerResultBean.getSuccess()!=null){
                                onSplashPageWordsListener.onNewsBannerListener(bannerResultBean,"获取成功");
                            }else{
                                onSplashPageWordsListener.onNewsBannerListener(null,"无数据");
                            }
                        } catch (Exception e) {
                            LogUtils.json("数据解析出错:1 " + e.getMessage());
                            onSplashPageWordsListener.onNewsBannerListener(null,"数据解析出错!");
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                            onSplashPageWordsListener.onNewsBannerListener(null,
                                    failedResultBean.getFailed().getErrorMsg());
                        } catch (Exception e) {
                            LogUtils.json("数据解析出错:2 " + e.getMessage());
                            onSplashPageWordsListener.onNewsBannerListener(null,"数据解析出错!");
                            e.printStackTrace();
                        }
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
                    LogUtils.json("responseCode: " + responseCode + "\n" + "responseMsg: " + responseMsg
                            + "\n" + "errorResult: " + errorResult);
                    onSplashPageWordsListener.onNewsBannerListener(null,"网络异常");
                } else { // 其他错误
                    LogUtils.json("errorResult: " + "服务器未响应，请稍后再试");
                    onSplashPageWordsListener.onNewsBannerListener(null,"服务器未响应，请稍后再试");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
               //     LogUtils.json("闪屏页广告词：------> " + result);
                }
            }
        });
    }
}
