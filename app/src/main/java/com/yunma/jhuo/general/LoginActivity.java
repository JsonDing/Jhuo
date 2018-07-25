package com.yunma.jhuo.general;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.CouponsBean;
import com.yunma.bean.InfoBean;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.bean.RecipientManageBean;
import com.yunma.dao.AppInfo;
import com.yunma.dao.ConsigneeAddress;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.ConsigneeAddressDao;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.m.ConsigneeAddressInterface.ObtainConsigneeAddressView;
import com.yunma.jhuo.m.LoginInterface.LoginView;
import com.yunma.jhuo.p.GetConsigneePre;
import com.yunma.jhuo.p.LoginPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.MD5Utils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.CustomProgressDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yunma.dao.GreenDaoUtils.getContactsDao;

//import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created on 2016-12-14.
 *
 * @author Json
 */

public class LoginActivity extends MyCompatActivity implements
        LoginView, ObtainConsigneeAddressView, View.OnClickListener {

    @BindView(R.id.etLoginName) EditText etLoginName;
    @BindView(R.id.etPasswd) EditText etPasswd;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.viewPasswd) View viewPasswd;
    @BindView(R.id.tvForgetPasswd) TextView tvForgetPasswd;
    @BindView(R.id.ckIsAutoLogin) CheckBox ckIsAutoLogin;
    private CustomProgressDialog dialog = null;
    private long startDate ;
    private LoginPre loginPre;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        SPUtils.setStatusHeight(this, ScreenUtils.getStatusHeight(this));
        initDatas();

    }

    private void initDatas() {
        mContext = this;
        SPUtils.setStatusHeight(this,SPUtils.getStatusHeight(this));
        etLoginName.addTextChangedListener(new TextWatcher() {
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
                    loginPre = new LoginPre(this);
                    String passsWd = etPasswd.getText().toString().trim();
                    String account = etLoginName.getText().toString().trim();
                    loginPre.login(mContext, account, MD5Utils.getMD5(passsWd));
                }
                break;
            case R.id.tvForgetPasswd:
                intent = new Intent(LoginActivity.this, ForgetPassWdActivity.class);
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
    public void showLoginInfos(final LoginSuccessResultBean resultBean, final String msg) {
        if (resultBean == null) {
            ToastUtils.showError(mContext, msg);
            progressDimiss();
        } else{
            String token = resultBean.getSuccess().getToken();
            saveSharePreferences(token,resultBean.getSuccess());
            uploadConsigneeAddress();
            updateAppInfos(resultBean.getSuccess().getToken());
            updateUserInfos(resultBean.getSuccess().getInfo());
            if(resultBean.getSuccess().getCoupons().size()!= 0){ //判断是否可以获取赠送的优惠券
                showDialog(resultBean.getSuccess().getCoupons().get(0));
            }else {
                goToMainActivity();
            }
        }
    }

    private void saveSharePreferences(String token, LoginSuccessResultBean.SuccessBean useInfo) {
        SPUtils.setToken(this, token);
        InfoBean.LvlBean mLvl = useInfo.getInfo().getLvl();
        if (mLvl != null){
            SPUtils.setRole(this, mLvl.getValue()); // 角色：运营、移动用户、、、
            SPUtils.setRoleId(this,mLvl.getId()); // 角色ID
        }
        SPUtils.setAgentId(this,useInfo.getAgent().getId());
        SPUtils.setAgentDiscount(this,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getDiscount()));
        SPUtils.setAgentName(this,useInfo.getAgent().getName());
        String mNick = useInfo.getAgent().getNick();
        if (mNick != null){
            SPUtils.setAgentNick(this,useInfo.getAgent().getNick());
        }else {
            SPUtils.setAgentNick(this,"");
        }
        SPUtils.setParentDiscount(this,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getParentDiscount()));
        SPUtils.setRootDiscount(this,
                ValueUtils.toTwoDecimal(useInfo.getAgent().getRootDiscount()));
        SPUtils.setAgentPoints(this,useInfo.getAgent().getPoints());

        SPUtils.setUserId(this,String.valueOf(useInfo.getInfo().getId()));
        SPUtils.setIntegral(this,useInfo.getInfo().getPoints());
        String phoneNumber = etLoginName.getText().toString().trim();
        String passWd = etPasswd.getText().toString().trim();
        SPUtils.setPhoneNumber(mContext,phoneNumber);
        SPUtils.setPassWd(mContext, MD5Utils.getMD5(passWd));
        int isAgent = useInfo.getInfo().getIsAgent();
        if (isAgent == 0){
            SPUtils.setIsAnget(this,false);
        } else {
            SPUtils.setIsAnget(this,true);
        }
    }

    private void updateAppInfos(String token) {
        AppInfoDao appInfoDao = GreenDaoManager.getInstance().getSession().getAppInfoDao();
        List<AppInfo> appInfos = appInfoDao.loadAll();
        AppInfo infos = new AppInfo(appInfos.get(0).getId(),1,1,
                token,appInfos.get(0).getIsFirstSetting());
        appInfoDao.save(infos);
    }

    private void updateUserInfos(InfoBean useInfo) {
        UserInfos userInfos = getUserDao().load(
                Long.valueOf(String.valueOf(useInfo.getId())));
        if ( userInfos == null){
            // 保存
            saveInfo(useInfo);
        } else {
            // 更新
            updataInfo(useInfo,userInfos);
        }
    }

    private void saveInfo(InfoBean useInfo) {
        LogUtils.test("保存用户信息。。。");
        UserInfos ui=  new UserInfos();
        ui.setId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setUserId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setPhoneNumber(useInfo.getTel());
        ui.setPassWd(useInfo.getPass());
        ui.setImgsPhotos(useInfo.getHeadImg());
        ui.setIsAutoLogin(ckIsAutoLogin.isChecked());
        ui.setNickName(useInfo.getName());
        ui.setRealName(useInfo.getUser());
        ui.setQq(useInfo.getQq());
        if (useInfo.getLvl() != null){
            ui.setRoleName(useInfo.getLvl().getValue());
            ui.setRoleId(useInfo.getLvl().getId());
        }
        ui.setPoints(useInfo.getPoints());
        getUserDao().insert(ui);
    }

    private void updataInfo(InfoBean useInfo, UserInfos userInfos) {
        LogUtils.test("更新用户信息。。。");
        UserInfos ui=  new UserInfos();
        ui.setId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setUserId(Long.valueOf(String.valueOf(useInfo.getId())));
        ui.setPhoneNumber(useInfo.getTel());
        ui.setPassWd(useInfo.getPass());
        ui.setImgsPhotos(useInfo.getHeadImg());
        ui.setIsAutoLogin(ckIsAutoLogin.isChecked());
        ui.setNickName(useInfo.getName());
        ui.setGender(userInfos.getGender());
        ui.setRealName(useInfo.getUser());
        ui.setQq(useInfo.getQq());
        ui.setWeChat(userInfos.getWeChat());
        if (useInfo.getLvl() != null){
            ui.setRoleName(useInfo.getLvl().getValue());
            ui.setRoleId(useInfo.getLvl().getId());
        }
        ui.setPoints(useInfo.getPoints());
        getUserDao().update(ui);
    }

    private void showDialog(CouponsBean coupons) {
        String strCondition = "";
        String strVolumeUseRange = "";
        if (coupons.getType() == 1) {
            strCondition = "包邮";
            strVolumeUseRange = "邮费抵扣券";
        } else if (coupons.getType() == 2) {
            strCondition = coupons.getAstrict() + "件内可用";
            strVolumeUseRange = "邮费抵扣券";
        } else if (coupons.getType() == 3) {
            strCondition = "不限件包邮";
            strVolumeUseRange = "邮费抵扣券";
        } else if (coupons.getType() == 4) {
            strCondition = "满" + coupons.getAstrict() + "可用";
            strVolumeUseRange = "满减券";
        } else if (coupons.getType() == 5) {
            strCondition = "不限消费金额";
            strVolumeUseRange = "满减券";
        }
        final Dialog mDialog = new Dialog(this, R.style.CenterDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.show_largess_coupon, null);
        //初始化视图
        root.findViewById(R.id.btnKnow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                goToMainActivity();
            }
        });
        TextView tvMoney = root.findViewById(R.id.tvMoney);
        TextView tvCondition = root.findViewById(R.id.tvCondition);
        TextView tvVolumeName = root.findViewById(R.id.tvVolumeName);
        TextView tvVolumeUseRange = root.findViewById(R.id.tvVolumeUseRange);
        tvMoney.setText(String.valueOf(coupons.getMoney()));
        tvCondition.setText(strCondition);
        tvVolumeName.setText(coupons.getName());
        tvVolumeUseRange.setText(strVolumeUseRange);
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = getResources().getDisplayMetrics().widthPixels - DensityUtils.dp2px(this,32); // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mDialog.dismiss();
                goToMainActivity();
            }
        });
        mDialog.show();
    }

    private void goToMainActivity() {
        long endDate,cost;
        endDate = DateTimeUtils.getCurrentTimeInLong();
        cost = endDate - startDate;
        if (cost > 2000) {
            progressDimiss();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(LoginActivity.this);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDimiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    AppManager.getAppManager().finishActivity(LoginActivity.this);
                }
            }, 2000 - cost);
        }
        progressDimiss();
    }

    private void uploadConsigneeAddress() {
        getAddressDao().deleteAll();
        GetConsigneePre mPresenter = new GetConsigneePre(this);
        mPresenter.obtainConsignee(this);
    }

    private ConsigneeAddressDao getAddressDao() {
        return GreenDaoManager.getInstance().getSession().getConsigneeAddressDao();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           showDialog();
        }
        return false;
    }

    private void showDialog(){
        final Dialog mCameraDialog = new Dialog(this,R.style.CenterDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.drop_out_dialog, null);
        //初始化视图
        root.findViewById(R.id.tvCancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraDialog.dismiss();
            }
        });
        root.findViewById(R.id.tvDropOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlideUtils.glidClearDisk(LoginActivity.this);
                getContactsDao().deleteAll();
                List<AppInfo> loginInfo = getAppInfoDao().loadAll();
                if (loginInfo.size() != 0) {
                    loginInfo.get(0).setIsLogin(0);
                }
                MainActivity.mainActivity.finish();
                AppManager.getAppManager().AppExit();
            }
        });
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = getResources().getDisplayMetrics().widthPixels
                - DensityUtils.dp2px(this,32); // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    private AppInfoDao getAppInfoDao() {
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    @Override
    public void showConsigneeAddress(RecipientManageBean resultBean, String msg) {
        if(resultBean!=null){
            final List<RecipientManageBean.SuccessBean.ListBean>
                    addressListBean = resultBean.getSuccess().getList();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<addressListBean.size();i++){
                        ConsigneeAddress address = new ConsigneeAddress();
                        address.setId(null);
                        address.setAddressId(String.valueOf(addressListBean.get(i).getId()));
                        address.setConsignee(addressListBean.get(i).getName());
                        address.setTelePhone(addressListBean.get(i).getTel());
                        address.setRegoin(addressListBean.get(i).getRegoin());
                        address.setAddress(addressListBean.get(i).getAddr());
                        address.setIsHideDetial(true);
                        address.setUserId(String.valueOf(addressListBean.get(i).getUserId()));
                        address.setIsDefault(String.valueOf(addressListBean.get(i).getUsed()));
                        getAddressDao().save(address);
                    }
                }
            }).start();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
