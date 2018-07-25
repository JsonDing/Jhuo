package com.yunma.jhuo.activity.homepage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yunma.R;
import com.yunma.bean.AliPayResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.bean.WeChatPayResultBean;
import com.yunma.domain.PayResult;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.PayInterface;
import com.yunma.jhuo.m.GetShoppingCartListInterface;
import com.yunma.jhuo.p.ALiPayPre;
import com.yunma.jhuo.p.DeleteShoppingCartPre;
import com.yunma.jhuo.p.WeChatPayPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GsonUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.jhuo.m.PayInterface.*;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPayWay extends MyCompatActivity implements PayInterface.ALiPayView,
        WeChatPayView, GetShoppingCartListInterface.DelShoppingCartView {
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.tvShowError) TextView tvShowError;
    private MyHandler mHandler = new MyHandler(this);
    private String payWay = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payway);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);
        getDatas();
    }

    private void getDatas() {
        String s ="￥" +  ValueUtils.toTwoDecimal(getIntent().getExtras().getDouble("totalPrice"));
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.b4,s, Typeface.NORMAL,
                13,1,s.indexOf("."));
        tvTotalPrice.setText(ss);
        SPUtils.setWeChatOrderPay(this,ValueUtils.toTwoDecimal(getIntent().getExtras().getDouble("totalPrice")));
        LogUtils.json("要删除的购物车ids: " + SPUtils.getDelCartGoodsIds(this));
        DeleteShoppingCartPre delPresenter = new DeleteShoppingCartPre(this);
        delPresenter.delGoods(this,SPUtils.getDelCartGoodsIds(this));
    }

    /**
     * 返回上一页
     * @param view
     */
    public void layoutBack(View view) {
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 支付宝支付
     * @param view
     */
    public void layoutAliPay(View view) {
        payWay = "支付宝支付";
        ALiPayPre aLiPayPre = new ALiPayPre(this);
        aLiPayPre.getALiPayInfos(this,getIntent().getExtras().getString("orderId"));
    }

    /**
     * 微信支付
     * 3627FCF676057AAE3124E31C151388248F86C535
     * @param view
     */
    public void layoutWeChatPay(View view) {
        payWay = "微信支付";
        SPUtils.setWeChatOrderId(this,getIntent().getExtras().getString("orderId"));
        WeChatPayPre weChatPayPre = new WeChatPayPre(this);
        weChatPayPre.getWeChatPayInfos(this,getIntent().getExtras().getString("orderId"));
        //ToastUtils.showInfo(this,"即将开放，敬请等待...");
    }

    /**
     * 余额支付
     * @param view
     */
    public void layoutJhuoPay(View view) {
        payWay = "余额支付";
    }

    @Override
    public void showWeChatPayInfos(WeChatPayResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else {
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(this,ConUtils.WECHAT_ID);
            iwxapi.registerApp(ConUtils.WECHAT_ID);
            PayReq payReq = new PayReq();
            payReq.appId = ConUtils.WECHAT_ID;
            payReq.partnerId = resultBean.getSuccess().getPartnerid();
            payReq.prepayId = resultBean.getSuccess().getPrepayid();
            payReq.packageValue = resultBean.getSuccess().getPackageX();
            payReq.nonceStr = resultBean.getSuccess().getNoncestr();
            payReq.timeStamp = resultBean.getSuccess().getTimestamp();
            payReq.sign = resultBean.getSuccess().getSign();
            iwxapi.sendReq(payReq);
        }
    }

    @Override
    public void showALiPayInfos(final SuccessResultBean successResultBean, String msg) {
        if(successResultBean==null){
            tvShowError.setText(msg);
            tvShowError.setVisibility(View.VISIBLE);
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            tvShowError.setVisibility(View.GONE);
            if(!successResultBean.getSuccess().isEmpty()){
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
            }
        }
    }

    @Override
    public void showDelInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            LogUtils.json("删除失败！！！");
        }else{
            LogUtils.json("删除成功！！！");
        }
        if(SPUtils.contains(this,"delCartGoodsIds")){
            SPUtils.remove(this,"delCartGoodsIds");
        }
    }

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
                    String totalPay = null;
                    try {
                        AliPayResultBean payResultBean = GsonUtils.GsonToBean(resultInfo,
                                AliPayResultBean.class);
                        totalPay = payResultBean.getAlipay_trade_app_pay_response().getTotal_amount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.showSuccess(theActivity, "支付成功");
                        Intent intent = new Intent(theActivity,CompletedPayMent.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("payWay",theActivity.payWay);
                        bundle.putString("orderId",theActivity.getIntent().getExtras().getString("orderId"));
                        bundle.putString("totalPrice",totalPay);
                        intent.putExtras(bundle);
                        theActivity.startActivity(intent);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        ToastUtils.showInfo(theActivity, "支付结果确认中");
                    } else if (TextUtils.equals(resultStatus, "6001")){
                        ToastUtils.showInfo(theActivity, "已取消");
                    } else if (TextUtils.equals(resultStatus, "6002")){
                        ToastUtils.showInfo(theActivity, "网络连接出错");
                    } else if (TextUtils.equals(resultStatus, "4000")){
                        ToastUtils.showInfo(theActivity, "订单支付失败");
                    } else {
                        ToastUtils.showInfo(theActivity, "其它支付错误");
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
