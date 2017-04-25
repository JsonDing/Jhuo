package com.yunma.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.PublishManageAdapter;
import com.yunma.adapter.PublishManageAdapter.OnItemClick;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace.DelSelfGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetSelfGoodsView;
import com.yunma.jhuo.p.DelSelfGoodsPre;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


public class PublishManageActivity extends MyCompatActivity implements
        GetSelfGoodsView,DelSelfGoodsView, OnItemClick {
    private static final int  REQUEST_CODE = 1;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutDelete) LinearLayout layoutDelete;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.rcPublishManage) RecyclerView rcPublishManage;
    @BindView(R.id.tvDelete) TextView tvDelete;
    @BindView(R.id.tvMaind) TextView tvMaind;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen = null;
    private SelfGoodsPre mPresenter;
    private DelSelfGoodsPre delSelfGoodsPre;
    private PublishManageAdapter mAdapter;
    private Context mContext;
    private int nextPage =2;
    private int totalGoodsNums = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_manage);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initBar();
        initSwipeRefreshLayout();
        getDatas();

    }

    private void initBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(PublishManageActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        rcPublishManage.setHasFixedSize(true);
        rcPublishManage.setItemAnimator(new FadeInLeftAnimator());
        rcPublishManage.getItemAnimator().setAddDuration(700);
        rcPublishManage.getItemAnimator().setMoveDuration(500);
        rcPublishManage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new PublishManageAdapter(PublishManageActivity.this, PublishManageActivity.this);
        rcPublishManage.setAdapter(mAdapter);

    }

    private void getDatas() {
        mPresenter = new SelfGoodsPre(PublishManageActivity.this);
        mPresenter.getSpecialOfferGoods(getApplicationContext(),"issue","8", 1);
        delSelfGoodsPre = new DelSelfGoodsPre(PublishManageActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvMaind.setVisibility(View.GONE);
            }
        },4000);
    }

    @OnClick({R.id.layoutBack, R.id.layoutDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutDelete:
                if(EmptyUtil.isNotEmpty(mAdapter.getDeleteIds())){
                    showWarning();
                }else{
                    ToastUtils.showError(mContext,"请选择需要删除的商品");
                }
                break;
        }
    }

    private void showWarning() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setIcon(R.drawable.logo_sm)
                .setTitle("温馨提示")
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("商品一经删除，不可恢复");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delSelfGoodsPre.delPublishGoods(mContext,mAdapter.getDeleteIds());
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    @Override
    public void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                nextPage = resultBean.getSuccess().getNextPage();
                if (resultBean.getSuccess().isHasNextPage()) {
                    swipeRefreshLayout.setLoadmoreEnable(true);
                } else {
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                totalGoodsNums = resultBean.getSuccess().getTotal();
                tvDelete.setText(Html.fromHtml("待发布" + "<font color = '#f44141'>"
                        + totalGoodsNums + "</font>" + "款",null,null));
                if (resultBean.getSuccess().getPageNum() == 1) {
                    if (goodsListBeen != null && goodsListBeen.size() != 0) {
                        goodsListBeen.clear();
                    }
                    goodsListBeen = resultBean.getSuccess().getList();
                    mAdapter.setGoodsListBeen(goodsListBeen);
                } else if (resultBean.getSuccess().getPageNum() <= resultBean.getSuccess().getPages()) {
                    goodsListBeen.addAll(resultBean.getSuccess().getList());
                    swipeRefreshLayout.finishLoadmore();
                    mAdapter.setGoodsListBeen(goodsListBeen);
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, int position, GetSelfGoodsResultBean.SuccessBean.ListBean successBean) {
        Intent intent = new Intent(this,PublishGoodsDetials.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PublishGoodsDetials",successBean);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(View view, int position, GetSelfGoodsResultBean.SuccessBean.ListBean successBean) {
        MobileUtils.Vibrate(PublishManageActivity.this,70);
        mAdapter.isShowBox(true);
        tvDelete.setText(Html.fromHtml("<font color = '#f4bd39'>删除</font>",null,null));
        layoutDelete.setClickable(true);
        layoutDelete.setEnabled(true);
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final Animation operatingAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.head);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        final View view = inflater.inflate(R.layout.refresh_head_view, null);
        final View view1 = inflater.inflate(R.layout.refresh_view, null);
        final ImageView imgSun = (ImageView)view.findViewById(R.id.imgSun);
        final TextView textView2 = (TextView) view1.findViewById(R.id.title);
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(true);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSpecialOfferGoods(getApplicationContext(),"issue","8",1);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                        imgSun.clearAnimation();
                    }
                }, 1500);
            }

            @Override
            public void onLoading() {
                mPresenter.getSpecialOfferGoods(getApplicationContext(),"issue","8",nextPage);
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

    @Override
    public void showDelInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
            goodsListBeen.clear();
            mPresenter.getSpecialOfferGoods(getApplicationContext(),"issue","8", 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mPresenter.getSpecialOfferGoods(getApplicationContext(),"issue","8", 1);
                }
                break;
        }
    }
}
