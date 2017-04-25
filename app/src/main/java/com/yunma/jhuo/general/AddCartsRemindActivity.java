package com.yunma.jhuo.general;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.utils.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCartsRemindActivity extends MyCompatActivity {

    @BindView(R.id.tvNums)
    TextView tvNums;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnGo)
    Button btnGo;
    @BindView(R.id.imgsClose)
    ImageView imgsClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carts_remind);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        getIntentDatas();
    }

    private void getIntentDatas() {
        Bundle bundle = this.getIntent().getExtras();
        if(bundle.getString("totalNums") !=null &&
                bundle.getString("tvTotalPrice") != null){
            tvMoney.setText(bundle.getString("tvTotalPrice"));
            tvNums.setText(bundle.getString("totalNums"));
        }
    }

    @OnClick({R.id.btnCancle, R.id.btnGo, R.id.imgsClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCancle:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(0,R.anim.fade_out);
                break;
            case R.id.btnGo:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(0,R.anim.fade_out);
                break;
            case R.id.imgsClose:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(0,R.anim.fade_out);
                break;
        }
    }
}
