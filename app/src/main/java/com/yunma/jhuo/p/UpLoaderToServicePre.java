package com.yunma.jhuo.p;


import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.UploaderToServiceImpl;
import com.yunma.jhuo.m.UploaderToServiceInterface.*;

/**
 * Created on 2017-02-18
 *
 * @author Json.
 */

public class UpLoaderToServicePre {
    private UploaderToServiceModel mModel;
    private UploaderToServiceView mView;

    public UpLoaderToServicePre(UploaderToServiceView mView) {
        this.mView = mView;
        this.mModel = new UploaderToServiceImpl();
    }

    public void uPLoaderToService(Context context,String imgUrl){
        mModel.uploader(context, imgUrl, new OnUploaderToServiceListener() {
            @Override
            public void OnUploaderListener(SuccessResultBean resultBean, String msg) {
                mView.showUploaderInfos(resultBean,msg);
            }
        });
    }
}
