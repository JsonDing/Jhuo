package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.VolumeListAdapter;
import com.yunma.bean.VolumeResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.VolumeListInterface;
import com.yunma.jhuo.p.VolumeListPre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class VolumeListActivity extends MyCompatActivity implements
        VolumeListAdapter.OnItemClick, VolumeListInterface.VolumeListView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutOperation) LinearLayout layoutOperation;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layoutTab) LinearLayout layoutTab;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.rbButton) CheckBox ckButton;
    @BindView(R.id.btnCancle) Button btnCancle;
    @BindView(R.id.btnDetele) Button btnDetele;
    @BindView(R.id.volumeUnuse) RelativeLayout volumeUnuse;
    @BindView(R.id.volumeTimeOut) RelativeLayout volumeTimeOut;
    @BindView(R.id.layoutBottom) RelativeLayout layoutBottom;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvOperation) TextView tvOperation;
    @BindView(R.id.tvUnuse) TextView tvUnuse;
    @BindView(R.id.viewUnuse) View viewUnuse;
    @BindView(R.id.tvTimeOut) TextView tvTimeOut;
    @BindView(R.id.viewTimeOut) View viewTimeOut;
    private Context mContext;
    private int refrishType = 1;
    private VolumeListPre volumeListPre = null;
    private VolumeListAdapter mAdapter = null;
    private List<VolumeResultBean.SuccessBean> unUseListBean = null;
    private List<VolumeResultBean.SuccessBean> timeOutListBean = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_list);
        ButterKnife.bind(this);
        initBar();
        initSwipeRefreshLayout();
        getVolumeList();
    }

    private void initBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(VolumeListActivity.this);
        layouStatusBar.setPadding(0,statusHeight,0,0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInLeftAnimator());
        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.getItemAnimator().setMoveDuration(500);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new VolumeListAdapter(mContext, VolumeListActivity.this);
        recyclerView.setAdapter(mAdapter);
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final Animation operatingAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.head);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        final View view = inflater.inflate(R.layout.refresh_head_view, null);
        final View view1 = inflater.inflate(R.layout.refresh_view, null);
        final ImageView imgSun = (ImageView) view.findViewById(R.id.imgSun);
        final TextView textView2 = (TextView) view1.findViewById(R.id.title);
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(true);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                if(refrishType == 1){
                    volumeListPre.getUnuseVolumeList(mContext);
                }else{
                    volumeListPre.getTimeOutVolumeList(mContext);
                }
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                        imgSun.clearAnimation();
                    }
                }, 1300);
            }

            @Override
            public void onLoading() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishLoadmore();
                    }
                }, 1500);
            }

            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        imgSun.startAnimation(operatingAnim);
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        textView2.setText("正在加载...");
                        JumpingBeans.with(textView2)
                                .appendJumpingDots()
                                .build();
                        break;
                }
            }
        });
    }

    private void getVolumeList() {
        volumeListPre = new VolumeListPre(this);
        volumeListPre.getUnuseVolumeList(mContext);
        volumeListPre.getTimeOutVolumeList(mContext);
    }

    @OnClick({R.id.layoutBack, R.id.layoutOperation, R.id.rbButton,
            R.id.btnCancle, R.id.btnDetele, R.id.volumeUnuse, R.id.volumeTimeOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutOperation:
                if ("操作".equals(tvOperation.getText().toString().trim())) {
                    tvOperation.setText("删除");
                    layoutBottom.setVisibility(View.VISIBLE);
                } else {
                    layoutBottom.setVisibility(View.GONE);
                    tvOperation.setText("操作");
                }
                break;
            case R.id.rbButton:

                break;
            case R.id.btnCancle:

                break;
            case R.id.btnDetele:

                break;
            case R.id.volumeUnuse:
                changeShow(1);
                break;
            case R.id.volumeTimeOut:
                changeShow(2);
                break;
        }
    }

    private void changeShow(int type) {
        switch (type){
            case 1:
                refrishType = 1;
                viewUnuse.setVisibility(View.VISIBLE);
                tvUnuse.setTextColor(Color.parseColor("#f4bd39"));
                viewTimeOut.setVisibility(View.INVISIBLE);
                tvTimeOut.setTextColor(Color.parseColor("#323232"));
                if(unUseListBean!=null){
                    mAdapter.setUnUseListBean(unUseListBean);
                }
                break;
            case 2:
                refrishType = 2;
                viewUnuse.setVisibility(View.INVISIBLE);
                tvUnuse.setTextColor(Color.parseColor("#323232"));
                viewTimeOut.setVisibility(View.VISIBLE);
                tvTimeOut.setTextColor(Color.parseColor("#f4bd39"));
                if(timeOutListBean!=null){
                    mAdapter.setUnUseListBean(timeOutListBean);
                }
                break;
        }
    }

    @Override
    public void onItemClick() {

    }

    @Override
    public void showVolumeIntro() {
        int dialogWith = ScreenUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 48);
        new MDDialog.Builder(this)
                .setContentView(R.layout.volume_info)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(this, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    @Override
    public void showUnuseVolumeList(VolumeResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            if(resultBean.getSuccess().size()==0){

            }else{
                unUseListBean = resultBean.getSuccess();
                tvUnuse.setText("未使用(" + unUseListBean.size() + ")");
                if(refrishType==1){
                    swipeRefreshLayout.finishRefresh();
                    mAdapter.setUnUseListBean(unUseListBean);
                }

            }
        }
    }

    @Override
    public void showTimeOutVolumeList(VolumeResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            if(resultBean.getSuccess().size()==0){

            }else{
                timeOutListBean= resultBean.getSuccess();
                tvTimeOut.setText("已过期(" + timeOutListBean.size() + ")");
                if(refrishType==2){
                    swipeRefreshLayout.finishRefresh();
                    mAdapter.setUnUseListBean(timeOutListBean);
                }
            }
        }
    }
}
