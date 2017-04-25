package com.yunma.jhuo.m;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.GoodsRemindResultBean;
import com.yunma.bean.TokenBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-04-20
 *
 * @author Json.
 */

public class GoodsRemindImpl implements SelfGoodsInterFace.GoodsRemindModel {
    private FailedResultBean failedResultBean = null;
    private GoodsRemindResultBean resultBean = null;
    @Override
    public void getGoodsRemind(Context mContext,
                                   final SelfGoodsInterFace.GoodsRemindListener goodsRemindListener) {
        RequestParams params = new RequestParams(ConUtils.GET_REMIND);
        TokenBean paramsBean = new TokenBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
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
                                    GoodsRemindResultBean.class);
                        } catch (Exception e) {
                            goodsRemindListener.onGoodsRemindListener(null,null,"数据解析出错");
                            return;
                        }
                        if(resultBean.getSuccess().getNewReminds()!=null){
                            goodsRemindListener.onGoodsRemindListener(resultBean,
                                    resultBean.getSuccess().getNewReminds(),"获取成功");
                        }else{
                            goodsRemindListener.onGoodsRemindListener(null,null,"获取失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            goodsRemindListener.onGoodsRemindListener(null,null,"数据解析出错");
                            return;
                        }
                        goodsRemindListener.onGoodsRemindListener(null,null,
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
                    goodsRemindListener.onGoodsRemindListener(null,null,"网络异常");
                }else{
                    goodsRemindListener.onGoodsRemindListener(null,null,"服务器异常");
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
                    LogUtils.log("获取上新提醒: " + result);
                }
            }
        });
    }
}
