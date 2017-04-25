package com.yunma.jhuo.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.SystemNoticeAdapter;
import com.yunma.bean.NoticeBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetNoticeInterFace;
import com.yunma.jhuo.p.NoticePre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

public class SystemNotice extends MyCompatActivity implements GetNoticeInterFace.NoticeView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvSystemNotice) ListView lvSystemNotice;
    private SystemNoticeAdapter mAdapter;
    private NoticePre noticePre = null;
    private List<NoticeBean.SuccessBean.ListBean> listBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notice);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        setDatas();
    }

    @OnClick(R.id.layoutBack)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }

    private void setDatas() {
        noticePre.getNotices(getApplicationContext());
    }

    private void initStatusBarAndNavigationBar() {
        noticePre = new NoticePre(this);
        int statusHeight = ScreenUtils.getStatusHeight(SystemNotice.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        lvSystemNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SystemNotice.this, SystemNoticeDatial.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("notice",listBean.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showNoticeInfo(NoticeBean noticeBean, String msg) {
        if(noticeBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            if(noticeBean.getSuccess().getList().size()!=0){
                listBean = noticeBean.getSuccess().getList();
                mAdapter = new SystemNoticeAdapter(this,noticeBean.getSuccess().getList());
                lvSystemNotice.setAdapter(mAdapter);
            }else{
                ToastUtils.showError(getApplicationContext(),"暂无公告");
            }

        }
    }
}
