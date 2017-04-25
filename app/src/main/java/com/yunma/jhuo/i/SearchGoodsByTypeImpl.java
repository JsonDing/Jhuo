package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.SearchGoodsInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class SearchGoodsByTypeImpl implements SearchGoodsByTypeModel {

    private GoodsInfoResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void searchByType(final Context mContext, String type,String size,int pages,
                             final OnSearchGoodsByTypeListener onSearchGoodsListener) {
        RequestParams params = new RequestParams(ConUtils.SEARCH_GOODS);
        final SearchByCodeBean paramsBean = new SearchByCodeBean();
        paramsBean.setType(type);
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setSize(size);
        paramsBean.setPage(String.valueOf(pages));
        Gson gson = new Gson();
        String strLogin = gson.toJson(paramsBean);
        LogUtils.log("查询同类商品请求: ------------>" + strLogin);
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
                        onSearchGoodsListener.onSearchListener(resultBean,"查找成功");
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
                    onSearchGoodsListener.onSearchListener(null,"网络错误!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onSearchGoodsListener.onSearchListener(null,"服务器错误");
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
                    LogUtils.log("查询同类商品: " + result);
                }
            }
        });
    }

    private class SearchByCodeBean extends TokenBean{

        private String size;
        private String page;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
