package com.yunma.jhuo.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.bean.UserInfosResultBean;
import com.yunma.dao.*;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.LoginInterface.LoginView;
import com.yunma.jhuo.m.MineFragmentInterface.UserInfosView;
import com.yunma.jhuo.p.LoginPre;
import com.yunma.jhuo.p.UserInfosPre;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.*;

/**
 * Created on 2016-12-14.
 *
 * @author Json
 */

public class LoginActivity extends MyCompatActivity
        implements LoginView,UserInfosView {

    @BindView(R.id.etLoginName) EditText etLoginName;
    @BindView(R.id.etPasswd) EditText etPasswd;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.viewPasswd) View viewPasswd;
    @BindView(R.id.tvForgetPasswd) TextView tvForgetPasswd;
    @BindView(R.id.ckIsAutoLogin) CheckBox ckIsAutoLogin;
    private CharSequence tempPasswd = null,tempPhone = null;
    private int passwdStart,passwdEnd,phoneStart,phoneEnd;
    private CustomProgressDialog dialog = null;
    private long startDate ;
    private LoginPre loginPre;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initDatas();
        int statusHeight = ScreenUtils.getStatusHeight(this);
        SPUtils.setStatusHeight(this,statusHeight);
    }

    private void initDatas() {
        mContext = this;
        loginPre = new LoginPre(this);
        etLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempPhone = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneStart = etLoginName.getSelectionStart();
                phoneEnd = etLoginName.getSelectionEnd();
                if (tempPhone.length() > 11) {
                    s.delete(phoneStart - 1, phoneEnd);
                    int tempSelection = phoneStart;
                    etLoginName.setText(s);
                    etLoginName.setSelection(tempSelection);
                }else if(tempPhone.length() == 11){
                    viewPasswd.setVisibility(View.GONE);
                }else{
                    if(viewPasswd.getVisibility()==View.GONE){
                        viewPasswd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        etPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempPasswd = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwdStart = etPasswd.getSelectionStart();
                passwdEnd = etPasswd.getSelectionEnd();
                if (tempPasswd.length() > 18) {
                    s.delete(passwdStart - 1, passwdEnd);
                    int tempSelection = passwdStart;
                    etPasswd.setText(s);
                    etPasswd.setSelection(tempSelection);
                }else if(6 <= tempPasswd.length() && tempPasswd.length() <= 18){
                    btnLogin.setEnabled(true);
                    btnLogin.setClickable(true);
                }else{
                    btnLogin.setEnabled(false);
                    btnLogin.setClickable(false);
                }
            }
        });
        etPasswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (etPasswd.getText().toString().trim().length()<6) {
                    ToastUtils.showWarning(getApplicationContext(), "密码长度不少于6位");
                    return true;
                }else{
                    if (actionId == EditorInfo.IME_ACTION_GO ||
                            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        progressShow();
                        loginPre.login(mContext,etLoginName.getText().toString().trim(),
                                etPasswd.getText().toString().trim());
                        return false;
                    }
                    return false;
                }
            }
        });
    }

    @OnClick({R.id.btnLogin, R.id.tvForgetPasswd})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnLogin:
                if(etLoginName.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入账户名！");
                }else if(etPasswd.getText().toString().isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入账户密码！");
                }else {
                    progressShow();
                    loginPre.login(mContext,etLoginName.getText().toString().trim(),
                            etPasswd.getText().toString().trim());
                }
                break;
            case R.id.tvForgetPasswd:
                intent = new Intent(LoginActivity.this, ForgetPassWd.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            etLoginName.setText(data.getExtras().getString("phoneNumber",""));
            etPasswd.setText(data.getExtras().getString("passWd",""));
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showUserInfos(UserInfosResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
            progressDimiss();
        }else{
            if(resultBean.getSuccess()!=null) {
                if (resultBean.getSuccess().getAgent().getName().equals("手机APP")) {
                    SPUtils.setRole(mContext,resultBean.getSuccess().getLvl().getValue());
                    long endDate;
                    long cost;
                    endDate = DateTimeUtils.getCurrentTimeInLong();
                    cost = endDate - startDate;
                    if(cost>2000){
                        progressDimiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        AppManager.getAppManager().finishActivity(this);
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDimiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                AppManager.getAppManager().finishActivity(LoginActivity.this);
                            }
                        }, 2000 - cost);
                    }
                }else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    AppManager.getAppManager().finishActivity(this);
                    }
                    progressDimiss();
                }
            progressDimiss();
        }

    }


    @Override
    public void showLoginInfos(final LoginSuccessResultBean resultBean, final String msg) {
        if (resultBean == null) {
            ToastUtils.showError(mContext, msg);
            progressDimiss();
        } else{
            SPUtils.setPhoneNumber(mContext,etLoginName.getText().toString().trim());
            SPUtils.setPassWd(mContext,etPasswd.getText().toString().trim());
            SPUtils.setToken(mContext,resultBean.getSuccess().getToken());
            updateAppInfos(resultBean.getSuccess().getToken());
            updateUserInfos();
            UserInfosPre userInfosPre = new UserInfosPre(this);
            userInfosPre.getUserInfos(this);
        }
    }

    private void updateUserInfos() {
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                .build();
        List<UserInfos> users = nQuery.list();
        if(users.size()!=0){
            UserInfos userInfos =  new UserInfos(users.get(0).getId(),users.get(0).getUserId(),
                    etLoginName.getText().toString().trim(),
                    etPasswd.getText().toString().trim(),null,ckIsAutoLogin.isChecked(),users.get(0).getNickName(),users.get(0).getGender(),
                    users.get(0).getRealName());
            getUserDao().update(userInfos);
        }else{
            UserInfos userInfos =  new UserInfos(null,null,etLoginName.getText().toString().trim(),
                    etPasswd.getText().toString().trim(),null,ckIsAutoLogin.isChecked(),null,null,null);
            getUserDao().save(userInfos);
        }
    }

    private void updateAppInfos(String token) {
        List<AppInfo> appInfos = getAppDao().loadAll();
        AppInfo infos = new AppInfo(appInfos.get(0).getId(),1,1,token,appInfos.get(0).getIsFirstSetting());
        getAppDao().save(infos);
    }

    public void progressShow() {
        startDate = DateTimeUtils.getCurrentTimeInLong();
        if (dialog == null) {
            dialog = new CustomProgressDialog(LoginActivity.this,"加载中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    private AppInfoDao getAppDao(){
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           AppManager.getAppManager().finishActivity(this);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
