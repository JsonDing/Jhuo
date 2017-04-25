package com.yunma.jhuo.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.AdInfoResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;
import com.yunma.widget.URLImageParser;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.*;

public class AdInfosDetialsActivity extends MyCompatActivity {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutDelete) LinearLayout layoutDelete;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvNoticeTittle) TextView tvNoticeTittle;
    @BindView(R.id.tvNoticeContent) TextView tvNoticeContent;
    @BindView(R.id.tvPublisher) TextView tvPublisher;
    @BindView(R.id.tvPublishTime) TextView tvPublishTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_infos_detials);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getDatas();
    }

    private void initStatusBar() {
        int statusHeight = ScreenUtils.getStatusHeight(AdInfosDetialsActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    private void getDatas() {
        AdInfoResultBean.SuccessBean adInfo =
                (AdInfoResultBean.SuccessBean) getIntent().getSerializableExtra("adInfos");
        tvNoticeContent.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        tvNoticeContent.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        tvNoticeTittle.setText(Html.fromHtml(adInfo.getTitle()));
        tvNoticeContent.setText(
                Html.fromHtml(adInfo.getContent(),new URLImageParser(tvNoticeContent),null));
        tvPublisher.setText(adInfo.getAuthor());
        tvPublishTime.setText(DateTimeUtils.getTime(adInfo.getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
    }

    @OnClick({R.id.layoutBack, R.id.layoutDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutDelete:

                break;
        }
    }
}
