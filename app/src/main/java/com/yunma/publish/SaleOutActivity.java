package com.yunma.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.SaleOutAdapter;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.jhuo.p.ShelveSelfGoodsPre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


public class SaleOutActivity extends MyCompatActivity implements
        SaleOutAdapter.OnItemClick, SelfGoodsInterFace.GetSelfGoodsView, SelfGoodsInterFace.ShelveSelfGoodsView {
    private static final int  REQUEST_CODE = 1;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvBookmarks) RecyclerView lvBookmarks;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private Context mContext;
    private SelfGoodsPre selfGoodsPre = null;
    private ShelveSelfGoodsPre shelveGoodsPre = null;
    private SaleOutAdapter mAdapter;
    private int soldOutPosition = -1;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean = null;
    private int nextPage =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_out);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        initSwipeRefreshLayout();
        getDatas();
    }

    private void getDatas() {
        selfGoodsPre = new SelfGoodsPre(this);
        selfGoodsPre.getSpecialOfferGoods(mContext,"nogoods","8",1);
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(SaleOutActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        lvBookmarks.setHasFixedSize(true);
        lvBookmarks.setItemAnimator(new FadeInLeftAnimator());
        lvBookmarks.getItemAnimator().setAddDuration(700);
        lvBookmarks.getItemAnimator().setMoveDuration(500);
        lvBookmarks.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SaleOutAdapter(this,SaleOutActivity.this);
        lvBookmarks.setAdapter(mAdapter);
        shelveGoodsPre = new ShelveSelfGoodsPre(SaleOutActivity.this);
    }

    @OnClick({R.id.layoutBack, R.id.layoutNews})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutNews:

                break;
        }
    }

    @Override
    public void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mContext,msg);
        }else{
            if (resultBean.getSuccess().getList().size() != 0) {
                nextPage = resultBean.getSuccess().getNextPage();
                if (resultBean.getSuccess().isHasNextPage()) {
                    swipeRefreshLayout.setLoadmoreEnable(true);
                } else {
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                if (resultBean.getSuccess().getPageNum() == 1) {
                    if (listBean != null && listBean.size() != 0) {
                        listBean.clear();
                    }
                    listBean = resultBean.getSuccess().getList();
                    mAdapter.setListBean(listBean);
                } else if (resultBean.getSuccess().getPageNum() <= resultBean.getSuccess().getPages()) {
                    listBean.addAll(resultBean.getSuccess().getList());
                    swipeRefreshLayout.finishLoadmore();
                    mAdapter.addListBean(listBean);
                }
            }
        }

    }

    private void firstLoadView(GetSelfGoodsResultBean resultBean) {

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
                selfGoodsPre.getSpecialOfferGoods(getApplicationContext(),"nogoods","8",1);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                        imgSun.clearAnimation();
                    }
                }, 1200);
            }

            @Override
            public void onLoading() {
                selfGoodsPre.getSpecialOfferGoods(getApplicationContext(),"nogoods","8",nextPage);
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

    /**
     * 修改库存
     * @param position
     * @param listBean
     */
    @Override
    public void onItemClick(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        Intent intent = new Intent(this,AddRepertoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ClickItem",position);
        bundle.putSerializable("SelfGoodsDetials",listBean);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
        overridePendingTransition(0,R.anim.bottom_out);
    }


    /**
     * 下架
     * @param id
     * @param position
     */
    @Override
    public void onSoldOut(int id, int position) {
        soldOutPosition = position;
        IssueBean issueBean = new IssueBean();
        issueBean.setToken(SPUtils.getToken(this));
        issueBean.setIssue("0");
        issueBean.setId(String.valueOf(id));
        LogUtils.log("---ShelveID: " + id);
        showWarning(issueBean);
    }

    private void showWarning(final IssueBean issueBean) {
        int dialogWith = ScreenUtils.getScreenWidth(this) -
                DensityUtils.dp2px(this, 42);
        new MDDialog.Builder(SaleOutActivity.this)
                .setIcon(R.drawable.logo_sm)
                .setTitle("温馨提示")
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("商品下架后，可在已下架商品栏目查看，并且可以重新发布上架");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shelveGoodsPre.shelveGoods(getApplicationContext(),issueBean);
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(getApplicationContext(), dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_OK:
                int clickItem = data.getIntExtra("ClickItem",-1);
                if(clickItem!=-1){
                    listBean.remove(clickItem);
                }
                mAdapter.notifyItemRemoved(clickItem);
                break;
        }
    }

    @Override
    public void shelveSelfGoods(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getApplicationContext(),msg);
        }else{
            ToastUtils.showSuccess(getApplicationContext(),msg);
            mAdapter.remove(soldOutPosition);
        }
    }
}
