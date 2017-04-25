package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.GoodsReturnDetialAdapter;
import com.yunma.bean.ServiceResultBean.SuccessBean.ListBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GoodsRefundInterface.DelRefundView;
import com.yunma.jhuo.p.RefundPre;
import com.yunma.utils.*;
import com.yunma.widget.NestListView;

import butterknife.*;

public class GoodsReturnDetial extends MyCompatActivity implements DelRefundView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvOrderId) TextView tvOrderId;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.nestlistview) NestListView nestlistview;
    @BindView(R.id.btnCanceRefund) Button btnCanceRefund;
    private Context mContext;
    private ListBean listBean = null;
    private GoodsReturnDetialAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_reback_detial);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();

    }

    private void getDatas() {
        listBean = (ListBean) getIntent().getSerializableExtra("goodsDetial");
        assert listBean != null;
        tvOrderId.setText(listBean.getOid());
        tvTime.setText(DateTimeUtils.getTime(listBean.getOrder().getDate(),DateTimeUtils.DATE_FORMAT_DATE));
        if(mAdapter==null){
            mAdapter = new GoodsReturnDetialAdapter(GoodsReturnDetial.this,listBean.getServiceDetails(),listBean.getDate());
            nestlistview.setAdapter(mAdapter);
        }
        listBean.getServiceDetails();
    }


    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(GoodsReturnDetial.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.btnCanceRefund})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnCanceRefund:
                RefundPre refundPre = new RefundPre(GoodsReturnDetial.this);
                refundPre.delRefund(mContext,listBean.getId());
                break;
        }
    }

    @Override
    public void showDelInfo(SuccessResultBean resultBean, String msg) {
        if (resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
        }
    }
}
