package com.yunma.jhuo.general;

import android.content.*;
import android.os.*;
import android.text.*;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.bean.*;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.LoginInterface.LoginView;
import com.yunma.jhuo.m.PhoneNumberVerificationInterFace.PhoneMumberView;
import com.yunma.jhuo.m.RegisterInterFace.RegisterView;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;

public class RegisterAccount extends CheckPermissionsActivity
        implements RegisterView, PhoneMumberView, LoginView {

    @BindView(R.id.layoutClose) LinearLayout layoutClose;
    @BindView(R.id.etPhoneNum) EditText etPhoneNum;
    @BindView(R.id.linearLayout) LinearLayout linearLayout;
    @BindView(R.id.layoutToLogin) LinearLayout layoutToLogin;
    @BindView(R.id.etCode) EditText etCode;
    @BindView(R.id.tvPasswd) TextView tvPasswd;
    @BindView(R.id.etPasswd) EditText etPasswd;
    @BindView(R.id.tvGetVerification) TextView tvGetVerification;
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.codeMask) View codeMask;
    @BindView(R.id.passwdMask) View passwdMask;
    private CharSequence tempPhone = null,tempPasswd = null,tempCode = null;
    private int phoneStart,phoneEnd,passwdStart,passwdEnd,codeStart,codeEnd;
    private Context mContext;
    private TimeUtils timeUtils = null;
    private RegisterPresenter mPrenter;
    private PhoneNumberVerificationPre nPresenter;
    private LoginPre loginPre;
    private CustomProgressDialog dialog = null;
    private long startDate;

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
        loginPre = new LoginPre(RegisterAccount.this);
        nPresenter = new PhoneNumberVerificationPre(RegisterAccount.this);
        tvPasswd.setText("密" + "\u3000" + "码");
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
                    ToastUtils.showError(getApplicationContext(), "你输入的字数已经超过了限制");
                    s.delete(codeStart - 1, codeEnd);
                    int tempSelection = codeStart;
                    etCode.setText(s);
                    etCode.setSelection(tempSelection);
                }else if(tempCode.length()==4){
                    passwdMask.setVisibility(View.GONE);
                }else{
                    passwdMask.setVisibility(View.VISIBLE);
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
                overridePendingTransition(0,R.anim.fade_out);
                break;
            case R.id.tvGetVerification:
                String phoneNum = etPhoneNum.getText().toString().trim();
                if(phoneNum.isEmpty()){
                    ToastUtils.showWarning(mContext,"请输入您的手机号码！");
                }else if(!RegexValidateUtil.checkCellphone(phoneNum)){
                    ToastUtils.showWarning(mContext,"请输入正确的手机号码！");
                }else{
                    timeUtils = new TimeUtils(mContext, tvGetVerification,
                            "获取验证码", 120,R.drawable.bg_button_orange);
                    timeUtils.RunTimer();
                    codeMask.setVisibility(View.GONE);
                    nPresenter.getVerificationCode(phoneNum);
                }
             //   showDialog();
                break;
            case R.id.btnRegister:
                register();
              //  showDialog();
                break;

        }
    }

    private void showDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 52);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.item_regidst_notice)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        ViewHolder holder = new ViewHolder(contentView);
                        holder.tvQQ.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QQAuth mqqAuth = QQAuth.createInstance("1105961309",RegisterAccount.this);
                                WPA mWPA = new WPA(RegisterAccount.this, mqqAuth.getQQToken());
                                String ESQ = "2252162352";  //客服QQ号
                                int ret = mWPA.startWPAConversation(RegisterAccount.this,ESQ,
                                        "你好，我正在J货看,想知道注册帐号等事宜");
                                if (ret != 0) {
                                    Toast.makeText(getApplicationContext(),
                                            "抱歉，联系客服出现了错误~. error:" + ret,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        holder.btnKnow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppManager.getAppManager().finishActivity(RegisterAccount.this);
                                overridePendingTransition(0,
                                        android.R.animator.fade_out);
                            }
                        });
                    }
                })
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    private void register() {
        String phoneNum = etPhoneNum.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String passWd = etPasswd.getText().toString().trim();
        if (phoneNum.isEmpty()) {
            ToastUtils.showWarning(mContext, "请输入您的手机号码！");
        } else if (RegexValidateUtil.checkCellphone(phoneNum)) {
            ToastUtils.showWarning(mContext, "请输入正确的手机号码！");
        } else if (code.isEmpty()) {
            ToastUtils.showWarning(mContext, "请输入您收到的验证码！");
        } else if (passWd.isEmpty()) {
            ToastUtils.showWarning(mContext, "请输入您的登录密码！");
        } else if (passWd.length() < 6) {
            ToastUtils.showInfo(mContext, "密码不能少于6位！");
        } else {
            progressShow();
            nPresenter.verifyingPhoneNumber(phoneNum,code);
        }
    }

    @Override
    public void onShowRegisterMsg(final RegisterSuccessResultBean resultBean, final String msg) {
        if (resultBean == null) {
            ToastUtils.showError(mContext, msg);
            progressDimiss();
        } else {
            ToastUtils.showSuccess(mContext, "注册成功");
            SPUtils.setUserId(mContext, resultBean.getSuccess().getId());
            SPUtils.setPhoneNumber(mContext, etPhoneNum.getText().toString().trim());
            SPUtils.setPassWd(mContext, etPasswd.getText().toString().trim());
            saveDataToDB(resultBean.getSuccess());
            loginPre.login(mContext,etPhoneNum.getText().toString().trim(),
                    etPasswd.getText().toString().trim());
        }
    }

    private void saveDataToDB(RegisterSuccessResultBean.SuccessBean successBean) {
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(etPhoneNum.getText().toString()))
                .build();
        List<UserInfos> userInfoses = nQuery.list();
        if (userInfoses.size() == 0) {
            //插入数据
            UserInfos userInfos = new UserInfos(null, successBean.getId(), successBean.getTel(),
                    successBean.getPass(), null, false, null, null, null);
            LogUtils.log("插入数据 ------> " + userInfos.toString());
            getUserDao().insert(userInfos);
        }
    }

    @Override
    public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
        if (resultBean != null) {
            long endDate = DateTimeUtils.getCurrentTimeInLong();
            long cost = endDate - startDate;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDimiss();
                    AppManager.getAppManager().finishActivity(RegisterAccount.this);
                    overridePendingTransition(0,
                            android.R.animator.fade_out);
                }
            }, 2000 - cost);
        } else {
            ToastUtils.showError(mContext, "自动登录失败，请手动登录");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDimiss();
                    Intent intent = new Intent(RegisterAccount.this, DialogStyleLoginActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(RegisterAccount.this);
                    overridePendingTransition(0,
                            android.R.animator.fade_out);
                }
            }, 1000);
        }


    }

    @Override
    public void showIsCorrent(String msg) {
        if (msg.equals("验证成功")) {
            LogUtils.log("验证成功");
            mPrenter.register(getApplicationContext(),
                    etPhoneNum.getText().toString().trim(),
                    etPasswd.getText().toString().trim());
        }else if(msg.equals("验证失败")){
            LogUtils.log("验证失败");
        }
    }

    public void progressShow() {
        startDate = DateTimeUtils.getCurrentTimeInLong();
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
        nPresenter.unregisterAllEventHandler();
        nPresenter = null;
        mPrenter = null;
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    static class ViewHolder {
        @BindView(R.id.tvQQ)
        TextView tvQQ;
        @BindView(R.id.btnKnow)
        Button btnKnow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
