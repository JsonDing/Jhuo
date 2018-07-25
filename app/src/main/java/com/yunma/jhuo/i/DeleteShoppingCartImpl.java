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
 * Created on 2017-03-02
 *
 * @author Json.
 */

public class DeleteShoppingCartImpl implements DelShoppingCartModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void delShoppingCartList(final Context mContext, String delId,
                                    final OnDelShoppingCart onDelShoppingCart) {
        RequestParams params = new RequestParams(ConUtils.DEL_SHOPPING_CARTS);
        DelGoodsBean delGoodsBean = new DelGoodsBean();
        delGoodsBean.setToken(SPUtils.getToken(mContext));
        delGoodsBean.setIds(delId);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(delGoodsBean);
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
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onDelShoppingCart.onDelShoppingCartListener(null,"数据解析出错");
                            return;
                        }
                        onDelShoppingCart.onDelShoppingCartListener(resultBean,resultBean.getSuccess());
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onDelShoppingCart.onDelShoppingCartListener(null,"数据解析出错");
                            return;
                        }
                        onDelShoppingCart.onDelShoppingCartListener(null,failedResultBean.getFailed().getErrorMsg());
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
                    onDelShoppingCart.onDelShoppingCartListener(null,"网络异常");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    LogUtils.json("------------> " + ex.getMessage());
                    onDelShoppingCart.onDelShoppingCartListener(null,"服务器未响应，请稍后再试");
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
                  //  LogUtils.json("删除订单 : " + result);
                }
            }
        });
    }

    public class DelGoodsBean{
        /**
         * token : eyJhbGciOiJIUzI1NiIsInR5cPBlLFVG8x-ib_Y
         * ids : 50
         */
        private String token;
        private String ids;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }
    }
}
