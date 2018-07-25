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

public class AddGoodsCollectImpl implements GoodsCollectInterFace.AddGoodsCollectModel {
    private AddGoodsCollectBean resultBean;
    private FailedResultBean failedResultBean;
    @Override
    public void addToCollect(final Context mContext, String goodId,
                             final GoodsCollectInterFace.OnAddCollect onAddCollect) {
        RequestParams params = new RequestParams(ConUtils.ADD_COLLECT);
        AddCollectRequestBean paramsBean = new AddCollectRequestBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setGid(goodId);
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
                            resultBean = GsonUtils.GsonToBean(result,
                                    AddGoodsCollectBean.class);
                        } catch (Exception e) {
                            onAddCollect.onAddCollectListener(null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            onAddCollect.onAddCollectListener(resultBean,"添加成功");
                        }else{
                            onAddCollect.onAddCollectListener(null,"添加失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onAddCollect.onAddCollectListener(null,"数据解析出错");
                            return;
                        }
                        onAddCollect.onAddCollectListener(null,
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
                    onAddCollect.onAddCollectListener(null,"网络错误");
                }else{
                    onAddCollect.onAddCollectListener(null,"服务器错误");
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
                    //LogUtils.json("添加收藏夹 result: " + result);
                }
            }
        });
    }

    public class AddCollectRequestBean{

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
