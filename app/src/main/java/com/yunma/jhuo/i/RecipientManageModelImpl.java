package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.RecipientManageInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Json on 2017/2/8.
 */

public class RecipientManageModelImpl implements RecipientManageModel {
    private RecipientManageBean recipientManageBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void onQueryRecipient(final Context mContext, final OnListener onListener) {
        RequestParams params = new RequestParams(ConUtils.RRECIPIENT_REQURY);
        QuaryAddressBean paramsBean = new QuaryAddressBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setSize("1000");
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(paramsBean);
        LogUtils.log("-----------> " + strLogin);
        params.setBodyContent(strLogin);
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
                            recipientManageBean = GsonUtils.getObject(result,
                                    RecipientManageBean.class);
                        } catch (Exception e) {
                            onListener.quaryRecipientListener(null,"数据解析出错");
                            return;
                        }
                        if(recipientManageBean.getSuccess()!=null&&
                                recipientManageBean.getSuccess().getList()!=null){
                            if(recipientManageBean.getSuccess().getList().size()!=0){
                                onListener.quaryRecipientListener(recipientManageBean,"查询成功");
                            }else{
                                onListener.quaryRecipientListener(null,"尚无收件人");
                            }
                        }

                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onListener.quaryRecipientListener(null,"数据解析出错");
                            return;
                        }
                        onListener.quaryRecipientListener(null,
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
                }
                onListener.quaryRecipientListener(null,ex.getMessage());
                LogUtils.log("responseCode: " + ex.getMessage() );
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.log("收件人管理result: " + result);
                }
            }
        });
    }

    private class QuaryAddressBean{
        private String size;
        private String token;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
