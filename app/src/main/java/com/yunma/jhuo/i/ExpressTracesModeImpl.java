package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ExpressTracesInterface;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-03-17
 *
 * @author Json.
 */

public class ExpressTracesModeImpl implements ExpressTracesInterface.ExpressTracesMode {
    private ExpressTracesBean expressTracesBean;
    private FailedResultBean failedResultBean;
    @Override
    public void getExpressTraces(Context mContext, String code, String number,
                                 final ExpressTracesInterface.Onlistener onlistener) {
        RequestParams params = new RequestParams(ConUtils.SEARCH_EXPRESS);
        final ExpressTracesRequstBean resultBean = new ExpressTracesRequstBean();
        resultBean.setToken(SPUtils.getToken(mContext));
        resultBean.setCode(code);
        resultBean.setNumber(number);
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(resultBean);
        params.setBodyContent(strParams);
        LogUtils.json("requst: " + strParams);
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
                            expressTracesBean = GsonUtils.GsonToBean(result,
                                    ExpressTracesBean.class);
                        } catch (Exception e) {
                            onlistener.toShowExpressTraces(null,"数据解析出错");
                            return;
                        }
                        if(expressTracesBean.getSuccess()!=null){
                            onlistener.toShowExpressTraces(expressTracesBean,"查询成功");
                        }else{
                            onlistener.toShowExpressTraces(null,"查询失败");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onlistener.toShowExpressTraces(null,"数据解析出错");
                            return;
                        }
                        onlistener.toShowExpressTraces(null, failedResultBean.getFailed().getErrorMsg());
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
                    onlistener.toShowExpressTraces(null,"网络错误");
                }else{
                    onlistener.toShowExpressTraces(null,"服务器错误");
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
                   // LogUtils.json("物流跟踪 result: " + result);
                }
            }
        });
    }

    public class ExpressTracesRequstBean{

        private String token;
        private String code;
        private String number;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
