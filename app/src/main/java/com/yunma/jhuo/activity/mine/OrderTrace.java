package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.ExpressTraceRecordAdapter;
import com.yunma.bean.ExpressTracesBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.ExpressTracesInterface;
import com.yunma.jhuo.p.ExpressTracesPre;
import com.yunma.utils.*;

import java.util.Collections;
import java.util.List;

import butterknife.*;

public class OrderTrace extends MyCompatActivity implements ExpressTracesInterface.ExpressTracesView {
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvExpressId)
    TextView tvExpressId;
    @BindView(R.id.tvExpressName)
    TextView tvExpressName;
    @BindView(R.id.lvExpressRecord)
    ListView lvExpressRecord;
    @BindView(R.id.layout)
    View layout;
    private Context mContext;
    private ExpressTraceRecordAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_trace);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
    }

    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        String code = bundle.getString("code");
        String orderId = bundle.getString("orderId");
        String name = bundle.getString("name");
        String number = bundle.getString("number");
        if(code!=null&&number!=null&&name!=null){
            tvOrderId.setText("订单编号：" + orderId);
            tvExpressId.setText("物流单号：" + number);
            tvExpressName.setText("承运来源：" + name);
        }
        ExpressTracesPre expressTracesPre = new ExpressTracesPre(this);
        expressTracesPre.getExpressTraces(mContext,code,number);
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(OrderTrace.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick(R.id.layoutBack)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void toShowExpressTraces(ExpressTracesBean expressTracesBean, String msg) {
        if(expressTracesBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            if(EmptyUtil.isNotEmpty(expressTracesBean.getSuccess().getTraces())){
                List<ExpressTracesBean.SuccessBean.TracesBean> listBean = expressTracesBean.getSuccess().getTraces();
                Collections.reverse(listBean);
                if(mAdapter==null){
                    mAdapter = new ExpressTraceRecordAdapter(this,listBean,expressTracesBean.getSuccess());
                    lvExpressRecord.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}

