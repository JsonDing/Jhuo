package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.InvoiceGoodsAdapter;
import com.yunma.adapter.SelectMonthAdapter;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;
import com.yunma.widget.NestListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.*;

public class ToGetInvoice extends MyCompatActivity {
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.layout)
    View layout;
    @BindView(R.id.lvSelectingMonth)
    NestListView lvSelectingMonth;
    @BindView(R.id.lvGoodsList)
    ListView lvGoodsList;
    @BindView(R.id.rbButton)
    CheckBox rbButton;
    @BindView(R.id.tvHadSelected)
    TextView tvHadSelected;
    @BindView(R.id.tvNeedInvoicePrice)
    TextView tvNeedInvoicePrice;
    @BindView(R.id.btnGoInvoice)
    Button btnGoInvoice;
    private Context mContext;
    private String currentMonth;
    private int currentPos;
    private SelectMonthAdapter mAdapter;
    private InvoiceGoodsAdapter nAdapter;
    private static String[] mMonth = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月",
            "9月", "10月", "11月", "12月"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_get_invoice);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        currentMonth = formatter.format(curDate);
        currentPos = Integer.valueOf(currentMonth);
        if (currentPos < 10) {
            currentMonth = currentMonth.substring(1, 2) + "月";
        }
        LogUtils.log("-----> " + currentMonth);
    }

    private void setDatas() {
        mAdapter = new SelectMonthAdapter(this, mMonth, currentMonth);
        lvSelectingMonth.setAdapter(mAdapter);
        lvSelectingMonth.smoothMoveToPosition((currentPos - 1));
        nAdapter = new InvoiceGoodsAdapter(this);
        lvGoodsList.setAdapter(nAdapter);
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(ToGetInvoice.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }


    @OnClick({R.id.layoutBack, R.id.btnGoInvoice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnGoInvoice:
                Intent intent = new Intent(ToGetInvoice.this,InvoiceInfos.class);
                startActivity(intent);
                break;
        }
    }
}
