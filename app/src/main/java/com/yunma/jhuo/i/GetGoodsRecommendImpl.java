package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GetGoodsInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class GetGoodsRecommendImpl implements GetGoodsRecommendModel {
    private GoodsInfoResultBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    public void getRecommenGoods(final Context mContext,String size,int nextPage,
                                 final OnGetGoodsRecommendListener onListener) {
        RequestParams params = new RequestParams(ConUtils.GET_GOODS_RECOMMEND);
        final RecommendGoodsBean recommendGoodsBean = new RecommendGoodsBean();
        recommendGoodsBean.setToken(SPUtils.getToken(mContext));
        recommendGoodsBean.setSize(size);
        recommendGoodsBean.setPage(String.valueOf(nextPage));
        String strToken = new Gson().toJson(recommendGoodsBean);
        LogUtils.log("每日精选 : ------------>" + strToken);
        params.setAsJsonContent(true);
        params.setBodyContent(strToken);
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
                                    GoodsInfoResultBean.class);
                        } catch (Exception e) {
                            onListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onListener.onListener(resultBean,"获取成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onListener.onListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onListener.onListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.log("每日精选: " + result);
                }
            }
        });
    }

   private class RecommendGoodsBean extends TokenBean{
       private String size;
       private String page;

       public String getSize() {
           return size;
       }

       public void setSize(String size) {
           this.size = size;
       }

       public String getPage() {
           return page;
       }

       public void setPage(String page) {
           this.page = page;
       }
   }
}
