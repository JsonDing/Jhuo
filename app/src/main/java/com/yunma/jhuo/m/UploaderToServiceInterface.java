package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;

/**
 * Created on 2017-02-18
 *
 * @author Json.
 */

public class UploaderToServiceInterface {
    public interface UploaderToServiceModel{
        void uploader(Context mContext,String path,
                      OnUploaderToServiceListener onListener);
    }

    public interface UploaderToServiceView{
        void showUploaderInfos(SuccessResultBean resultBean,String msg);
    }

    public interface OnUploaderToServiceListener{
        void OnUploaderListener(SuccessResultBean resultBean,String msg);
    }

}
