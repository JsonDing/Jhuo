package com.yunma.publish;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.LocalImagePathBean;
import com.yunma.bean.PathBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.DownLoaderInterface;
import com.yunma.jhuo.p.DownLoaderPre;
import com.yunma.utils.*;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.*;

/**
 * Created by Json on 2017/3/10.
 */

public class PicViewerActivity extends MyCompatActivity implements DownLoaderInterface.DownLoadView {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layoutDown)
    RelativeLayout layoutDown;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    private ViewPager viewPager;
    private TextView tv_indicator;
    private List<PathBean> urlList;
    public LocalImagePathBean localImagePathBean;
    private int currPosition;
    private PictureSlidePagerAdapter mAdapter;
    private DownLoaderPre downLoaderPre = null;
    private ProgressDialog progressDialog;
    private static final String DOWN_LOAD_PATH =
            Environment.getExternalStorageDirectory().getPath().toString() + "/YunMa/Jhuo/download/" ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_viewer);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        getIntentDatas();
    }

    private void getIntentDatas() {
        Bundle bundle = this.getIntent().getExtras();
        localImagePathBean = (LocalImagePathBean) bundle.getSerializable("path");
        downLoaderPre = new DownLoaderPre(this);
        if (localImagePathBean != null)
            urlList = localImagePathBean.getPathBeen();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);
        mAdapter = new PictureSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(bundle.getInt("pos"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setCurrPosition(position);
                tv_indicator.setText(String.valueOf(position + 1) + "/" + urlList.size());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBar() {
        int statusHeight = ScreenUtils.getStatusHeight(PicViewerActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutDown})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutDown:
                downLoaderPre.startDown(this,urlList.get(getCurrPosition()).getImgsPath()
                        ,DOWN_LOAD_PATH + DateTimeUtils.getCurrentTimeInString(
                                new SimpleDateFormat("yyyy_MM_dd_HH_mm_SSS")) + ".png");
                break;
        }
    }

    @Override
    public void showDownLoadInfo(String msg) {
        ToastUtils.showSuccess(this,msg);

    }


    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position));//
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
    }

    public int getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(int currPosition) {
        this.currPosition = currPosition;
    }
}
