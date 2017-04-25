package com.yunma.jhuo.general;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.BannerResultBean;
import com.yunma.bean.LoginSuccessResultBean;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.dao.AppInfo;
import com.yunma.dao.GreenDaoManager;
import com.yunma.dao.UserInfos;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.UserInfosDao;
import com.yunma.jhuo.m.LoginInterface;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.BannerPre;
import com.yunma.jhuo.p.LoginPre;
import com.yunma.utils.*;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashPageActivity extends MyCompatActivity implements
        LoginInterface.LoginView, SelfGoodsInterFace.GetBannerView {

    @BindView(R.id.tvSkipOut)
    TextView tvSkipOut;
    @BindView(R.id.layoutSkip)
    LinearLayout layoutSkip;
    @BindView(R.id.tvSpWord)
    TextView tvSpWord;
    private Context mContext = null;
    private String phoneNumber = null;
    private String passWd = null;
    private TimeUtils timeUtils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_page);
        ButterKnife.bind(this);
        mContext = SplashPageActivity.this;
        AppManager.getAppManager().addActivity(this);
        int statusHeight = ScreenUtils.getStatusHeight(this);
        SPUtils.setStatusHeight(this,statusHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        BannerPre bannerPre = new BannerPre(SplashPageActivity.this);
        bannerPre.getNewsBanner(SplashPageActivity.this);

    }

    private void getIsFirstOpen() {
        List<AppInfo> info = getAppDao().loadAll();
        layoutSkip.setVisibility(View.VISIBLE);
        timeUtils = new TimeUtils(mContext, tvSkipOut, "0s", 5, -1);
        timeUtils.RunTimer();
        if (info.size() == 0) {
            AppInfo infos = new AppInfo(null, 1, 0, null, 0);
            getAppDao().save(infos);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashPageActivity.this, MyViewPage.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(SplashPageActivity.this);
                }
            }, 4000);
        } else {
            AppInfo infos = new AppInfo(info.get(0).getId(), 1, 0, null, info.get(0).getIsFirstSetting());
            getAppDao().save(infos);
            getIsAutoLogin();
        }
    }

    private void getIsAutoLogin() {
        Query<UserInfos> nQuery = getUserDao().queryBuilder()
                .where(UserInfosDao.Properties.PhoneNumber.eq(SPUtils.getPhoneNumber(mContext)))
                .build();
        layoutSkip.setVisibility(View.VISIBLE);
        timeUtils = new TimeUtils(mContext, tvSkipOut, "0s", 5, -1);
        timeUtils.RunTimer();
        LoginPre mPresenter = new LoginPre(SplashPageActivity.this);
        if (nQuery.list().size() != 0) {
            UserInfos users = nQuery.list().get(0);
            if (users.getIsAutoLogin()) {
                phoneNumber = users.getPhoneNumber();
                passWd = users.getPassWd();//此处passwd已MD5加密处理
                mPresenter.login(mContext, phoneNumber, passWd);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashPageActivity.this, MainActivity.class);
                        startActivity(intent);
                        AppManager.getAppManager().finishActivity(SplashPageActivity.this);
                    }
                }, 4000);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(SplashPageActivity.this);
                }
            }, 4000);
        }
    }

    private AppInfoDao getAppDao() {
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    private UserInfosDao getUserDao() {
        return GreenDaoManager.getInstance().getSession().getUserInfosDao();
    }

    @Override
    public void showLoginInfos(LoginSuccessResultBean resultBean, String msg) {
        layoutSkip.setVisibility(View.VISIBLE);
        timeUtils = new TimeUtils(mContext, tvSkipOut, "0s", 5, -1);
        timeUtils.RunTimer();
        if (resultBean == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(SplashPageActivity.this);
                }
            }, 4000);
        } else {
            List<AppInfo> appInfos = getAppDao().loadAll();
            AppInfo infos = new AppInfo(appInfos.get(0).getId(), 1, 1, resultBean.getSuccess().getToken(),
                    appInfos.get(0).getIsFirstSetting());
            getAppDao().save(infos);
            SPUtils.setToken(mContext, resultBean.getSuccess().getToken());
            SPUtils.setPhoneNumber(mContext, phoneNumber);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashPageActivity.this, MainActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(SplashPageActivity.this);
                }
            }, 4000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
        if (passWd != null) {
            passWd = null;
        }
        if (phoneNumber != null) {
            phoneNumber = null;
        }
        GlideUtils.glidClearMemory(SplashPageActivity.this);
    }

    @OnClick(R.id.layoutSkip)
    public void onViewClicked() {
        if (timeUtils != null) {
            timeUtils.destroyHandler();
        }
        Intent intent = new Intent(SplashPageActivity.this, MainActivity.class);
        startActivity(intent);
        AppManager.getAppManager().finishActivity(SplashPageActivity.this);
    }

    @Override
    public void showNewsBanner(BannerResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        } else {
            for (int i = 0; i < resultBean.getSuccess().size(); i++) {
                if (resultBean.getSuccess().get(i).getType().equals("spword")) {
                    tvSpWord.setText(resultBean.getSuccess()
                            .get(i).getValue());
                }
            }
            getIsFirstOpen();
        }
    }
}
