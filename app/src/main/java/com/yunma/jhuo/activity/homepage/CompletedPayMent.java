package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.jhuo.activity.LookOrderDetial;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.adapter.CompletedPayMentAdapter;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetGoodsInterface;
import com.yunma.jhuo.p.GetGoodsHotPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompletedPayMent extends MyCompatActivity implements
         GetGoodsInterface.GetGoodsHotView {

    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvPayway) TextView tvPayway;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.btnOrderDetial) Button btnOrderDetial;
    @BindView(R.id.btnBackHome) Button btnBackHome;
    @BindView(R.id.gvIntro) GridView gvIntro;
    @BindView(R.id.activity_completed_pay_ment)
    FrameLayout activityCompletedPayMent;
    private String orderId;
    private CompletedPayMentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_pay_ment);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBar();
        getDatas();
    }

    private void getDatas() {
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        tvPayway.setText(bundle.getString("payWay"));
        tvTotalPrice.setText(bundle.getString("totalPrice"));
        GetGoodsHotPre hotPre = new GetGoodsHotPre(this);
        hotPre.getHotGoods("16",1);
    }

    private void initStatusBar() {
        int statusHeight = ScreenUtils.getStatusHeight(CompletedPayMent.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams(); //取控件textView当前的布局参数
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(this, 44), 0,0);
        scroll.setLayoutParams(fl); //使设置好的布局参数应用到控件
    }


    @OnClick({R.id.layoutBack, R.id.btnOrderDetial, R.id.btnBackHome})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnOrderDetial:
                intent = new Intent(CompletedPayMent.this,LookOrderDetial.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnBackHome:
                intent = new Intent(CompletedPayMent.this, MainActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showHotGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else {
            if(resultBean.getSuccess().getList()!=null&&
                    resultBean.getSuccess().getList().size()!=0){
                List<GoodsInfoResultBean.SuccessBean.ListBean> listBean = resultBean.getSuccess().getList();
                if(mAdapter==null){
                    mAdapter = new CompletedPayMentAdapter(this,listBean);
                    gvIntro.setAdapter(mAdapter);
                    scroll.smoothScrollTo(0,0);
                }else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
