package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.alipay.sdk.app.PayTask;
import com.yunma.R;
import com.yunma.bean.SuccessResultBean;
import com.yunma.domain.PayResult;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.ALiPayInterface;
import com.yunma.jhuo.p.ALiPayPre;
import com.yunma.utils.*;

import java.lang.ref.WeakReference;

import butterknife.*;

public class SelectedPayWay extends MyCompatActivity implements ALiPayInterface.ALiPayView {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.imgWeChat) ImageView imgWeChat;
    @BindView(R.id.layoutWeChatPay) RelativeLayout layoutWeChatPay;
    @BindView(R.id.imgAliPay) ImageView imgAliPay;
    @BindView(R.id.layoutAliPay) RelativeLayout layoutAliPay;
    @BindView(R.id.imgJhuoPay) ImageView imgJhuoPay;
    @BindView(R.id.layoutJhuoPay) RelativeLayout layoutJhuoPay;
    @BindView(R.id.layout) View layout;
    @BindView(R.id.tvShowError) TextView tvShowError;
    private MyHandler mHandler = new MyHandler(this);
    private Context mContext;
    private String orderId = null;
    private ALiPayPre aLiPayPre = null;
    private String payWay = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payway);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBar();
        getDatas();

    }
    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(SelectedPayWay.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        aLiPayPre = new ALiPayPre(this);
    }

    private void getDatas() {
        Bundle datas = getIntent().getExtras();
        double totalCost = datas.getDouble("totalPrice");
        orderId = datas.getString("orderId");
        String s ="￥" +  ValueUtils.toTwoDecimal(totalCost);
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,13),1,s.indexOf("."));
        tvTotalPrice.setText(ss);
        layoutAliPay.setVisibility(View.VISIBLE);
    }



    @OnClick({R.id.layoutBack, R.id.layoutWeChatPay, R.id.layoutAliPay, R.id.layoutJhuoPay})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutWeChatPay:
                payWay = "微信支付";
                intent = new Intent(SelectedPayWay.this,CompletedPayMent.class);
                startActivity(intent);
                break;
            case R.id.layoutAliPay:
                payWay = "支付宝支付";
                aLiPayPre.getALiPayInfos();
                break;
            case R.id.layoutJhuoPay:
                payWay = "余额支付";
                break;
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void showPayInfos(final SuccessResultBean successResultBean, String msg) {
        if(successResultBean==null){
            tvShowError.setText(msg);
            tvShowError.setVisibility(View.VISIBLE);
            layoutAliPay.setVisibility(View.GONE);
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            tvShowError.setVisibility(View.GONE);
            layoutAliPay.setVisibility(View.VISIBLE);
            if(!successResultBean.getSuccess().isEmpty()){
               /* Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(SelectedPayWay.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(successResultBean.getSuccess(), true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };*/
                // 必须异步调用
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(SelectedPayWay.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(successResultBean.getSuccess(), true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                }).start();
              //  payThread.start();
            }
        }
    }

   /* @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    {
                    PayResult payResult = new PayResult((String)msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.showSuccess(SelectedPayWay.this, "支付成功");
                        Intent intent = new Intent(SelectedPayWay.this,CompletedPayMent.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("payWay",payWay);
                        bundle.putString("orderId",orderId);
                        bundle.putString("totalPrice",tvTotalPrice.getText().toString().trim());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.showInfo(SelectedPayWay.this, "支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.showError(SelectedPayWay.this, "支付失败");
                        }
                    }
                    break;
                }

            }
        }

    };*/

    private static class MyHandler extends Handler{
        WeakReference<SelectedPayWay> mActivity;
        MyHandler(SelectedPayWay activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SelectedPayWay theActivity = mActivity.get();
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String)msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.showSuccess(theActivity, "支付成功");
                        Intent intent = new Intent(theActivity,CompletedPayMent.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("payWay",theActivity.payWay);
                        bundle.putString("orderId",theActivity.orderId);
                        bundle.putString("totalPrice",theActivity.tvTotalPrice.getText().toString().trim());
                        intent.putExtras(bundle);
                        theActivity.startActivity(intent);
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.showInfo(theActivity, "支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.showError(theActivity, "支付失败");
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    }

}
