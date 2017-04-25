package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.SearchGoodsInterface.OnSearchGoodsListener;
import com.yunma.jhuo.m.SearchGoodsInterface.SearchGoodsByCodeModel;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2017/3/2.
 */

public class SearchGoodsByCodeImpl implements SearchGoodsByCodeModel {
    private GoodsInfoResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void searchByCode(final Context mContext, String code,
                             final OnSearchGoodsListener onSearchGoodsListener) {
        RequestParams params = new RequestParams(ConUtils.SEARCH_GOODS);
        final SearchByCodeBean paramsBean = new SearchByCodeBean();
        paramsBean.setNumber(code);
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setSize("1000");
        Gson gson = new Gson();
        String strLogin = gson.toJson(paramsBean);
        LogUtils.log("查询商品请求: ------------>" + strLogin);
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
                                    GoodsInfoResultBean.class);
                        } catch (Exception e) {
                            onSearchGoodsListener.onSearchListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(resultBean.getSuccess().getList()!=null){
                            onSearchGoodsListener.onSearchListener(resultBean,"查找成功");
                        }else{
                            onSearchGoodsListener.onSearchListener(null,"商品已下架或货号错误");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onSearchGoodsListener.onSearchListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onSearchGoodsListener.onSearchListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onSearchGoodsListener.onSearchListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onSearchGoodsListener.onSearchListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.log("查询商品: " + result);
                }
            }
        });
    }

    private class SearchByCodeBean extends TokenBean{

        private String size;
        private String page;
        private String number;

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

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
