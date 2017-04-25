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

public class GetBannerModelImpl implements SelfGoodsInterFace.GetBannerModel {
    private FailedResultBean failedResultBean = null;
    private BannerResultBean bannerResultBean = null;

    @Override
    public void getNewsBanner(Context mContext,
                              final SelfGoodsInterFace.OnBananerListener onBananerListener) {
        RequestParams params = new RequestParams(ConUtils.BANNER);
        x.http().get(params, new Callback.CommonCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            bannerResultBean = GsonUtils.getObject(result,
                                    BannerResultBean.class);
                        } catch (Exception e) {
                            onBananerListener.onNewsBannerListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(bannerResultBean.getSuccess()!=null){
                            onBananerListener.onNewsBannerListener(bannerResultBean,"获取成功");
                        }else{
                            onBananerListener.onNewsBannerListener(null,"无数据");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onBananerListener.onNewsBannerListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onBananerListener.onNewsBannerListener(null,
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
                    onBananerListener.onNewsBannerListener(null,"网络异常");
                } else { // 其他错误
                    LogUtils.log("errorResult: " + "服务器未响应，请稍后再试");
                    onBananerListener.onNewsBannerListener(null,"服务器未响应，请稍后再试");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("获取Banner数据：------> " + result);
                }
            }
        });
    }
}
