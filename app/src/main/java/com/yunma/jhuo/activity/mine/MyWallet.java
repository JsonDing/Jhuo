package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.MyWalletPayListAdapter;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import butterknife.*;

public class MyWallet extends MyCompatActivity {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.btnWithdraw)
    Button btnWithdraw;
    @BindView(R.id.btnPay)
    Button btnPay;
    @BindView(R.id.lvPayList)
    ListView lvPayList;
    @BindView(R.id.layout)
    View layout;
    private Context mContext;
    private MyWalletPayListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {

    }

    private void setDatas() {
        SpannableStringBuilder ss = ValueUtils.changeTextSize(
                "￥999.00",DensityUtils.sp2px(mContext,38), "￥999.00".indexOf("￥"), "￥999.00".indexOf("."));
        tvMoney.setText(ss);
        mAdapter = new MyWalletPayListAdapter(MyWallet.this);
        lvPayList.setAdapter(mAdapter);
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(MyWallet.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        /*int navigationBarHeight = ScreenUtils.getNavigationBarHeight(MyWallet.this);
        //取控件textView当前的布局参数
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        linearParams.height = navigationBarHeight;// 控件的高强制设成
        linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件*/
    }

    @OnClick({R.id.layoutBack, R.id.btnWithdraw, R.id.btnPay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnWithdraw:

                break;
            case R.id.btnPay:
                break;
        }
    }
}
