package com.yunma.jhuo.p;

import android.app.Activity;

import com.yunma.jhuo.i.DownLoaderImpl;
import com.yunma.jhuo.m.DownLoaderInterface;

/**
 * Created on 2017-04-07
 *
 * @author Json.
 */

public class DownLoaderPre {
    private DownLoaderInterface.DownLoadModel mModel;
    private DownLoaderInterface.DownLoadView mView;

    public DownLoaderPre(DownLoaderInterface.DownLoadView mView) {
        this.mView = mView;
        this.mModel = new DownLoaderImpl();
    }

    public void startDown(Activity mActivity,String url, String path){
        mModel.downLoad(mActivity, url, path, new DownLoaderInterface.OnDownLoadListener() {
            @Override
            public void onDownLoadListener(String msg,String path) {
                mView.showDownLoadInfo(msg,path);
            }
        });
    }
}
