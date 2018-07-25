package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GetShoppingCartListInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class GetShoppingCartImpl implements GetShoppingCartModel {
    private GetShoppingListBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void getShoppingCartList(final Context mContext,
                                    final OnGetShoppingCart onGetShoppingCart) {
        RequestParams params = new RequestParams(ConUtils.GET_SHOPPING_CARTS);
        TokenBean tokenBean = new TokenBean();
        tokenBean.setToken(SPUtils.getToken(mContext));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(tokenBean);
        params.setBodyContent(strLogin);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.GsonToBean(result,
                                    GetShoppingListBean.class);
                        } catch (Exception e) {
                            onGetShoppingCart.onGetShoppingCartListener(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onGetShoppingCart.onGetShoppingCartListener(resultBean,"购物车获取成功");
                        }else{
                            onGetShoppingCart.onGetShoppingCartListener(null,"购物车为空");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetShoppingCart.onGetShoppingCartListener(null,"数据解析出错");
                            return;
                        }
                        onGetShoppingCart.onGetShoppingCartListener(null,failedResultBean.getFailed().getErrorMsg());
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
                    onGetShoppingCart.onGetShoppingCartListener(null,"网络异常");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    LogUtils.json("------------> " + ex.getMessage());
                    onGetShoppingCart.onGetShoppingCartListener(null,"服务器未响应，请稍后再试");
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
                ToastUtils.showInfo(mContext,"cancelled");
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                 //   LogUtils.json("getShoppingCarts : " + result);
                }
            }
        });

    }
}
