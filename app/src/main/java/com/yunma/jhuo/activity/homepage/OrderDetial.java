package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.mine.*;
import com.yunma.adapter.OrderDetialAdapter;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.CancleOrderInterface;
import com.yunma.jhuo.p.CancleOrderPre;
import com.yunma.utils.*;
import com.yunma.widget.MyListView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.*;
import cn.carbs.android.library.MDDialog;


public class OrderDetial extends MyCompatActivity implements CancleOrderInterface.CancleOrderView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.tvOrderId) TextView tvOrderId;
    @BindView(R.id.tvOrderHour) TextView tvOrderHour;
    @BindView(R.id.tvExpressStatus) TextView tvExpressStatus;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvReceiver) TextView tvReceiver;
    @BindView(R.id.tvPhone) TextView tvPhone;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.lvOrder) MyListView lvOrder;
    @BindView(R.id.tvOrderTime) TextView tvOrderTime;
    @BindView(R.id.tvPayway) TextView tvPayway;
    @BindView(R.id.tvOrderPrice) TextView tvOrderPrice;
    @BindView(R.id.tvShouldPay) TextView tvShouldPay;
    @BindView(R.id.tvOrderExpress) TextView tvOrderExpress;
    @BindView(R.id.tvRealPay) TextView tvRealPay;
    @BindView(R.id.btnApplyHelp) Button btnApplyHelp;
    @BindView(R.id.btnOrderTrance) Button btnOrderTrance;
    @BindView(R.id.btnCance) Button btnCance;
    @BindView(R.id.btnPay) Button btnPay;
    @BindView(R.id.btnNotSend) Button btnNotSend;
    @BindView(R.id.layoutTrace) RelativeLayout layoutTrace;
    private OrderUnPayResultBean.SuccessBean.ListBean listBean = null;
    private OrderDetialAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detial);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {
        listBean = (OrderUnPayResultBean.SuccessBean.ListBean) getIntent()
                .getSerializableExtra("orderDetial");
        assert listBean != null;
        if(listBean.getPay() == 0){
            btnCance.setVisibility(View.VISIBLE);
            btnPay.setVisibility(View.VISIBLE);
            btnNotSend.setVisibility(View.GONE);
            btnApplyHelp.setVisibility(View.GONE);
            btnOrderTrance.setVisibility(View.GONE);
            layoutTrace.setVisibility(View.GONE);
            tvOrderTime.setText("尚未支付");
            tvPayway.setText("尚未支付");
            tvOrderExpress.setText("￥" + listBean.getExpresscost());
            tvOrderPrice.setText("￥" + ValueUtils.toTwoDecimal(listBean.getTotalmoney()));
            tvShouldPay.setText("￥" + listBean.getTotalcost());
            tvRealPay.setText("￥0.00");
        }else if(listBean.getSend() == 0){
            btnCance.setVisibility(View.GONE);
            btnPay.setVisibility(View.GONE);
            btnNotSend.setVisibility(View.VISIBLE);
            btnApplyHelp.setVisibility(View.GONE);
            btnOrderTrance.setVisibility(View.GONE);
            layoutTrace.setVisibility(View.VISIBLE);
            tvExpressStatus.setText("正在揽收，等待发货");
            tvPayway.setText("支付宝支付");
            tvOrderExpress.setText("￥" + listBean.getExpresscost());
            tvOrderTime.setText(DateTimeUtils.getTime(listBean.getPayDate(),
                    new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)));
            tvShouldPay.setText("￥" + listBean.getTotalcost());
            tvRealPay.setText("￥" + listBean.getTotalcost());
            tvOrderPrice.setText("￥" + ValueUtils.toTwoDecimal(listBean.getTotalmoney()));
            tvTime.setText(DateTimeUtils.getTime(listBean.getPayDate(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        }else if(listBean.getSend() == 1){
            btnCance.setVisibility(View.VISIBLE);
            btnPay.setVisibility(View.GONE);
            btnNotSend.setVisibility(View.GONE);
            btnApplyHelp.setVisibility(View.VISIBLE);
            btnOrderTrance.setVisibility(View.VISIBLE);
            layoutTrace.setVisibility(View.VISIBLE);
            tvPayway.setText("支付宝支付");
            tvOrderPrice.setText("￥" + ValueUtils.toTwoDecimal(listBean.getTotalmoney()));
            tvOrderExpress.setText("￥" + listBean.getExpresscost());
            tvExpressStatus.setText("包裹已寄出，请耐心等候");
            tvOrderTime.setText(DateTimeUtils.getTime(listBean.getPayDate(),
                    new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)));
            tvShouldPay.setText("￥" + listBean.getTotalcost());
            tvRealPay.setText("￥" + listBean.getTotalcost());
        }
        tvOrderId.setText(listBean.getId() +"");
        tvOrderHour.setText(DateTimeUtils.getTime(listBean.getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        tvReceiver.setText(listBean.getName());
        tvPhone.setText(listBean.getTel());
        tvAddress.setText(listBean.getAddr());
    }

    private void setDatas() {
        mAdapter = new OrderDetialAdapter(this,listBean.getOrderdetails());
        lvOrder.setAdapter(mAdapter);
        scroll.smoothScrollTo(0, 0);
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(OrderDetial.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams();
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(this, 44),
                0, DensityUtils.dp2px(this, 48));
        scroll.setLayoutParams(fl);
    }

    @OnClick({R.id.layoutTrace, R.id.layoutBack, R.id.btnApplyHelp, R.id.btnOrderTrance,R.id.btnCance,
            R.id.btnPay,R.id.btnNotSend})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            /*case R.id.layoutTrace:
                intent = new Intent(OrderDetial.this, OrderTrace.class);
                startActivity(intent);
                break;*/
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnApplyHelp:
                Bundle bundle;
                if(listBean.getOrderdetails().size() == 1){
                    intent = new Intent(mContext,ApplySingleGoodsAftermarket.class);
                    bundle = new Bundle();
                    bundle.putSerializable("orderdetails", listBean.getOrderdetails().get(0));
                }else{
                    intent = new Intent(mContext,ApplyMoreGoodsAftermarket.class);
                    bundle = new Bundle();
                    bundle.putSerializable("orderdetails", (Serializable) listBean.getOrderdetails());
                }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnOrderTrance:
                intent = new Intent(mContext,OrderTrace.class);
                bundle = new Bundle();
                bundle.putString("name",listBean.getOrderdetails().get(0).getExpressname());
                bundle.putString("code",listBean.getOrderdetails().get(0).getExpresscode());
                bundle.putString("orderId",String.valueOf(listBean.getId()));
                bundle.putString("number",listBean.getOrderdetails().get(0).getExpressnumber());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnCance:
                canceOrder();
                break;
            case R.id.btnPay:
                intent = new Intent(mContext.getApplicationContext(),SelectedPayWay.class);
                intent.putExtra("orderId",String.valueOf(listBean.getId()));
                intent.putExtra("totalPrice",listBean.getTotalcost());
                startActivity(intent);
                break;
            case R.id.btnNotSend:
                ToastUtils.showSuccess(mContext,"催单消息已发出" + "\n" +
                        "请耐心等待...");
                break;
        }
    }

    private void canceOrder() {
        final String[] messages = new String[]{"删除后，订单将不再显示","是否确定删除"};
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setIcon(R.drawable.logo_sm)
                .setMessages(messages)
                //   .setContentView(R.layout.wheelview_add_size)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CancleOrderPre cancleOrderPre = new CancleOrderPre(OrderDetial.this);
                        cancleOrderPre.cancleOrder(String.valueOf(listBean.getId()));
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }


    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showCancleInfos(SuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getContext(), msg);
        } else {
            ToastUtils.showSuccess(getContext(), msg);
            AppManager.getAppManager().finishActivity(this);
        }
    }
}
