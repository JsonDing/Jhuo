package com.yunma.jhuo.general;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.utils.AppManager;
import com.yunma.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopWindowsActivity extends MyCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.tvTittle)
    TextView tvTittle;
    @BindView(R.id.imgsRight)
    ImageView imgsRight;
    @BindView(R.id.layoutRight)
    LinearLayout layoutRight;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.sAddCart)
    Switch sAddCart;
    @BindView(R.id.sSaleRemind)
    Switch sSaleRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_windows);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initBar();
        initSetting();
    }

    private void initBar() {
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);
        tvTittle.setText("弹窗设置");
    }

    private void initSetting() {
        sAddCart.setChecked(SPUtils.isAddCartRemind(this));
        sSaleRemind.setChecked(SPUtils.isSaleRemind(this));
        sAddCart.setOnCheckedChangeListener(this);
        sSaleRemind.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.layoutBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.sAddCart:
                SPUtils.setAddCartRemind(this,isChecked);
                break;
            case R.id.sSaleRemind:
                SPUtils.setSaleRemind(this,isChecked);
                break;
        }
    }
}
