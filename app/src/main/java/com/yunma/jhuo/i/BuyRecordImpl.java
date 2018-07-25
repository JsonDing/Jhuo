package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.BuyRecordInterface;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-21
 *
 * @author Json.
 */

public class BuyRecordImpl implements BuyRecordInterface.BuyRecordModel {

    private BuyRecordBean resultBean;
    private FailedResultBean failedResultBean;

    @Override
    public void buyRecord(final Context mContext, String page, String size,
                          final BuyRecordInterface.OnBuyListener onBuyListener) {
        RequestParams params = new RequestParams(ConUtils.LOOK_BUY_RECORD);
        BuyRecordReruestBean mBean = new BuyRecordReruestBean();
        mBean.setToken(SPUtils.getToken(mContext));
        mBean.setPage(page);
        mBean.setSize(size);
        String strBodyContent = new Gson().toJson(mBean);
        LogUtils.json("购买记录请求: ------------>" + strBodyContent);
        params.setAsJsonContent(true);
        params.setBodyContent(strBodyContent);
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
                    if (result.contains("success")) {
                        try {
                            resultBean = GsonUtils.GsonToBean(result,
                                    BuyRecordBean.class);
                        } catch (Exception e) {
                            onBuyListener.onBuyListener(null, "数据解析出错");
                            return;
                        }
                        if (resultBean.getSuccess().getList() != null)
                            onBuyListener.onBuyListener(resultBean, "获取成功");
                        else
                            onBuyListener.onBuyListener(null, "数据为空");
                    } else {
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onBuyListener.onBuyListener(null, "数据解析出错");
                            return;
                        }
                        onBuyListener.onBuyListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onBuyListener.onBuyListener(null, "网络出错");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" + "--- errorResult: " + errorResult);
                } else { // 其他错误
                    onBuyListener.onBuyListener(null, "服务器出错");
                    LogUtils.json("-----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.showInfo(mContext, "cancelled");
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                  //  LogUtils.json("购买记录: " + result);
                }
            }
        });
    }

    public class BuyRecordReruestBean {
        private String token;
        private String size;
        private String page;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

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
