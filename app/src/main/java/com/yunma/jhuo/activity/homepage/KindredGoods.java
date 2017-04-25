package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.KindredGoodsListAdapter;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SearchGoodsInterface;
import com.yunma.jhuo.p.SearchGoodsByTypePre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;

public class KindredGoods extends MyCompatActivity implements SearchGoodsInterface.SearchGoodsByTypeView{

    @BindView(R.id.layoutBack) RelativeLayout layoutBack;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvGoodsList) RecyclerView lvGoodsList;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private KindredGoodsListAdapter mAdapter;
    private SearchGoodsByTypePre searchGoodsByTypePre = null;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean = null;
    private String searchType;
    private int nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kindred_goods);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarNavigationBar();
        initSwipeRefreshLayout();
        getDatas();
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
                searchGoodsByTypePre.searchGoodsByType(searchType, "12",nextPage);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishLoadmore();
                    }
                }, 1800);
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

    private void getDatas() {
        Bundle bundle = this.getIntent().getExtras();
        searchType = bundle.getString("type");
        searchGoodsByTypePre = new SearchGoodsByTypePre(this);
        searchGoodsByTypePre.searchGoodsByType(searchType, "12",1);
    }

    private void initStatusBarNavigationBar() {
        int statusHeight = ScreenUtils.getStatusHeight(KindredGoods.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        lvGoodsList.setLayoutManager(mgr);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:

                break;
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }


    @Override
    public void showSearchInfosByType(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        }else{
            nextPage = resultBean.getSuccess().getNextPage();
            if (resultBean.getSuccess().isIsFirstPage()) {
                listBean = resultBean.getSuccess().getList();
            } else if(resultBean.getSuccess().isHasNextPage()){
                List<GoodsInfoResultBean.SuccessBean.ListBean> newlist =
                        resultBean.getSuccess().getList();
                for (int i = 0; i < newlist.size(); i++) {
                        listBean.add(newlist.get(i));
                }
            }else{
                return;
            }
            if (listBean.size() == 0) {
                ToastUtils.showConfusing(getApplicationContext(), "暂无同类推荐");
            } else {
                if (mAdapter == null) {
                    mAdapter = new KindredGoodsListAdapter(KindredGoods.this, listBean);
                    lvGoodsList.setAdapter(mAdapter);
                }else{
                    swipeRefreshLayout.finishLoadmore();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(KindredGoods.this);
    }
}
