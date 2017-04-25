package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.SuccessResultBean;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.ModifypassWdInterface;
import com.yunma.jhuo.p.ModifyPassWdPre;
import com.yunma.utils.*;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.*;

public class AccountManage extends MyCompatActivity implements ModifypassWdInterface.ModifypassWdView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.etOldPasswd) EditText etOldPasswd;
    @BindView(R.id.etNewPassWd) EditText etNewPassWd;
    @BindView(R.id.btnConfirmEdit) Button btnConfirmEdit;
    @BindView(R.id.layout) View layout;
    private Context mContext;
    private ModifyPassWdPre modifyPassWdPre;
    private boolean isCanTouch = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        modifyPassWdPre = new ModifyPassWdPre(this);
        int statusHeight = ScreenUtils.getStatusHeight(AccountManage.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
     /*   int navigationBarHeight = ScreenUtils.getNavigationBarHeight(AccountManage.this);
        //取控件textView当前的布局参数
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        linearParams.height = navigationBarHeight;// 控件的高强制设成
        linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件*/
    }

    @OnClick({R.id.layoutBack, R.id.btnConfirmEdit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnConfirmEdit:
                if(isCanTouch){
                    isCanTouch = false;
                    modifyPassWdPre.modifyPassWd();
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public String getOriginalPasswd() {
        return etOldPasswd.getText().toString().trim();
    }

    @Override
    public String getNewPassWd() {
        return etNewPassWd.getText().toString().trim();
    }

    @Override
    public void showModifyInfos(SuccessResultBean successResultBean, String msg) {
        if(successResultBean==null){
            isCanTouch = true;
            ToastUtils.showError(mContext,msg);
        }else{
            SPUtils.setPassWd(mContext,etNewPassWd.getText().toString().trim());
            Query<UserInfos> nQuery = getUserDao().queryBuilder()
                    .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                    .build();
            List<UserInfos> users = nQuery.list();
            LogUtils.log("users.size() -------->  " + " " + users.size());
            UserInfos userInfos =  new UserInfos(users.get(0).getId(),users.get(0).getUserId(),
                    users.get(0).getPhoneNumber(), etNewPassWd.getText().toString().trim(),
                    users.get(0).getImgsPhotos(), users.get(0).getIsAutoLogin(),
                    users.get(0).getNickName(),users.get(0).getGender(),
                    users.get(0).getRealName());
            getUserDao().update(userInfos);
            ToastUtils.showSuccess(mContext,msg);
            etNewPassWd.setText("");
            etOldPasswd.setText("");
            isCanTouch = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppManager.getAppManager().finishActivity(AccountManage.this);
                }
            },2000);
        }
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }
}
