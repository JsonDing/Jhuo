package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.AddToCartsRequestBean;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.m.AddToCartsInterface;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-04-24
 *
 * @author Json.
 */

public class AddToCartsImpl implements AddToCartsInterface.AddToCartsModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean =  null;
    @Override
    public void addToCarts(final Context mContext, AddToCartsRequestBean cartsRequestBean,
                           final AddToCartsInterface.OnAddToCartsListener onAddToCartsListener) {
        RequestParams params = new RequestParams(ConUtils.ADD_BASKET);
        Gson gson = new Gson();
        String strParams = gson.toJson(cartsRequestBean);
        params.setAsJsonContent(true);
        params.setBodyContent(strParams);
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
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onAddToCartsListener.onAddListener(null,"数据解析出错!");
                            LogUtils.json("--------------> " + e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                        onAddToCartsListener.onAddListener(resultBean,"添加成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onAddToCartsListener.onAddListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onAddToCartsListener.onAddListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    onAddToCartsListener.onAddListener(null,"网络出错");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onAddToCartsListener.onAddListener(null,"服务器错误");
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
                   // LogUtils.json("批量添加购物车: " + result);
                }
            }
        });
    }
}
