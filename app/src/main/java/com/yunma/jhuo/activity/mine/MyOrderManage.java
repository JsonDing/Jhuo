package com.yunma.jhuo.activity.mine;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.jhuo.fragment.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import butterknife.*;


public class MyOrderManage extends MyCompatActivity {
    public static MyOrderManage orderManageContext;
    @BindView(R.id.waitPayLine) View waitPayLine;
    @BindView(R.id.layoutWaitPay) RelativeLayout layoutWaitPay;
    @BindView(R.id.sendOutLine) View sendOutLine;
    @BindView(R.id.layoutSendOut) RelativeLayout layoutSendOut;
    @BindView(R.id.receiptLine) View receiptLine;
    @BindView(R.id.layoutReceipt) RelativeLayout layoutReceipt;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.content) FrameLayout content;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.tvWaitPay) TextView tvWaitPay;
    @BindView(R.id.tvSendOut) TextView tvSendOut;
    @BindView(R.id.tvReceipt) TextView tvReceipt;
    private OrderWaitToPay orderWaitToPay = null;
    private OrderWaitToSend orderWaitToSend = null;
    private OrderWaitToReceive orderWaitToReceive = null;
    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_manage);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        fragmentManager = getFragmentManager();
        getIntentValue();
    }

    private void getIntentValue() {
        int position = this.getIntent().getExtras().getInt("position");
        setTabSelection(position);
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
                if (orderWaitToPay == null) {
                    orderWaitToPay = new OrderWaitToPay();
                    transaction.add(R.id.content, orderWaitToPay);
                } else {
                    transaction.show(orderWaitToPay);
                }
                tvWaitPay.setTextColor(getResources().getColor(R.color.color_b3));
                waitPayLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (orderWaitToSend == null) {
                    orderWaitToSend = new OrderWaitToSend();
                    transaction.add(R.id.content, orderWaitToSend);
                } else {
                    transaction.show(orderWaitToSend);
                }
                tvSendOut.setTextColor(getResources().getColor(R.color.color_b3));
                sendOutLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (orderWaitToReceive == null) {
                    orderWaitToReceive = new OrderWaitToReceive();
                    transaction.add(R.id.content, orderWaitToReceive);
                } else {
                    transaction.show(orderWaitToReceive);
                }
                tvReceipt.setTextColor(getResources().getColor(R.color.color_b3));
                receiptLine.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();

    }

    private void clearSelection() {
        tvWaitPay.setTextColor(getResources().getColor(R.color.color_b1));
        tvSendOut.setTextColor(getResources().getColor(R.color.color_b1));
        tvReceipt.setTextColor(getResources().getColor(R.color.color_b1));
        waitPayLine.setVisibility(View.INVISIBLE);
        sendOutLine.setVisibility(View.INVISIBLE);
        receiptLine.setVisibility(View.INVISIBLE);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (orderWaitToPay != null) {
            transaction.hide(orderWaitToPay);
        }
        if (orderWaitToSend != null) {
            transaction.hide(orderWaitToSend);
        }
        if (orderWaitToReceive != null) {
            transaction.hide(orderWaitToReceive);
        }
    }

    private void initStatusBar() {
        orderManageContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(MyOrderManage.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutWaitPay, R.id.layoutSendOut, R.id.layoutReceipt, /*R.id.layoutHasConfirm,*/
            R.id.layoutBack,R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutWaitPay:
                setTabSelection(0);
                break;
            case R.id.layoutSendOut:
                setTabSelection(1);
                break;
            case R.id.layoutReceipt:
                setTabSelection(2);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getApplicationContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(MyOrderManage.this,ESQ,null);
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderWaitToPay = null;
        orderWaitToSend = null;
        orderWaitToReceive = null;
        fragmentManager = null;
    }
}
