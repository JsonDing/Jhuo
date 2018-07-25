package com.yunma.jhuo.i;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yunma.bean.FailedResultBean;
import com.yunma.bean.SelfGoodsResultBean;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.m.SelfGoodsInterFace.OnGetSelfGoodsListener;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-13
 *
 * @author Json.
 */

public class GetSelfGoodsImpl implements SelfGoodsInterFace.GetSelfGoodsModel {

    private SelfGoodsResultBean resualtBean = null;
    private FailedResultBean failedResultBean = null;

    @Override
    public void getSpecialOfferGoods(final Context mContext,String requestType,String size,
                                     int nextPage,String sort,String desc,
                                     final OnGetSelfGoodsListener onGetSelfGoodsListener) {
        RequestParams params = new RequestParams(ConUtils.GET_SELF_GOODS);
        GetSelfGoodsBean paramsBean = new GetSelfGoodsBean();
        paramsBean.setToken(SPUtils.getToken(mContext));
        paramsBean.setType(requestType);
        paramsBean.setSort(sort);
        paramsBean.setDesc(desc);
        paramsBean.setSize(size);
        paramsBean.setPage(String.valueOf(nextPage));
        Gson gson = new Gson();
        String strParams = gson.toJson(paramsBean);
        LogUtils.json("查询全部商品：" + strParams);
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
                            resualtBean = GsonUtils.GsonToBean(result,
                                    SelfGoodsResultBean.class);
                        } catch (Exception e) {
                            onGetSelfGoodsListener.onGetGoodsListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(resualtBean.getSuccess()!= null&&
                                resualtBean.getSuccess().getList()!=null){
                            onGetSelfGoodsListener.onGetGoodsListener(resualtBean,"商品获取成功");
                        }else{
                            onGetSelfGoodsListener.onGetGoodsListener(null,"数据获取失败");
                        }

                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onGetSelfGoodsListener.onGetGoodsListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onGetSelfGoodsListener.onGetGoodsListener(null,
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
                    onGetSelfGoodsListener.onGetGoodsListener(null,"网络出错");
                    LogUtils.json("自仓推荐:responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onGetSelfGoodsListener.onGetGoodsListener(null,"服务器未响应");
                    LogUtils.json("自仓推荐: -------> " + ex.getMessage() + "\n" + ex.getCause());
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
                    LogUtils.json("自仓推荐: " + result);
                }
            }
        });
    }

    private class GetSelfGoodsBean {

        private String token;
        private String type;
        private String sort;
        private String desc;
        private String size;
        private String page;


        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
