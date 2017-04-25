package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.SimilarGoodsAdapter;
import com.yunma.bean.GetCollectResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.*;

import butterknife.*;

public class SimilarGoods extends MyCompatActivity {

    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.imgMessage)
    ImageView imgMessage;
    @BindView(R.id.imgRemind)
    ImageView imgRemind;
    @BindView(R.id.layoutNews)
    RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar)
    LinearLayout layouStatusBar;
    @BindView(R.id.imgGoods)
    ImageView imgGoods;
    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;
    @BindView(R.id.lvSimilarGoods)
    ListView lvSimilarGoods;
    private Context mContext;
    private SimilarGoodsAdapter mAdapter;
    private GetCollectResultBean.SuccessBean successBean = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_goods);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        getDatas();
        setDatas();
    }

    private void getDatas() {
        successBean = (GetCollectResultBean.SuccessBean)
                this.getIntent().getSerializableExtra("goodsInfo");
        assert successBean!=null;
        if(successBean.getRepoid()==1){
            if(successBean.getPic()!=null){
                GlideUtils.glidNormleFast(mContext,imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        successBean.getPic().split(",")[0]);
            } else{
                imgGoods.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.default_pic));
            }
        }else{
            if(successBean.getPic()!=null){
                GlideUtils.glidNormleFast(mContext,imgGoods, ConUtils.GOODS_IMAGE_URL +
                        successBean.getPic());
            } else{
                imgGoods.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.default_pic));
            }

        }
        tvGoodsName.setText(successBean.getName());
        String price = "￥" + String.valueOf(successBean.getYunmaprice());
        tvGoodsPrice.setText(ValueUtils.changeTextSize(price,
                42,price.indexOf("￥"),price.indexOf(".")));
    //    getSimilarGoods();
    }

    private void getSimilarGoods() {

    }

    private void setDatas() {
        mAdapter = new SimilarGoodsAdapter(this);
        lvSimilarGoods.setAdapter(mAdapter);
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(SimilarGoods.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getApplicationContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(SimilarGoods.this,ESQ,
                        "你好，我正在J货看" + tvGoodsName.getText().toString());
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
