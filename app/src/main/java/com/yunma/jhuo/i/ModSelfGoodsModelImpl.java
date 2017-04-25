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
 * Created on 2017-04-10
 *
 * @author Json.
 */

public class ModSelfGoodsModelImpl implements SelfGoodsInterFace.ModSelfGoodsModel {
    private ModifySelfResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    private SuccessResultBean successResultBean = null;

    @Override
    public void modifySelfGoods(final Context mContext, NewModSelfGoodsBean newModSelfGoodsBean,
                                final SelfGoodsInterFace.ModSelfGoodsListener modSelfGoodsListener) {
        RequestParams requestParams = new RequestParams(ConUtils.MOD_SELF_GOODS);
        requestParams.setAsJsonContent(true);
        LogUtils.log("修改库存请求: ------------>" + new Gson().toJson(newModSelfGoodsBean));
        requestParams.setBodyContent(new Gson().toJson(newModSelfGoodsBean));
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
                                    ModifySelfResultBean.class);
                        } catch (Exception e) {
                            modSelfGoodsListener.onModifyListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        modSelfGoodsListener.onModifyListener(resultBean,"修改成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            modSelfGoodsListener.onModifyListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        modSelfGoodsListener.onModifyListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    modSelfGoodsListener.onModifyListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    modSelfGoodsListener.onModifyListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.log("修改商品库存成功: " + result);
                }
            }
        });
    }

    @Override
    public void modifySelfGoodsById(final Context mContext, ModifyYunmasBean modifyYunmasBean,
                                    final SelfGoodsInterFace.ModSelfGoodsByIdListener modSelfGoodsListener) {
        RequestParams requestParams = new RequestParams(ConUtils.MOD_SELF_GOODS);
        requestParams.setAsJsonContent(true);
        LogUtils.log("修改发布商品请求: ------------>" + new Gson().toJson(modifyYunmasBean));
        requestParams.setBodyContent(new Gson().toJson(modifyYunmasBean));
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    LogUtils.log("修改商品信息成功: " + result);
                    if(result.contains("success")){
                        try {
                            successResultBean = GsonUtils.getObject(result,
                                    SuccessResultBean.class);
                        } catch (Exception e) {
                            modSelfGoodsListener.onModifyByIdListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        modSelfGoodsListener.onModifyByIdListener(successResultBean,"修改成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            modSelfGoodsListener.onModifyByIdListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        modSelfGoodsListener.onModifyByIdListener(null, failedResultBean.getFailed().getErrorMsg());
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
                    modSelfGoodsListener.onModifyByIdListener(null,"网络异常!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    modSelfGoodsListener.onModifyByIdListener(null,"服务器未响应，请稍后再试");
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
                    LogUtils.log("修改商品信息成功: " + result);
                }
            }
        });
    }
}
