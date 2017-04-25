package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-04-06
 *
 * @author Json.
 */

public class DelSelfGoodsModelImpl implements SelfGoodsInterFace.DelSelfGoodsModel {
    private SuccessResultBean resultBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void delSelfGoods(Context mContext, String ids,
                             final SelfGoodsInterFace.DelSelfGoodsListener delSelfGoodsListener) {
        RequestParams params = new RequestParams(ConUtils.DEL_SELF_GOODS);
        DeletePublishBean paramsBean = new DeletePublishBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setIds(ids);
        Gson gson = new Gson();
        String strParams = gson.toJson(paramsBean);
        LogUtils.log("请求参数 ---> " + strParams);
        params.setAsJsonContent(true);
        params.setBodyContent(strParams);
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
                            delSelfGoodsListener.delSelfGoodsListene(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(resultBean.getSuccess()!=null){
                            delSelfGoodsListener.delSelfGoodsListene(resultBean,"删除成功");

                        }else{
                            delSelfGoodsListener.delSelfGoodsListene(null,"数据获取失败");
                        }

                    }else{
                        try {
                            failedResultBean = GsonUtils.getObject(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            delSelfGoodsListener.delSelfGoodsListene(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        delSelfGoodsListener.delSelfGoodsListene(null,
                                failedResultBean.getFailed().getErrorMsg());
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
                    delSelfGoodsListener.delSelfGoodsListene(null,"网络出错!");
                    LogUtils.log("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    delSelfGoodsListener.delSelfGoodsListene(null,"服务器未响应");
                    LogUtils.log("onGetSelfGoodsListener -------> " + ex.getMessage() + "\n" + ex.getCause());
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
                    LogUtils.log("自仓推荐: " + result);
                }
            }
        });

    }

    private class DeletePublishBean{

        private String token;
        private String ids;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }
    }
}
