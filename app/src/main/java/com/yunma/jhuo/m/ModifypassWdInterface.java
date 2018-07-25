package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;

/**
 * Created on 2017-02-15
 *
 * @author Json.
 */

public class ModifypassWdInterface {
    public interface ModifypassWdView{
        void showModifyInfos(SuccessResultBean successResultBean,String msg);
    }

    public interface ModifyPassWdModel{
        void modifyPassWd(Context context,String originalPassWd,String newPassWd,
                          OnModifyPassWdListener onModifyPassWdListener);
    }

    public interface OnModifyPassWdListener{
        void onListener(SuccessResultBean successResultBean,String msg);
    }
}
