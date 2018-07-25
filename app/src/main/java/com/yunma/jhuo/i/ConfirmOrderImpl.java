package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.ConfirmOrderInterface.*;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created on 2017-01-02
 *
 * @author Json.
 */

public class ConfirmOrderImpl implements ConfirmOrderModel {
    private RecipientManageBean recipientManageBean = null;
    private FailedResultBean failedResultBean = null;
    private List<RecipientManageBean.SuccessBean.ListBean> listBean = null;
    @Override
    public void getDefaultAddress(final Context mContext, final OnGetDefaultAddress onGetDefaultAddress) {
        RequestParams params = new RequestParams(ConUtils.RRECIPIENT_REQURY);
        QuaryAddressBean paramsBean = new QuaryAddressBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setSize("30");
        paramsBean.setToken(SPUtils.getToken(mContext));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strParams = gson.toJson(paramsBean);
        params.setBodyContent(strParams);
        params.setUseCookie(false);
        params.setConnectTimeout(1000*5);
        LogUtils.json("获取默认地址：" + strParams);
        x.http().post(params, new Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                return false;
            }

            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                        try {
                            recipientManageBean = GsonUtils.GsonToBean(result,
                                    RecipientManageBean.class);
                        } catch (Exception e) {
                            onGetDefaultAddress.onShowDefaultAddress(null,"数据解析出错");
                            return;
                        }
                        if(recipientManageBean.getSuccess().getList().size()!=0){
                            listBean = recipientManageBean.getSuccess().getList();
                            getDefault(listBean,onGetDefaultAddress);
                        }else{
                            onGetDefaultAddress.onShowDefaultAddress(null,"尚无默认收件人");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetDefaultAddress.onShowDefaultAddress(null,"数据解析出错");
                            return;
                        }
                        onGetDefaultAddress.onShowDefaultAddress(null,
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
                    onGetDefaultAddress.onShowDefaultAddress(null,"网络错误");
                }else{
                    onGetDefaultAddress.onShowDefaultAddress(null,"服务器错误");
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
                 //   LogUtils.json("确认订单-->收件人result: " + result);
                }
            }
        });
    }

    private void getDefault(List<RecipientManageBean.SuccessBean.ListBean> listBean,
                            OnGetDefaultAddress onGetDefaultAddress) {
            for(int i=0;i<listBean.size();i++){
                if(listBean.get(i).getUsed()==1){
                    onGetDefaultAddress.onShowDefaultAddress(listBean.get(i),"存在默认地址");
                    return;
                }
            }
            onGetDefaultAddress.onShowDefaultAddress(null,"尚无默认收件人");
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
