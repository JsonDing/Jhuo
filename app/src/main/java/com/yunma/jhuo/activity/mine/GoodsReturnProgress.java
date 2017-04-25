package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.ReturnProgressAdapter;
import com.yunma.bean.ServiceResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GoodsRefundInterface.OnGoodsRefundProgressAdapterClick;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import butterknife.*;

public class GoodsReturnProgress extends MyCompatActivity implements OnGoodsRefundProgressAdapterClick{

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.imgReturnStatusLogo) ImageView imgReturnStatusLogo;
    @BindView(R.id.tvReturnStatus) TextView tvReturnStatus;
    @BindView(R.id.tvOrderCode) TextView tvOrderCode;
    @BindView(R.id.tvApplyTime) TextView tvApplyTime;
    @BindView(R.id.lvReturnProgress) ListView lvReturnProgress;
    private Context mContext;
    private ServiceResultBean.SuccessBean.ListBean listBean = null;
    private ReturnProgressAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_reback_progress);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getDatas();
    }

    private void getDatas() {
        listBean = (ServiceResultBean.SuccessBean.ListBean) getIntent().getSerializableExtra("goodsDetial");
        if(EmptyUtil.isNotEmpty(listBean)){
            tvOrderCode.setText(listBean.getOid());
            tvApplyTime.setText(DateTimeUtils.getTime(listBean.getRefundDate(),
                    DateTimeUtils.DEFAULT_DATE_FORMAT));
            if(listBean.getRefund()==2||listBean.getRefund()==1){
                tvReturnStatus.setText("退款进行中");
                JumpingBeans.with(tvReturnStatus)
                        .appendJumpingDots()
                        .build();
                GlideUtils.glidLocalDrawable(mContext,imgReturnStatusLogo,R.drawable.being_processed);
            }else if(listBean.getRefund()==3){
                tvReturnStatus.setText("退款成功");
                GlideUtils.glidLocalDrawable(mContext,imgReturnStatusLogo,R.drawable.succeed);
            }else{
                tvReturnStatus.setText("退款失败");
                GlideUtils.glidLocalDrawable(mContext,imgReturnStatusLogo,R.drawable.be_defeated);
            }
            mAdapter =  new ReturnProgressAdapter(this,listBean,GoodsReturnProgress.this);
            lvReturnProgress.setAdapter(mAdapter);
        }
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(GoodsReturnProgress.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",mContext);
                WPA mWPA = new WPA(GoodsReturnProgress.this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(GoodsReturnProgress.this,ESQ,
                        "你好");
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onLookDetialClickListener(ServiceResultBean.SuccessBean.ListBean listBean) {
        Intent intent = new Intent(mContext,GoodsReturnDetial.class);
        intent.putExtra("goodsDetial", listBean);
        startActivity(intent);
    }

    @Override
    public void onWriteExpressClickListener(String serviceId) {
        Intent intent = new Intent(GoodsReturnProgress.this,ToWriteExpress.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        bundle.putString("serviceId",serviceId);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == 1){
                listBean.setRefund(2);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
