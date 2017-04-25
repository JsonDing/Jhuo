package com.yunma.jhuo.activity.mine;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.NoticeBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;
import com.yunma.widget.URLImageParser;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.*;

public class SystemNoticeDatial extends MyCompatActivity {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutDelete) LinearLayout layoutDelete;
    @BindView(R.id.tvNoticeTittle) TextView tvNoticeTittle;
    @BindView(R.id.tvNoticeContent) TextView tvNoticeContent;
    @BindView(R.id.tvPublisher) TextView tvPublisher;
    @BindView(R.id.tvPublishTime) TextView tvPublishTime;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    private NoticeBean.SuccessBean.ListBean listBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notice_datial);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getDatas();
    }

    private void getDatas() {
        listBean = (NoticeBean.SuccessBean.ListBean) getIntent().getSerializableExtra("notice");
        tvNoticeContent.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        tvNoticeContent.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        tvNoticeTittle.setText(Html.fromHtml(listBean.getTitle()));
        tvNoticeContent.setText(
                Html.fromHtml(listBean.getContent(),new URLImageParser(tvNoticeContent),null));
        tvPublisher.setText(listBean.getAuthor());
        tvPublishTime.setText(DateTimeUtils.getTime(listBean.getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
    }

    private void initStatusBar() {
        int statusHeight = ScreenUtils.getStatusHeight(SystemNoticeDatial.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutDelete:

                break;
        }
    }

}
