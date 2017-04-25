package com.yunma.jhuo.activity.mine;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.fragment.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import butterknife.*;

public class ReturnGoodsManage extends MyCompatActivity {
    public static ReturnGoodsManage returnGoodsContext;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvNotHandle) TextView tvNotHandle;
    @BindView(R.id.notHandleLine) View notHandleLine;
    @BindView(R.id.layoutNotHandle) RelativeLayout layoutNotHandle;
    @BindView(R.id.tvHandling) TextView tvHandling;
    @BindView(R.id.handlingLine) View handlingLine;
    @BindView(R.id.layoutHandling) RelativeLayout layoutHandling;
    @BindView(R.id.tvComplete) TextView tvComplete;
    @BindView(R.id.completeLine) View completeLine;
    @BindView(R.id.layoutComplete) RelativeLayout layoutComplete;
    @BindView(R.id.tvRejust) TextView tvRejust;
    @BindView(R.id.rejustLine) View rejustLine;
    @BindView(R.id.layoutRejust) RelativeLayout layoutRejust;
    @BindView(R.id.content) FrameLayout content;
    private Context mContext;
    private ServiceNotHandle serviceNotHandle = null;
    private ServiceHandling serviceHandling = null;
    private ServiceComplete serviceComplete = null;
    private ServiceRejust serviceRejust = null;
    private FragmentManager fragmentManager = null;
    private int currentPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reback_goods);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        fragmentManager = getFragmentManager();
        setTabSelection(currentPos);
    }


    private void initStatusBarAndNavigationBar() {
        mContext = this;
        returnGoodsContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ReturnGoodsManage.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack,R.id.layoutNotHandle, R.id.layoutHandling, R.id.layoutComplete,
            R.id.layoutRejust})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNotHandle:
                if(currentPos!=0){
                    setTabSelection(0);
                    currentPos = 0;
                }
                break;
            case R.id.layoutHandling:
                if(currentPos!=1){
                    setTabSelection(1);
                    currentPos = 1;
                }
                break;
            case R.id.layoutComplete:
                if(currentPos!=2){
                    setTabSelection(2);
                    currentPos = 2;
                }
                break;
            case R.id.layoutRejust:
                if(currentPos!=3){
                    setTabSelection(3);
                    currentPos = 3;
                }
                break;
        }
    }

    private void setTabSelection(int position) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (position) {
            case 0:
                if (serviceNotHandle == null) {
                    serviceNotHandle = new ServiceNotHandle();
                    transaction.add(R.id.content, serviceNotHandle);
                } else {
                    transaction.show(serviceNotHandle);
                }
                tvNotHandle.setTextColor(getResources().getColor(R.color.color_b3));
                notHandleLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (serviceHandling == null) {
                    serviceHandling = new ServiceHandling();
                    transaction.add(R.id.content, serviceHandling);
                } else {
                    transaction.show(serviceHandling);
                }
                tvHandling.setTextColor(getResources().getColor(R.color.color_b3));
                handlingLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (serviceComplete == null) {
                    serviceComplete = new ServiceComplete();
                    transaction.add(R.id.content, serviceComplete);
                } else {
                    transaction.show(serviceComplete);
                }
                tvComplete.setTextColor(getResources().getColor(R.color.color_b3));
                completeLine.setVisibility(View.VISIBLE);
                break;
            case 3:
                if (serviceRejust == null) {
                    serviceRejust = new ServiceRejust();
                    transaction.add(R.id.content, serviceRejust);
                } else {
                    transaction.show(serviceRejust);
                }
                tvRejust.setTextColor(getResources().getColor(R.color.color_b3));
                rejustLine.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (serviceNotHandle != null) {
            transaction.hide(serviceNotHandle);
        }
        if (serviceHandling != null) {
            transaction.hide(serviceHandling);
        }
        if (serviceComplete != null) {
            transaction.hide(serviceComplete);
        }
        if (serviceRejust != null) {
            transaction.hide(serviceRejust);
        }
    }

    private void clearSelection() {
        tvNotHandle.setTextColor(getResources().getColor(R.color.color_b1));
        tvHandling.setTextColor(getResources().getColor(R.color.color_b1));
        tvComplete.setTextColor(getResources().getColor(R.color.color_b1));
        tvRejust.setTextColor(getResources().getColor(R.color.color_b1));
        notHandleLine.setVisibility(View.INVISIBLE);
        handlingLine.setVisibility(View.INVISIBLE);
        completeLine.setVisibility(View.INVISIBLE);
        rejustLine.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceComplete = null;
        serviceRejust = null;
        serviceHandling= null;
        serviceNotHandle = null;
        fragmentManager = null;
    }
}
