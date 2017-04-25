package com.yunma.jhuo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.yunma.adapter.NewProductListAdapter;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetGoodsInterface;
import com.yunma.jhuo.p.GetGoodsNewPre;
import com.yunma.utils.*;
import com.yunma.widget.SpaceItemDecoration;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;

public class NewProduct extends MyCompatActivity implements GetGoodsInterface.GetGoodsNewView ,
        NewProductListAdapter.LayoutOnClick{

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layout) View layout;
    private GridLayoutManager gridLayoutManager;
    private int nextPage;
    private GetGoodsNewPre newPre = null;
    private NewProductListAdapter mAdapter;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        initSwipeRefreshLayout();
        getDatas();

    }

    private void getDatas() {
        newPre = new GetGoodsNewPre(this);
        newPre.getNewGoods("6",1);
    }

    private void initStatusBarAndNavigationBar() {
        int statusHeight = ScreenUtils.getStatusHeight(NewProduct.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        recyclerview.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new SpaceItemDecoration(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dp2px(this, 5), Color.parseColor("#F2F2F2")));
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
                newPre.getNewGoods("6",nextPage);
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
                int ret = mWPA.startWPAConversation(NewProduct.this,ESQ,null);
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
    public void showNewGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
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
                ToastUtils.showConfusing(getApplicationContext(), "暂无新品推荐");
            } else {
                if (mAdapter == null) {
                    mAdapter = new NewProductListAdapter(NewProduct.this, listBean ,NewProduct.this);
                    recyclerview.setAdapter(mAdapter);
                }
            }
        }
    }

    @Override
    public void onLayoutClick(GoodsInfoResultBean.SuccessBean.ListBean bean) {
        Intent intent = new Intent(NewProduct.this, EntrepotGoodsDetial.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd","yes");
        bundle.putSerializable("goodsBean",bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAddBasketClick(GoodsInfoResultBean.SuccessBean.ListBean bean) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(NewProduct.this);
    }
}
