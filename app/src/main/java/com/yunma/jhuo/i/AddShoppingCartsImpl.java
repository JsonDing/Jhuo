package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.AddShoppingCartsInterface.*;
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

public class AddShoppingCartsImpl implements AddShoppingCartsModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean =  null;
    @Override
    public void addToBasket(final Context mContext, ShoppingCartsBean shoppingCartsBean,
                            final OnAddShoppingCartsListener onListener) {
        RequestParams params = new RequestParams(ConUtils.ADD_BASKET);
        Gson gson = new Gson();
        String strParams = gson.toJson(shoppingCartsBean);
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
                            onListener.onListener(null,"数据解析出错!");
                            LogUtils.json("--------------> " + e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                        onListener.onListener(resultBean,"添加成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
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
                    onListener.onListener(null,"网络出错");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onListener.onListener(null,"服务器错误");
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
                   // LogUtils.json("AddShoppingCarts result: " + result);
                }
            }
        });
    }
}
