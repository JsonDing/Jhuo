package com.yunma.jhuo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.HotGoodsListAdapter;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetGoodsInterface;
import com.yunma.jhuo.p.GetGoodsHotPre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;

public class HotGoods extends MyCompatActivity implements
        GetGoodsInterface.GetGoodsHotView, HotGoodsListAdapter.LayoutOnClick {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private int nextPage;
    private HotGoodsListAdapter mAdapter;
    private GetGoodsHotPre hotPre = null;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_goods);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        initSwipeRefreshLayout();
        getDatas();
    }

    private void initStatusBarAndNavigationBar() {
        int statusHeight = ScreenUtils.getStatusHeight(HotGoods.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(staggeredGridLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

    }

    private void getDatas() {
        hotPre = new  GetGoodsHotPre(this);
        hotPre.getHotGoods("10",1);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:
                QQAuth mqqAuth = QQAuth.createInstance("1106058796",getContext());
                WPA mWPA = new WPA(this, mqqAuth.getQQToken());
                String ESQ = "2252162352";  //客服QQ号
                int ret = mWPA.startWPAConversation(HotGoods.this,ESQ, null);
                if (ret != 0) {
                    Toast.makeText(getApplicationContext(),
                            "抱歉，联系客服出现了错误~. error:" + ret,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showHotGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        }else{
            nextPage = resultBean.getSuccess().getNextPage();
            if (nextPage == 2) {
                listBean = resultBean.getSuccess().getList();
            } else {
               /* List<GoodsInfoResultBean.SuccessBean.ListBean> newlist =
                        resultBean.getSuccess().getList();
                for (int i = 0; i < newlist.size(); i++) {
                    listBean.add(newlist.get(i));
                }*/
                listBean.addAll(resultBean.getSuccess().getList());
                mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
            }
            if (listBean.size() == 0) {
                ToastUtils.showConfusing(getApplicationContext(), "暂无热销推荐");
            } else {
                if (mAdapter == null) {
                    mAdapter = new HotGoodsListAdapter(HotGoods.this,listBean,HotGoods.this);
                    recyclerview.setAdapter(mAdapter);
                }
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
                hotPre.getHotGoods("10",nextPage);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onLayoutClick(GoodsInfoResultBean.SuccessBean.ListBean bean) {
        Intent intent = new Intent(HotGoods.this, EntrepotGoodsDetial.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd","yes");
        bundle.putSerializable("goodsBean",bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(HotGoods.this);
    }
}
