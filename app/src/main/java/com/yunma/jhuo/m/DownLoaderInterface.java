package com.yunma.jhuo.m;

import android.app.Activity;

/**
 * Created on 2017-04-07
 *
 * @author Json.
 */

public class DownLoaderInterface {

    public interface DownLoadModel{
        void downLoad(Activity mActivity,String url, String path
                ,OnDownLoadListener onDownLoadListener);
    }

    public  interface DownLoadView{
        void showDownLoadInfo(String msg,String path);
    }

    public interface OnDownLoadListener{
        void onDownLoadListener(String msg,String path);
    }
}
