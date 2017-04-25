package com.yunma.jhuo.i;

import android.app.Activity;
import android.app.ProgressDialog;

import com.yunma.jhuo.m.DownLoaderInterface;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created on 2017-04-07
 *
 * @author Json.
 */

public class DownLoaderImpl implements DownLoaderInterface.DownLoadModel {
    private ProgressDialog progressDialog;

    @Override
    public void downLoad(final Activity mActivity, final String url, String path,final
                         DownLoaderInterface.OnDownLoadListener onDownLoadListener) {
        progressDialog = new ProgressDialog(mActivity);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("亲，努力下载中。。。");
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                onDownLoadListener.onDownLoadListener("下载成功");
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                onDownLoadListener.onDownLoadListener("下载失败，请检查网络和SD卡");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });

    }

}
