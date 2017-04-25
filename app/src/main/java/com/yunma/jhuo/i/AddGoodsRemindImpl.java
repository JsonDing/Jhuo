package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.bean.TokenBean;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-04-19
 *
 * @author Json.
 */

public class AddGoodsRemindImpl implements SelfGoodsInterFace.AddSelfGoodsRemindModel {
    private SuccessResultBean resultBean;
    private FailedResultBean failedResultBean = null;
    @Override
    public void addSelfGoodsRemind(Context mContext,String goodsIds,
                                   final SelfGoodsInterFace.AddSelfGoodsRemindListener mListener) {
        RequestParams params = new RequestParams(ConUtils.ADD_REMIND);
        AddRemindBean requstBean = new AddRemindBean();
        requstBean.setToken(SPUtils.getToken(mContext));
        List<String> ids = new ArrayList<>();
        ids.add(goodsIds);
        requstBean.setGoodsNum(ids);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(requstBean);
        params.setBodyContent(strParams);
        LogUtils.log("requst: " + strParams);
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
                            mListener.addSelfGoodsRemindListener(null,"数据解析出错");
                            return;
                        }
                        mListener.addSelfGoodsRemindListener(resultBean,"添加成功");
                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            mListener.addSelfGoodsRemindListener(null,"数据解析出错");
                            return;
                        }
                        mListener.addSelfGoodsRemindListener(null,
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
                    mListener.addSelfGoodsRemindListener(null,"网络异常或延时，请稍后重试");
                }else{
                    mListener.addSelfGoodsRemindListener(null,"服务器异常");
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
                    LogUtils.log("添加提醒 result: " + result);
                }
            }
        });
    }

    private class AddRemindBean extends TokenBean{

        private List<String> goodsNum;

        public List<String> getGoodsNum() {
            return goodsNum;
        }

        public void setGoodsNum(List<String> goodsNum) {
            this.goodsNum = goodsNum;
        }
    }
}
