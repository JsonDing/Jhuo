package com.yunma.jhuo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.adapter.SelectVolumeAdapter;
import com.yunma.bean.VolumeResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.VolumeListInterface;
import com.yunma.jhuo.p.VolumeListPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


public class SelectVolumeActivity extends MyCompatActivity implements VolumeListInterface.VolumeListView, SelectVolumeAdapter.OnItemClick {
    private static final int VOLIME_RESULT = 3;
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.tvTittle)
    TextView tvTittle;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private Context context;
    private SelectVolumeAdapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_volume);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initBar();
        getVolume();
    }

    private void getVolume() {
        VolumeListPre volumeListPre = new VolumeListPre(this);
        volumeListPre.getUnuseVolumeList(context);
    }

    private void initBar() {
        context = this;
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(context),0,0);
        tvTittle.setText("优惠卷");
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new SelectVolumeAdapter(context, SelectVolumeActivity.this);
        recyclerView.setAdapter(mAdapter);
    }




    @OnClick(R.id.layoutBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void showUnuseVolumeList(VolumeResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(context,msg);
        }else{
            if(resultBean.getSuccess().size()==0){

            }else{
                mAdapter.setUnUseListBean(resultBean.getSuccess());
            }
        }
    }

    @Override
    public void showTimeOutVolumeList(VolumeResultBean resultBean, String msg) {
        // TODO: 2017-04-21   Unneed to do anything
    }

    @Override
    public void onItemClick(String money, String name) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("money",money);
        bundle.putString("name",name);
        intent.putExtras(bundle);
        SelectVolumeActivity.this.setResult(VOLIME_RESULT, intent);
        AppManager.getAppManager().finishActivity(SelectVolumeActivity.this);
    }
}
