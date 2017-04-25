package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.jhuo.i.LoginModelImpl;
import com.yunma.jhuo.m.LoginInterface.*;
/**
 * Created on 2016-12-02.
 * @author Json
 */

public class LoginPre {
    private LoginModel loginModel;
    private LoginView  loginView;
    private LoginDialogView loginDialogView;

    public LoginPre(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }

    public LoginPre(LoginDialogView loginDialogView) {
        this.loginDialogView = loginDialogView;
        this.loginModel = new LoginModelImpl();
    }



    public void login(Context context,String phoneNum,String passwd){
        loginModel.login(context,phoneNum, passwd, new OnLoginListener() {
                    @Override
                    public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
                        loginView.showLoginInfos(resultBean,msg);
                    }
                });
    }

    public void loginDialog(Context context,String name, String passwd){
        loginModel.login(context, name, passwd, new OnLoginListener() {
            @Override
            public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
                loginDialogView.showLoginInfos(resultBean,msg);
            }
        });
    }
}
