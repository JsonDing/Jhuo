package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GoodsCollectInterFace;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-11
 *
 * @author Json.
 */

public class IfcollectImpl implements GoodsCollectInterFace.IfcollectModel {
    private AddGoodsCollectBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    public void ifCollect(final Context mContext, String goodsId,
                          final GoodsCollectInterFace.IfcollectListener onListener) {
        RequestParams params = new RequestParams(ConUtils.IF_COLLECT);
        AddCollectRequestBean paramsBean = new AddCollectRequestBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setGid(goodsId);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(paramsBean);
        params.setBodyContent(strParams);
        params.setConnectTimeout(1000*5);
        x.http().post(params, new Callback.CommonCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            resultBean = GsonUtils.getObject(result,
                                    AddGoodsCollectBean.class);
                        } catch (Exception e) {
                            onListener.onListener(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onListener.onListener(resultBean,"已收藏");
                        }else{
                            onListener.onListener(null,"未收藏");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.onListener(null,"数据解析出错");
                            return;
                        }
                        onListener.onListener(null,
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
                    LogUtils.log("responseCode: " + responseCode + "\n" + "responseMsg: " +
                            responseMsg + "\n" + "errorResult: " + errorResult);
                    onListener.onListener(null,"网络错误");
                }else{
                    onListener.onListener(null,"服务器错误");
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("是否收藏 result: " + result);
                }
            }
        });
    }

    private class AddCollectRequestBean{

        private String token;
        private String gid;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }
    }
}
