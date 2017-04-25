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
import com.yunma.adapter.OffTheShelfAdapter;
import com.yunma.bean.*;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.SelfGoodsInterFace;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class OffTheShelfActivity extends MyCompatActivity implements
        SelfGoodsInterFace.GetSelfGoodsView, OffTheShelfAdapter.OnItemClick{
    private static final int  REQUEST_CODE = 1;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvBookmarks) RecyclerView lvBookmarks;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private Context mContext;
    private SelfGoodsPre selfGoodsPre = null;
    private OffTheShelfAdapter mAdapter;
    private int reShelfPosition = -1;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean = null;
    private int nextPage = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_the_shelf);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        initSwipeRefreshLayout();
        getDatas();
    }

    private void getDatas() {
        selfGoodsPre = new SelfGoodsPre(this);
        selfGoodsPre.getSpecialOfferGoods(mContext,"before","8",1);
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(OffTheShelfActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        lvBookmarks.setHasFixedSize(true);
        lvBookmarks.setItemAnimator(new FadeInLeftAnimator());
        lvBookmarks.getItemAnimator().setAddDuration(500);
        lvBookmarks.getItemAnimator().setMoveDuration(500);
        lvBookmarks.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new OffTheShelfAdapter(this,OffTheShelfActivity.this);
        lvBookmarks.setAdapter(mAdapter);
    }

    @OnClick({R.id.layoutBack, R.id.tvTittle, R.id.layoutNews})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.tvTittle:

                break;
            case R.id.layoutNews:

                break;
        }
    }

    @Override
    public void onItemClick(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        //// TODO: 2017-04-11
    }

    /**
     * 上架
     * @param id
     * @param position
     */
    @Override
    public void onReShelf(int id,int position,
                          GetSelfGoodsResultBean.SuccessBean.ListBean listBean) {
        Intent intent = new Intent(this,SelectShelfSizeNumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ClickItem",position);
        bundle.putInt("ids",id);
        bundle.putSerializable("SelfGoodsDetials",listBean);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
        overridePendingTransition(0,R.anim.bottom_out);
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
        swipeRefreshLayout.setRefreshEnable(false);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                selfGoodsPre.getSpecialOfferGoods(getApplicationContext(),"before","8",1);
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
                selfGoodsPre.getSpecialOfferGoods(getApplicationContext(),"before","8",nextPage);
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
                    mAdapter.setListBean(listBean);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case RESULT_OK:
                GetSelfGoodsResultBean.SuccessBean.ListBean goodsBeen =
                        (GetSelfGoodsResultBean.SuccessBean.ListBean)
                                data.getSerializableExtra("GoodsInfos");
                int clickItem = data.getIntExtra("ClickItem",-1);
                if(clickItem!=-1){
                    listBean.remove(clickItem);
                  //  listBean.set(clickItem,goodsBeen);
                    mAdapter.notifyItemRemoved(clickItem);
                }

                break;
        }
    }
}
