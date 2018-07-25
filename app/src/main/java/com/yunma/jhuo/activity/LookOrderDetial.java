package com.yunma.jhuo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.yunma.R;
import com.yunma.adapter.LookOrderDetialAdapter;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;
import com.yunma.widget.MyListView;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.*;

public class LookOrderDetial extends MyCompatActivity {
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layout1) View layout1;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.tvOrderId) TextView tvOrderId;
    @BindView(R.id.tvOrderHour) TextView tvOrderHour;
    @BindView(R.id.tvExpressStatus) TextView tvExpressStatus;
    @BindView(R.id.tvDay) TextView tvDay;
    @BindView(R.id.tvReceiver) TextView tvReceiver;
    @BindView(R.id.tvPhone) TextView tvPhone;
    @BindView(R.id.tvAddress) TextView tvAddress;
    @BindView(R.id.lvOrder) MyListView lvOrder;
    @BindView(R.id.tvOrderTime) TextView tvOrderTime;
    @BindView(R.id.tvPayway) TextView tvPayway;
    @BindView(R.id.tvOrderPrice) TextView tvOrderPrice;
    @BindView(R.id.tvLuckyBean) TextView tvLuckyBean;
    @BindView(R.id.tvOrderExpress) TextView tvOrderExpress;
    @BindView(R.id.tvRealPay) TextView tvRealPay;
    @BindView(R.id.btnOrderTrance) Button btnOrderTrance;
    @BindView(R.id.layoutTrace) RelativeLayout layoutTrace;
    private LookOrderDetialAdapter mAdapter;
    private LookOrderBean lookOrderBean = null;
    private FailedResultBean failedResultBean = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_order_detial);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        getDatas();
    }

    private void setDatas(LookOrderBean lookOrderBean) {
        tvOrderId.setText(String.valueOf(lookOrderBean.getSuccess().getId()));
        tvOrderHour.setText(DateTimeUtils.getTime(lookOrderBean.getSuccess().getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        tvDay.setText(DateTimeUtils.getTime(lookOrderBean.getSuccess().getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        tvReceiver.setText(lookOrderBean.getSuccess().getName());
        tvPhone.setText(ValueUtils.hideNumber(lookOrderBean.getSuccess().getTel()));
        tvAddress.setText(lookOrderBean.getSuccess().getAddr());
        tvOrderTime.setText(DateTimeUtils.getTime(lookOrderBean.getSuccess().getPayDate(),
                new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)));
        tvOrderPrice.setText(ValueUtils.toTwoDecimal(lookOrderBean.getSuccess().getTotalcost()));
        tvRealPay.setText(ValueUtils.toTwoDecimal(lookOrderBean.getSuccess().getTotalcost()));
        tvOrderExpress.setText(ValueUtils.toTwoDecimal(lookOrderBean.getSuccess().getExpresscost()));
        if(lookOrderBean.getSuccess().getPay()==1){
            tvPayway.setText("支付宝支付");
        }else {
            tvPayway.setText("微信支付");
        }
        if(lookOrderBean.getSuccess().getCouponMoney() != 0.00){
            tvLuckyBean.setText(ValueUtils.toTwoDecimal(
                    Double.valueOf(lookOrderBean.getSuccess().getCouponMoney())));
        }else{
            tvLuckyBean.setText(String.valueOf("0.00"));
        }
        mAdapter = new LookOrderDetialAdapter(this, lookOrderBean.getSuccess().getOrderdetails());
        lvOrder.setAdapter(mAdapter);
        scroll.smoothScrollTo(0, 0);
    }

    private void getDatas() {
        String orderId = getIntent().getExtras().getString("orderId");
        RequestParams params = new RequestParams(ConUtils.LOOL_ORDER);
        OrderIdBean orderIdBean = new OrderIdBean();
        orderIdBean.setToken(SPUtils.getToken(mContext));
        orderIdBean.setId(orderId);
        String strBodyContent = new Gson().toJson(orderIdBean);
        LogUtils.json("查看订单请求: ------------>" + strBodyContent);
        params.setAsJsonContent(true);
        params.setBodyContent(strBodyContent);
        x.http().post(params, new Callback.CacheCallback<String>() {
            private boolean hasError = false;
            private String result = null;
            @Override
            public boolean onCache(String result) {
                this.result = result;
                return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    this.result = result;
                    LogUtils.json("查看订单: " + result);
                    if(result.contains("success")){
                        try {
                            lookOrderBean = GsonUtils.GsonToBean(result,
                                    LookOrderBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(mContext,"数据解析出错");
                            return;
                        }
                        setDatas(lookOrderBean);
                    }else{
                        try {
                            failedResultBean = GsonUtils.GsonToBean(result,
                                    FailedResultBean.class);
                        } catch (Exception e) {
                            ToastUtils.showError(mContext,"数据解析出错");
                            return;
                        }
                        ToastUtils.showError(mContext,failedResultBean.getFailed().getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                if (ex instanceof HttpException) {
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    ToastUtils.showInfo(mContext,"网络出错");
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                } else { // 其他错误
                    ToastUtils.showInfo(mContext,"服务器出错");
                    LogUtils.json("-----------> " + ex.getMessage() + "\n" + ex.getCause() + "\n"
                    + ex.getLocalizedMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtils.showInfo(mContext,"cancelled");
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.json("查看订单: " + result);
                }
            }
        });
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(LookOrderDetial.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) scroll.getLayoutParams();
        fl.setMargins(0, statusHeight + DensityUtils.dp2px(this, 44), 0,0);
        scroll.setLayoutParams(fl);

    }

    @OnClick({ R.id.layoutBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    private class OrderIdBean extends TokenBean{
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}
