package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.UserInfosResultBean;

/**
 * Created on 2017-02-16
 *
 * @author Json.
 */

public class MineFragmentInterface {
    public interface UserInfosView{
        void showUserInfos(UserInfosResultBean resultBean,String msg);
    }

    public interface GetUserInfosModel{
        void getUserInfos(Context mContext,OnGetUserInfosListener onGetUserInfosListener);
    }

    public interface OnGetUserInfosListener{
        void onListener(UserInfosResultBean resultBean,String msg);
    }

}
