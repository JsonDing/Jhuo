package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.QiniuResultBean;

/**
 * Created on 2017-02-17
 *
 * @author Json.
 */

public class GetQiniuTokenInterface {
    public interface GetQiniuTokenView{
        void showQiniuToken(QiniuResultBean resultBean,String msg);
    }

    public interface GetQiniuTokenModel{
        void getQiniuToken(Context mContext,String type,OnGetQiniuTokenListener onGetQiniuTokenListener);
    }

    public interface OnGetQiniuTokenListener{
        void onListener(QiniuResultBean resultBean,String msg);
    }
}
