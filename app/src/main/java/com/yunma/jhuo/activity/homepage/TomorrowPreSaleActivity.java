package com.yunma.jhuo.activity.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yalantis.taurus.PullToRefreshView;
import com.yunma.R;
import com.yunma.adapter.SelfIssueGoodsAdapter;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetIssueView;
import com.yunma.jhuo.p.AddGoodsRemindPre;
import com.yunma.jhuo.p.SelfIssueGoodsPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.MySpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

import static org.xutils.common.util.DensityUtil.dip2px;

public class TomorrowPreSaleActivity extends MyCompatActivity implements
        GetIssueView, SelfIssueGoodsAdapter.OnClickListener, SelfGoodsInterFace.AddSelfGoodsRemindView {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgsRight) ImageView imgsRight;
    @BindView(R.id.layoutRight) LinearLayout layoutRight;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;
    private SelfIssueGoodsPre issueGoodsPre = null;
    private SelfIssueGoodsAdapter mAdapter = null;
    private Activity mActivity = null;
    private AddGoodsRemindPre addGoodsRemindPre = null;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean = null;

    // TODO: 2017-04-19
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrow_pre_sale);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initView();
        tomorrowPreSaleDatas();
    }

    private void initView() {
        mActivity = this;
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);
        tvTittle.setText("明日上新");
        imgsRight.setImageResource(R.drawable.re_message);
        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                issueGoodsPre.getSpecialOfferGoods(TomorrowPreSaleActivity.this, "issue", "12", 1);
                pullToRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    private void tomorrowPreSaleDatas() {
        issueGoodsPre = new SelfIssueGoodsPre(TomorrowPreSaleActivity.this);
        issueGoodsPre.getSpecialOfferGoods(this, "issue", "12", 1);
        recyclerview.setHasFixedSize(true);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isHeaderView(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.addItemDecoration(new MySpacesItemDecoration(dip2px(5), dip2px(5)));
        recyclerview.setItemAnimator(new SlideInLeftAnimator());
        mAdapter = new SelfIssueGoodsAdapter(mActivity, TomorrowPreSaleActivity.this);
        recyclerview.setAdapter(mAdapter);
    }

    @OnClick({R.id.layoutBack, R.id.layoutRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutRight:

                break;
        }
    }

    @Override
    public void showIssueGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(this, msg);
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                listBean = resultBean.getSuccess().getList();
                mAdapter.setListBean(listBean);
            }else{
                // TODO: 2017-04-17   无数据提示
            }
        }
    }


    @Override
    public void lookDetials(int position,
                            GetSelfGoodsResultBean.SuccessBean.ListBean detialsBean) {
        Intent intent = new Intent(TomorrowPreSaleActivity.this,TomorrowPreSaleDetials.class);
        Bundle bundle =  new Bundle();
        bundle.putSerializable("TomorrowPreSaleDetials",detialsBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void remindMeClick(String goodsNums) {
        if(addGoodsRemindPre==null){
            addGoodsRemindPre = new AddGoodsRemindPre(TomorrowPreSaleActivity.this);
        }
        addGoodsRemindPre.addGoodsRemind(this,goodsNums);
    }

    @Override
    public void showSelfGoodsRemindInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
        }
    }
}
