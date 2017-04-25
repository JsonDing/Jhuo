package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.GoodsManifestAdapter;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import butterknife.*;

public class GoodsMnifest extends MyCompatActivity {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvGoodsList) ListView lvGoodsList;
    @BindView(R.id.tvTotalNums) TextView tvTotalNums;
    @BindView(R.id.layout) View layout;
    private Context mContext;
    private GoodsManifestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_mnifest);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        setDates();
    }

    private void setDates() {
        GetShoppingListBean resultBean = (GetShoppingListBean) getIntent()
                .getSerializableExtra("goodsListBean");
        tvTotalNums.setText(getIntent().getStringExtra("totalNums"));
        if(resultBean!=null){
            if(mAdapter==null){
                mAdapter = new GoodsManifestAdapter(this,resultBean);
                lvGoodsList.setAdapter(mAdapter);
            }else{
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(GoodsMnifest.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
       /* int navigationBarHeight = ScreenUtils.getNavigationBarHeight(GoodsMnifest.this);
        //取控件textView当前的布局参数
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        linearParams.height = navigationBarHeight;// 控件的高强制设成
        linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件*/
    }

    @OnClick(R.id.layoutBack)
    public void onClick() {
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(GoodsMnifest.this);
    }
}
