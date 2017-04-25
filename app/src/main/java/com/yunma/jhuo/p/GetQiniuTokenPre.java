package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.QiniuResultBean;
import com.yunma.jhuo.i.GetQiniuTokenImpl;
import com.yunma.jhuo.m.GetQiniuTokenInterface.*;

/**
 * Created on 2017-02-17
 *
 * @author Json.
 */

public class GetQiniuTokenPre {
    private GetQiniuTokenModel mModel;
    private GetQiniuTokenView mView;

    public GetQiniuTokenPre(GetQiniuTokenView mView) {
        this.mView = mView;
        this.mModel = new GetQiniuTokenImpl();
    }

    public void getToken(Context mContext,String type){
        mModel.getQiniuToken(mContext, type, new OnGetQiniuTokenListener() {
            @Override
            public void onListener(QiniuResultBean resultBean, String msg) {
                mView.showQiniuToken(resultBean,msg);
            }
        });
    }
}
