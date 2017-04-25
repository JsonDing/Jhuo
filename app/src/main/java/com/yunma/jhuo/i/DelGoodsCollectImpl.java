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
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class DelGoodsCollectImpl implements GoodsCollectInterFace.DelGoodsCollectModel {
    private SuccessResultBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    public void delCollect(final Context mContext, String goodId,
                           final GoodsCollectInterFace.OnDelCollect onDelCollect) {
        RequestParams params = new RequestParams(ConUtils.DEL_COLLECT);
        DelCollectRequestBean paramsBean = new DelCollectRequestBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setIds(goodId);
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
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            onDelCollect.onDelCollectListener(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onDelCollect.onDelCollectListener(resultBean,"删除成功");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onDelCollect.onDelCollectListener(null,"数据解析出错");
                            return;
                        }
                        onDelCollect.onDelCollectListener(null,
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
                    onDelCollect.onDelCollectListener(null,"网络错误");
                }else{
                    onDelCollect.onDelCollectListener(null,"服务器错误");
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
                    LogUtils.log("删除收藏夹 result: " + result);
                }
            }
        });
    }

    public class DelCollectRequestBean{

        private String token;
        private String ids;

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }
}
