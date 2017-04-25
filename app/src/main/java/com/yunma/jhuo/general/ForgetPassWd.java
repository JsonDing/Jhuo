package com.yunma.jhuo.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.m.ForgetPasswdInterface.ForgetPasswdView;
import com.yunma.jhuo.m.PhoneNumberVerificationInterFace.PhoneMumberView;
import com.yunma.jhuo.p.ForgetPassWdPre;
import com.yunma.jhuo.p.PhoneNumberVerificationPre;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;

import butterknife.*;

public class ForgetPassWd extends MyCompatActivity implements PhoneMumberView,ForgetPasswdView {


    @BindView(com.yunma.R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.etPhone) EditText etPhone;
    @BindView(R.id.layoutGetCode) LinearLayout layoutGetCode;
    @BindView(R.id.layout) LinearLayout layout;
    @BindView(R.id.etCode) EditText etCode;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;
    @BindView(R.id.etPasswd) EditText etPasswd;
    @BindView(R.id.view1) View view1;
    @BindView(R.id.btnConfirm) Button btnConfirm;
    @BindView(R.id.tvCode) TextView tvCode;
    private CharSequence tempPhone = null;
    private int phoneStart;
    private int phoneEnd;
    private CharSequence tempCode = null;
    private int codeStart;
    private int codeEnd;
    private CharSequence tempPasswd = null;
    private int passwdStart;
    private int passwdEnd;
    private Context mContext;
    private boolean isTrue;
    private long startDate ;
    private TimeUtils timeUtils = null;
    private CustomProgressDialog dialog = null;
    private ForgetPassWdPre mPresenter;
    private PhoneNumberVerificationPre nPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass_wd);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mContext = this;
        mPresenter = new ForgetPassWdPre(this);
        nPresenter = new PhoneNumberVerificationPre(this);
        int statusHeight = ScreenUtils.getStatusHeight(ForgetPassWd.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempPhone = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneStart = etPhone.getSelectionStart();
                phoneEnd = etPhone.getSelectionEnd();
                if (tempPhone.length() > 11) {
                    ToastUtils.showError(getApplicationContext(),"你输入的字数已经超过了限制");
                    s.delete(phoneStart - 1, phoneEnd);
                    int tempSelection = phoneStart;
                    etPhone.setText(s);
                    etPhone.setSelection(tempSelection);
                }
            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
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
                if (tempCode.length() > 4) {
                    ToastUtils.showError(getApplicationContext(),"你输入的字数已经超过了限制");
                    s.delete(codeStart - 1, codeEnd);
                    int tempSelection = codeStart;
                    etCode.setText(s);
                    etCode.setSelection(tempSelection);
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
                    ToastUtils.showError(getApplicationContext(),"你输入的字数已经超过了限制");
                    s.delete(passwdStart - 1, passwdEnd);
                    int tempSelection = passwdStart;
                    etPasswd.setText(s);
                    etPasswd.setSelection(tempSelection);
                }
            }
        });
    }


    @OnClick({R.id.layoutBack, R.id.layoutGetCode, R.id.btnConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutGetCode:
                String phoneNum = etPhone.getText().toString().trim();
                if (phoneNum.isEmpty()) {
                    ToastUtils.showWarning(mContext, "请输入您的手机号码！");
                } else if (RegexValidateUtil.checkCellphone(phoneNum)) {
                    ToastUtils.showWarning(mContext, "请输入正确的手机号码！");
                } else {
                    timeUtils = new TimeUtils(mContext, tvCode, "获取验证码", 120,R.drawable.bg_button_orange);
                    timeUtils.RunTimer();
                    nPresenter.getVerificationCode(phoneNum);
                }
                break;
            case R.id.btnConfirm:
                ModifyPasswd();
                break;
        }
    }

    private void ModifyPasswd() {
        String phoneNum = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String passWd = etPasswd.getText().toString().trim();
        if(phoneNum.isEmpty()){
            ToastUtils.showWarning(mContext,"请输入您的手机号码！");
        }else if(RegexValidateUtil.checkCellphone(phoneNum)){
            ToastUtils.showWarning(mContext,"请输入正确的手机号码！");
        }else if(code.isEmpty()){
            ToastUtils.showWarning(mContext,"请输入您收到的验证码！");
        }else if(passWd.isEmpty()){
            ToastUtils.showWarning(mContext,"请输入您的登录密码！");
        }else if(passWd.length()<6){
            ToastUtils.showInfo(mContext,"密码不能少于6位！");
        }else{
            progressShow();
            nPresenter.verifyingPhoneNumber(phoneNum,code);
        }
    }

    @Override
    public void showIsCorrent(String msg) {
        if (msg.equals("验证成功")) {
            LogUtils.log("验证成功");
            mPresenter.modifyPasswd(getApplicationContext(),
                    etPhone.getText().toString().trim(),
                    etPasswd.getText().toString().trim());
        }else if(msg.equals("验证失败")){
            LogUtils.log("验证失败");
        }
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public void progressShow() {
        startDate = DateTimeUtils.getCurrentTimeInLong();
        LogUtils.log("startDate: " + startDate);
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
    public void showModifyInfos(SuccessResultBean successResultBean, String msg) {
        if (successResultBean==null){
            ToastUtils.showError(mContext,msg);
            progressDimiss();
        }else{
            ToastUtils.showSuccess(mContext,msg);
            long endDate = DateTimeUtils.getCurrentTimeInLong();
            long cost = endDate - startDate;
            if(cost>2000){
                progressDimiss();
                Intent intent = new Intent();
                intent.putExtra("phoneNumber",etPhone.getText().toString().trim());
                intent.putExtra("passWd",etPasswd.getText().toString().trim());
                this.setResult(1, intent);
                AppManager.getAppManager().finishActivity(this);
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDimiss();
                        Intent intent = new Intent();
                        intent.putExtra("phoneNumber",etPhone.getText().toString().trim());
                        intent.putExtra("passWd",etPasswd.getText().toString().trim());
                        ForgetPassWd.this.setResult(1, intent);
                        AppManager.getAppManager().finishActivity(ForgetPassWd.this);
                    }
                }, 2000 - cost);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timeUtils!=null){
            timeUtils.destroyHandler();
        }
        if(dialog!=null){
            dialog = null;
        }
        //要在activity销毁时反注册，否侧会造成内存泄漏问题
        nPresenter.unregisterAllEventHandler();
    }
}
