package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GoodsCollectInterFace.*;
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

public class GetGoodsCollectImpl implements GetGoodsCollectModel {
    private GetCollectResultBean resultBean;
    private FailedResultBean failedResultBean;

    @Override
    public void GetCollect(final Context mContext,final OnGetCollect onGetCollect) {
        RequestParams params = new RequestParams(ConUtils.GET_COLLECT);
        TokenBean paramsBean = new TokenBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(paramsBean);
        params.setBodyContent(strParams);
        LogUtils.json("收藏夹请求：" + strParams);
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
                            resultBean = GsonUtils.GsonToBean(result,
                                    GetCollectResultBean.class);
                        } catch (Exception e) {
                            onGetCollect.onGetCollectListener(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onGetCollect.onGetCollectListener(resultBean,"获取成功");
                        }else{
                            onGetCollect.onGetCollectListener(null,"尚无默认收件人");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetCollect.onGetCollectListener(null,"数据解析出错");
                            return;
                        }
                        onGetCollect.onGetCollectListener(null,
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
                    LogUtils.json("responseCode: " + responseCode + "\n" + "responseMsg: " +
                            responseMsg + "\n" + "errorResult: " + errorResult);
                    onGetCollect.onGetCollectListener(null,"网络错误");
                }else{
                    onGetCollect.onGetCollectListener(null,"服务器错误");
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
                  //  LogUtils.json("收藏夹 result: " + result);
                }
            }
        });
    }

}
