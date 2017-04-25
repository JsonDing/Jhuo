package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.CarriageExplainAdapter;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.ScreenUtils;

import butterknife.*;

public class CarriageExplain extends MyCompatActivity {

    @BindView(R.id.layoutBack)
    RelativeLayout layoutBack;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.lvExplain)
    ListView lvExplain;
    @BindView(R.id.layout)
    View layout;
    private Context mContext;
    private CarriageExplainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carriage_explain);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {

    }

    private void setDatas() {
        mAdapter = new CarriageExplainAdapter(mContext);
        lvExplain.setAdapter(mAdapter);

    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(CarriageExplain.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }
    @OnClick(R.id.layoutBack)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }


}
