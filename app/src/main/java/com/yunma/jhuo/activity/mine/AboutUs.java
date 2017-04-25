package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yunma.R;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import butterknife.*;


public class AboutUs extends MyCompatActivity {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {
    }

    private void setDatas() {
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(AboutUs.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick(R.id.layoutBack)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }
}
