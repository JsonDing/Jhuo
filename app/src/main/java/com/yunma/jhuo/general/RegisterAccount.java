package com.yunma.jhuo.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunma.R;
import com.yunma.bean.RegisterSuccessResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.GetIdentifyingCodeInterface;
import com.yunma.jhuo.m.RegisterInterFace.RegisterView;
import com.yunma.jhuo.p.GetIdentifyingCodePre;
import com.yunma.jhuo.p.RegisterPresenter;
import com.yunma.utils.AppManager;
import com.yunma.utils.MD5Utils;
import com.yunma.utils.RegexValidateUtil;
import com.yunma.utils.SPUtils;
import com.yunma.utils.TimeUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.CustomProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterAccount extends CheckPermissionsActivity
        implements RegisterView,GetIdentifyingCodeInterface.GetIdentifyingCodeView {

    @BindView(R.id.layoutClose) LinearLayout layoutClose;
    @BindView(R.id.etPhoneNum) EditText etPhoneNum;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;
    @BindView(R.id.layoutToLogin) LinearLayout layoutToLogin;
    @BindView(R.id.etCode) EditText etCode;
    @BindView(R.id.tvPasswd) TextView tvPasswd;
    @BindView(R.id.etPasswd) EditText etPasswd;
    @BindView(R.id.tvGetVerification) TextView tvGetVerification;
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.etIntro) EditText etIntro;
    private Context mContext;
    private TimeUtils timeUtils = null;
    private RegisterPresenter mPrenter;
    private GetIdentifyingCodePre getCodePre;
    private CustomProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mContext = this;
        mPrenter = new RegisterPresenter(RegisterAccount.this);
        getCodePre = new GetIdentifyingCodePre(RegisterAccount.this);
        tvPasswd.setText("密" + "\u3000" + "码");
        etCode.addTextChangedListener(new TextWatcher() {
            private CharSequence tempCode = null;
            private int codeStart,codeEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempCode = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                codeStart = etCode.getSelectionStart();
                codeEnd = etCode.getSelectionEnd();
                if (tempCode.length() > 6) {
                    ToastUtils.showError(getApplicationContext(), "你输入的字数已经超过了限制");
                    s.delete(codeStart - 1, codeEnd);
                    int tempSelection = codeStart;
                    etCode.setText(s);
                    etCode.setSelection(tempSelection);
                }
            }
        });
        etPasswd.addTextChangedListener(new TextWatcher() {
            private CharSequence tempPasswd = null;
            private int passwdStart,passwdEnd;
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
                    ToastUtils.showError(getApplicationContext(), "你输入的字数已经超过了限制");
                    s.delete(passwdStart - 1, passwdEnd);
                    int tempSelection = passwdStart;
                    etPasswd.setText(s);
                    etPasswd.setSelection(tempSelection);
                }else if(6 <= tempPasswd.length() && tempPasswd.length() <= 18){
                    btnRegister.setEnabled(true);
                    btnRegister.setClickable(true);
                }else{
                    btnRegister.setEnabled(false);
                    btnRegister.setClickable(false);
                }
            }
        });
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            private CharSequence tempPhone = null;
            private int phoneStart,phoneEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempPhone = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneStart = etPhoneNum.getSelectionStart();
                phoneEnd = etPhoneNum.getSelectionEnd();
                if (tempPhone.length() > 11) {
                    ToastUtils.showError(getApplicationContext(), "你输入的字数已经超过了限制");
                    s.delete(phoneStart - 1, phoneEnd);
                    int tempSelection = phoneStart;
                    etPhoneNum.setText(s);
                    etPhoneNum.setSelection(tempSelection);
                }
            }
        });

    }

    @OnClick({R.id.layoutClose, R.id.layoutToLogin, R.id.tvGetVerification, R.id.btnRegister})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutClose:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(android.R.animator.fade_in,
                        android.R.animator.fade_out);
                break;
            case R.id.layoutToLogin:
                intent = new Intent(RegisterAccount.this, DialogStyleLoginActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.tvGetVerification:
                String phoneNum = etPhoneNum.getText().toString().trim();
                if(phoneNum.isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入您的手机号码！");
                }else if(!RegexValidateUtil.checkCellphone(phoneNum)){
                    ToastUtils.showWarning(mContext,"请输入正确的手机号码！");
                }else{
                    getCodePre.GetIdentifyingCode(this,phoneNum);
                }
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {
        String phoneNum = etPhoneNum.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String passWd = etPasswd.getText().toString().trim();
        String intro = etIntro.getText().toString().trim();
        if (phoneNum.isEmpty()) {
            ToastUtils.showWarning(mContext, "请输入您的手机号码！");
        } else if (!RegexValidateUtil.checkCellphone(phoneNum)) {
            ToastUtils.showWarning(mContext, "请输入正确的手机号码！");
        } else if (code.isEmpty()) {
            ToastUtils.showWarning(mContext, "请输入您收到的验证码！");
        } else if (passWd.isEmpty()) {
            ToastUtils.showWarning(mContext, "请输入您的登录密码！");
        } else if (passWd.length() < 6) {
            ToastUtils.showInfo(mContext, "密码不能少于6位！");
        }else{
            progressShow();
            mPrenter.register(this,phoneNum,passWd,code,intro);
            // 提交注册信息
        }
    }

    @Override
    public void onShowRegisterMsg(final RegisterSuccessResultBean resultBean, final String msg) {
        if (resultBean == null) {
            ToastUtils.showError(mContext, msg);
            progressDimiss();
        } else {
            progressDimiss();
            ToastUtils.show(mContext, "注册成功",Toast.LENGTH_SHORT);
            SPUtils.setUserId(mContext, String.valueOf(resultBean.getSuccess().getId()));
            SPUtils.setPhoneNumber(mContext, etPhoneNum.getText().toString().trim());
            SPUtils.setPassWd(mContext, etPasswd.getText().toString().trim());
            saveDataToDB(resultBean.getSuccess());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(RegisterAccount.this,DialogStyleLoginActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(RegisterAccount.this);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }, 1500);
        }
    }

    private void saveDataToDB(RegisterSuccessResultBean.SuccessBean successBean) {
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(etPhoneNum.getText().toString()))
                .build();
        List<UserInfos> userInfoses = nQuery.list();
        if (userInfoses.size() == 0) {
            //插入数据
            UserInfos userInfos = new UserInfos();
            userInfos .setId(null);
            userInfos.setUserId(successBean.getId());
            userInfos.setPhoneNumber(successBean.getTel());
            userInfos.setPassWd(MD5Utils.getMD5(successBean.getPass()));
            getUserDao().insert(userInfos);
        }
    }

    public void progressShow() {
        if (dialog == null) {
            dialog = new CustomProgressDialog(this, "加载中...", R.drawable.pb_loading_logo_1);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeUtils != null) {
            timeUtils.destroyHandler();
        }
        if (dialog != null) {
            dialog = null;
        }
        //要在activity销毁时反注册，否侧会造成内存泄漏问题
        mPrenter = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            AppManager.getAppManager().finishActivity(this);
            overridePendingTransition(R.anim.fade_in,
                    R.anim.fade_out);
        }
        return false;
    }

    /**
     * 短信验证码
     * @param resultBean
     * @param msg
     */
    @Override
    public void showIdentifyingCodeResult(SuccessResultBean resultBean, String msg) {
        if(resultBean != null){
            timeUtils = new TimeUtils(mContext, tvGetVerification,
                    "获取验证码", 180,R.drawable.bg_button_orange);
            timeUtils.RunTimer();
            ToastUtils.show(this,msg);
        }else{
            ToastUtils.show(this,msg);
        }
    }
}
