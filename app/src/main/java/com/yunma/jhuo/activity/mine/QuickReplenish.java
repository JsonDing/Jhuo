package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.scu.miomin.shswiperefresh.view.SHListView;
import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SelfGoodsSizeNumsChoose;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.adapter.QuickReplenishAdapter;
import com.yunma.bean.BuyRecordBean;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.BuyRecordInterface;
import com.yunma.jhuo.m.SearchGoodsInterface;
import com.yunma.jhuo.p.BuyRecordPre;
import com.yunma.jhuo.p.SearchSelfGoodsByCodePre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;

public class QuickReplenish extends MyCompatActivity implements BuyRecordInterface.BuyRecordView,
        QuickReplenishAdapter.OnbuyClick, SearchGoodsInterface.SearchSelfGoodsView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvQuickReplenish) SHListView lvQuickReplenish;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layoutGoLook) RelativeLayout layoutGoLook;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    private Context mContext;
    private int selectPos = -1;
    private BuyRecordPre buyRecordPre = null;
    private QuickReplenishAdapter mAdapter;
    private SearchSelfGoodsByCodePre searchPre = null;
    private List<BuyRecordBean.SuccessBean.ListBean> listBean = null;
    private int nextPage = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_replenish);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBarAndNavigationBar();
        initSwipeRefreshLayout();
        getDatas();
    }

    private void getDatas() {
        buyRecordPre = new BuyRecordPre(this);
        searchPre = new SearchSelfGoodsByCodePre(this);
        buyRecordPre.getBuyRecord(mContext, "1", "10");
    }

    private void initStatusBarAndNavigationBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(QuickReplenish.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews,R.id.layoutGoLook})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",QuickReplenish.this);
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(QuickReplenish.this,ESQ,null);
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.layoutGoLook:
                Intent intent = new Intent(mContext, SpecialPriceActivity.class);
                startActivity(intent);
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    @Override
    public void toShowBuyRecord(BuyRecordBean recordBean, String msg) {
        if (recordBean == null) {
            ToastUtils.showError(mContext, msg);
            layoutNull.setVisibility(View.VISIBLE);
            lvQuickReplenish.setVisibility(View.GONE);
        } else {
            if (recordBean.getSuccess().getList() != null) {
                if(recordBean.getSuccess().isHasNextPage()){
                    swipeRefreshLayout.setLoadmoreEnable(true);
                }else{
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                nextPage = recordBean.getSuccess().getNextPage();
                if (recordBean.getSuccess().getList().size() != 0) {
                    layoutNull.setVisibility(View.GONE);
                    lvQuickReplenish.setVisibility(View.VISIBLE);
                    if (mAdapter == null) {
                        listBean = recordBean.getSuccess().getList();
                        mAdapter = new QuickReplenishAdapter(this,listBean,QuickReplenish.this);
                        lvQuickReplenish.setAdapter(mAdapter);
                    } else {
                        listBean.addAll(recordBean.getSuccess().getList());
                        mAdapter.setListBean(listBean);
                        swipeRefreshLayout.finishLoadmore();

                    }
                } else {
                    layoutNull.setVisibility(View.VISIBLE);
                    lvQuickReplenish.setVisibility(View.GONE);
                }
            } else {
                layoutNull.setVisibility(View.VISIBLE);
                lvQuickReplenish.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBuyClick(int position, BuyRecordBean.SuccessBean.ListBean buyListBean) {
        selectPos = position;
        searchPre.searchSelfGoodsByCode(this,buyListBean.getNumber(),"12");

    }

    @Override
    public void showSearchInfos(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showSuccess(mContext,msg);
        }else{
            if(resultBean.getSuccess().getList()==null){
                ToastUtils.showInfo(mContext,"该商品已下架");
            }else if(resultBean.getSuccess().getList().size()==0){
                ToastUtils.showInfo(mContext,"该商品已售空");
            }else{
                Intent intent = new Intent(QuickReplenish.this,SelfGoodsSizeNumsChoose.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selfGoods",resultBean.getSuccess().getList().get(selectPos));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.head);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        final View view = inflater.inflate(R.layout.refresh_head_view, null);
        final View view1 = inflater.inflate(R.layout.refresh_view, null);
        final ImageView imgSun = (ImageView)view.findViewById(R.id.imgSun);
        final TextView textView2 = (TextView) view1.findViewById(R.id.title);
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setRefreshEnable(false);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                        imgSun.clearAnimation();
                    }
                }, 1600);
            }

            @Override
            public void onLoading() {
                buyRecordPre.getBuyRecord(mContext, String.valueOf(nextPage), "10");
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishLoadmore();
                    }
                }, 1500);
            }


            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        imgSun.startAnimation(operatingAnim);
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        textView2.setText("正在加载...");
                        JumpingBeans.with(textView2)
                                .appendJumpingDots()
                                .build();
                        break;
                }
            }
        });
    }
}
