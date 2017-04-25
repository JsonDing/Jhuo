package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2017/4/10.
 */

public class ShelveSelfGoodsModelImpl implements SelfGoodsInterFace.ShelveSelfGoodsModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void shelveSelfGoods(final Context mContext,IssueBean issueBean,
                                final SelfGoodsInterFace.ShelveSelfGoodsListener mListener) {
        RequestParams requestParams = new RequestParams(ConUtils.PUBLISH_SELF_GOODS);
        requestParams.setAsJsonContent(true);
        LogUtils.log("下架商品请求: ------------>" + new Gson().toJson(issueBean));
        requestParams.setBodyContent(new Gson().toJson(issueBean));
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
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
                            mListener.onShelveListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        mListener.onShelveListener(resultBean,resultBean.getSuccess());
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            mListener.onShelveListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        mListener.onShelveListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    mListener.onShelveListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    mListener.onShelveListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.log("下架商品: " + result);
                }
            }
        });
    }
}
