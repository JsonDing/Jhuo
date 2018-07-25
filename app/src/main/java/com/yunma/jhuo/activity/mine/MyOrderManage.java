package com.yunma.jhuo.activity.mine;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.jhuo.activity.ContactUsActivity;
import com.yunma.jhuo.fragment.OrderWaitToPay;
import com.yunma.jhuo.fragment.OrderWaitToReceive;
import com.yunma.jhuo.fragment.OrderWaitToSend;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private int selectPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_manage);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        fragmentManager = getFragmentManager();
        selectPos = this.getIntent().getExtras().getInt("position");
        setTabSelection(selectPos);
    }

    private void setTabSelection(int position) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (position) {
            case 0:
                if (orderWaitToPay == null) {
                    orderWaitToPay = new OrderWaitToPay();
                    transaction.add(R.id.content, orderWaitToPay);
                } else {
                    transaction.show(orderWaitToPay);
                }
                tvWaitPay.setTextColor(ContextCompat.getColor(this,R.color.b3));
                waitPayLine.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (orderWaitToSend == null) {
                    orderWaitToSend = new OrderWaitToSend();
                    transaction.add(R.id.content, orderWaitToSend);
                } else {
                    transaction.show(orderWaitToSend);
                }
                tvSendOut.setTextColor(ContextCompat.getColor(this,R.color.b3));
                sendOutLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (orderWaitToReceive == null) {
                    orderWaitToReceive = new OrderWaitToReceive();
                    transaction.add(R.id.content, orderWaitToReceive);
                } else {
                    transaction.show(orderWaitToReceive);
                }
                tvReceipt.setTextColor(ContextCompat.getColor(this,R.color.b3));
                receiptLine.setVisibility(View.VISIBLE);
                break;
        }
       // transaction.commit();
        transaction.commitAllowingStateLoss();

    }

    private void clearSelection() {
        tvWaitPay.setTextColor(ContextCompat.getColor(this,R.color.b1));
        tvSendOut.setTextColor(ContextCompat.getColor(this,R.color.b1));
        tvReceipt.setTextColor(ContextCompat.getColor(this,R.color.b1));
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
                if (selectPos != 0) {
                    selectPos = 0;
                    setTabSelection(0);
                }
                break;
            case R.id.layoutSendOut:
                if (selectPos != 1) {
                    selectPos = 1;
                    setTabSelection(1);
                }
                break;
            case R.id.layoutReceipt:
                if (selectPos != 2) {
                    selectPos = 2;
                    setTabSelection(2);
                }
                break;
            case R.id.layoutNews:
                Intent intent = new Intent(this,ContactUsActivity.class);
                startActivity(intent);
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
