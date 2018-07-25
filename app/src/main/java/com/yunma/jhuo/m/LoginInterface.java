package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.LoginSuccessResultBean;

/**
 * Created on 2017-01-21
 *
 * @author Json.
 */

public class LoginInterface {

    public interface LoginModel {
        void login(Context context,String username, String password, OnLoginListener loginListener);
    }

    public interface LoginView {
        void showLoginInfos(LoginSuccessResultBean resultBean , String msg);
    }

    public interface LoginDialogView{
        void showLoginInfos(LoginSuccessResultBean resultBean , String msg);
    }

    public interface OnLoginListener {
        void showLoginInfos(LoginSuccessResultBean resultBean, String msg);
    }
}
