package com.yunma.jhuo.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yunma.R;
import com.yunma.dao.AppInfo;
import com.yunma.dao.BrowsingHistory;
import com.yunma.dao.GreenDaoManager;
import com.yunma.greendao.AppInfoDao;
import com.yunma.greendao.BrowsingHistoryDao;
import com.yunma.jhuo.fragment.BasketFragment;
import com.yunma.jhuo.fragment.GoodsClassifiedFragment;
import com.yunma.jhuo.fragment.HomeFragment;
import com.yunma.jhuo.fragment.MineFragment;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yunma.dao.GreenDaoUtils.getBrowsingHistoryDao;
import static com.yunma.dao.GreenDaoUtils.getContactsDao;

public class MainActivity extends MyCompatActivity /*implements
        BaiduLocationListener.LocationCallBack,RadarSearchListener*/  {
    @BindView(R.id.layNavigationBar) LinearLayout layNavigationBar;
    @BindView(R.id.content) FrameLayout content;
    @BindView(R.id.imgHome) ImageView imgHome;
    @BindView(R.id.tvHome) TextView tvHome;
    @BindView(R.id.layoutHome) RelativeLayout layoutHome;
    @BindView(R.id.imgStorage) ImageView imgStorage;
    @BindView(R.id.tvStorage) TextView tvStorage;
    @BindView(R.id.layoutStorage) RelativeLayout layoutStorage;
    @BindView(R.id.imgBasket) ImageView imgBasket;
    @BindView(R.id.tvBasket) TextView tvBasket;
    @BindView(R.id.layoutBasket) RelativeLayout layoutBasket;
    @BindView(R.id.imgMine) ImageView imgMine;
    @BindView(R.id.tvMine) TextView tvMine;
    @BindView(R.id.layoutMine) RelativeLayout layoutMine;
    @BindView(R.id.mLyout) RelativeLayout mLyout;
    //private HomepageFragment homePageFragment = null;
    private HomeFragment homeFragment = null;
    private BasketFragment basketFragment = null;
    private MineFragment mineFragment = null;
    private GoodsClassifiedFragment gcFragmnet;
    private FragmentManager fragmentManager = null;
    private int currentPosition;
    public static MainActivity mainActivity;
    private static int[] colorId = new int[]{R.color.b2,R.color.b3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBar();
        initBrowserhistory();
    }

    private void initBar() {
        mainActivity = MainActivity.this;
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) mLyout.getLayoutParams();
        fl.setMargins(0, SPUtils.getStatusHeight(this), 0,0);
        mLyout.setLayoutParams(fl);
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    private void setTabSelection(int type) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (type) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                currentPosition = 0;
                tvHome.setTextColor(ContextCompat.getColor(this,colorId[1]));
                imgHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_home_s));
                break;
            case 1:
                if (gcFragmnet == null) {
                    gcFragmnet = new GoodsClassifiedFragment();
                    transaction.add(R.id.content, gcFragmnet);
                } else {
                    transaction.show(gcFragmnet);
                }
                currentPosition = 1;
                tvStorage.setTextColor(ContextCompat.getColor(this,colorId[1]));
                imgStorage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_entrepot_s));
                break;
            case 2:
                if (basketFragment == null) {
                    basketFragment = new BasketFragment();
                    transaction.add(R.id.content, basketFragment);
                } else {
                    transaction.show(basketFragment);
                }
                currentPosition = 2;
                tvBasket.setTextColor(ContextCompat.getColor(this,colorId[1]));
                imgBasket.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_shopping_s));
                break;
            case 3:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.content, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                currentPosition = 3;
                tvMine.setTextColor(ContextCompat.getColor(this,colorId[1]));
                imgMine.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_me_s));
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (gcFragmnet != null) {
            transaction.hide(gcFragmnet);
        }
        if (basketFragment != null) {
            transaction.hide(basketFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    private void clearSelection() {
        tvHome.setTextColor(ContextCompat.getColor(this,colorId[0]));
        tvStorage.setTextColor(ContextCompat.getColor(this,colorId[0]));
        tvBasket.setTextColor(ContextCompat.getColor(this,colorId[0]));
        tvMine.setTextColor(ContextCompat.getColor(this,colorId[0]));
        imgHome.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_home));
        imgStorage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_entrepot));
        imgBasket.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_shopping));
        imgMine.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tab_me));
    }

    @OnClick({R.id.layoutHome, R.id.layoutStorage, R.id.layoutBasket, R.id.layoutMine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutHome:
                if(currentPosition!=0){
                    setTabSelection(0);
                }
                break;
            case R.id.layoutStorage:
                if(currentPosition!=1){
                    setTabSelection(1);
                }
                break;
            case R.id.layoutBasket:
                if(currentPosition!=2){
                    setTabSelection(2);
                }
                break;
            case R.id.layoutMine:
                if(currentPosition!=3){
                    setTabSelection(3);
                }
                break;
        }
    }

    private void initBrowserhistory() {
        final String tel = SPUtils.getPhoneNumber(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<BrowsingHistory> nQuery = getBrowsingHistoryDao().queryBuilder()
                        .where(BrowsingHistoryDao.Properties.Tel.isNull())
                        .build()
                        .list();
                for (int i = 0; i < nQuery.size(); i++) {
                    nQuery.get(i).setTel(tel);
                }
                saveLists(nQuery);
            }
        }).start();
    }

    public void saveLists(final List<BrowsingHistory> list){
        if(list == null || list.isEmpty()){
            return;
        }
        getBrowsingHistoryDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    BrowsingHistory user = list.get(i);
                    getBrowsingHistoryDao().save(user);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new MaterialDialog.Builder(this)
                    .title("J货")
                    .icon(ContextCompat.getDrawable(this,R.mipmap.jhuo_logo))
                    .content("确定退出？")
                    .positiveText("退出")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                                            @NonNull DialogAction which) {
                            GlideUtils.glidClearDisk(MainActivity.this);
                            dialog.dismiss();
                            getContactsDao().deleteAll();
                            List<AppInfo> loginInfo = getAppInfoDao().loadAll();
                            if (loginInfo.size() != 0) {
                                loginInfo.get(0).setIsLogin(0);
                            }
                            MainActivity.this.finish();
                            AppManager.getAppManager().AppExit();

                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                                            @NonNull DialogAction which) {
                        }
                    })
                    .show();
        }
        return false;
    }

    private AppInfoDao getAppInfoDao() {
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.json("执行onDestroy");
    }

}
