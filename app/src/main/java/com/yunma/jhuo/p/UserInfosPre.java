package com.yunma.jhuo.p;

import android.app.Activity;

import com.yunma.bean.UserInfosResultBean;
import com.yunma.jhuo.i.UserInfosImpl;
import com.yunma.jhuo.m.MineFragmentInterface.*;
/**
 * Created on 2017-02-16
 *
 * @author Json.
 */

public class UserInfosPre {
    private UserInfosView mView;
    private GetUserInfosModel mModel;

    public UserInfosPre(UserInfosView mView) {
        this.mView = mView;
        this.mModel = new UserInfosImpl();
    }

    public void getUserInfos(Activity mActivity){

        mModel.getUserInfos(mActivity, new OnGetUserInfosListener() {
            @Override
            public void onListener(UserInfosResultBean resultBean, String msg) {
                mView.showUserInfos(resultBean,msg);
            }
        });
    }
}
