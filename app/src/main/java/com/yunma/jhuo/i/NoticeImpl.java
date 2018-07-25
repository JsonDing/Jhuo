package com.yunma.jhuo.i;

import android.content.Context;

import com.google.gson.Gson;
import com.yunma.bean.*;
import com.yunma.jhuo.m.GetNoticeInterFace;
import com.yunma.utils.*;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created on 2017-02-18
 *
 * @author Json.
 */

public class NoticeImpl implements GetNoticeInterFace.NoticeModel {
    private NoticeBean noticeBean = null;
    private FailedResultBean failedResultBean = null;
    @Override
    public void getNotice(final Context mContext,
                          final GetNoticeInterFace.OnNoticeListener onNoticeListener) {

        RequestParams params = new RequestParams(ConUtils.GET_NOTICES);
        PageBean pageBean = new PageBean();
        pageBean.setToken(SPUtils.getToken(mContext));
        pageBean.setSize("99");
        pageBean.setPage("1");
        Gson gson = new Gson();
        String strParams = gson.toJson(pageBean);
        params.setAsJsonContent(true);
        params.setBodyContent(strParams);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    if(result.contains("success")){
                        try {
                            noticeBean = GsonUtils.GsonToBean(result,
                                    NoticeBean.class);
                        } catch (Exception e) {
                            onNoticeListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        if(noticeBean.getSuccess().getList()!=null){
                            onNoticeListener.onListener(noticeBean,"获取成功");
                        }else{
                            onNoticeListener.onListener(noticeBean,"暂无公告");
                        }
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            onNoticeListener.onListener(null,"数据解析出错!");
                            e.printStackTrace();
                            return;
                        }
                        onNoticeListener.onListener(null, failedResultBean.getFailed().getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    onNoticeListener.onListener(null,"网络出错!");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    onNoticeListener.onListener(null,"服务器未响应");
                    LogUtils.json("-----------> " + ex.getMessage() + "\n" + ex.getCause());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.showInfo(mContext,"cancelled");
            }

            @Override
            public void onFinished() {
            }
        });
    }

}
