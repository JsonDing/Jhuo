package com.yunma.jhuo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.GetSelfGoodsAdapter;
import com.yunma.bean.AdInfoResultBean;
import com.yunma.bean.NoticeBean;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.bean.SelfGoodsResultBean;
import com.yunma.jhuo.activity.ContactUsActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.SearchGoodsActivity;
import com.yunma.jhuo.activity.homepage.BrowerRecordActivity;
import com.yunma.jhuo.activity.homepage.GoodsDetialsActivity;
import com.yunma.jhuo.m.GetNoticeInterFace.NoticeView;
import com.yunma.jhuo.m.HomepageModelInterface;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetBannersView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetBeforeGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetSelfGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetTodayGoodsView;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetTomorrowGoodsView;
import com.yunma.jhuo.p.GetBeforeGoodsPre;
import com.yunma.jhuo.p.GetTodayGoodsPre;
import com.yunma.jhuo.p.GetTomorrowGoodsPre;
import com.yunma.jhuo.p.HomepageModelPre;
import com.yunma.jhuo.p.NoticePre;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.jhuo.p.TopBannerPre;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.CustomProgressDialog;
import com.yunma.widget.SpaceItemDecoration;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class HomepageFragment extends Fragment implements OnBuyClick, GetSelfGoodsView,
        GetBannersView,NoticeView, GetTodayGoodsView, GetTomorrowGoodsView,
        GetBeforeGoodsView, HomepageModelInterface.ShowModelDataView {
    private static final int ONE_DAY = 24*60*60*1000;
    @BindView(R.id.layouStatusBar) FrameLayout layouStatusBar;
    @BindView(R.id.layoutSearch) FrameLayout layoutSearch;
    @BindView(R.id.btnMessage) RelativeLayout btnMessage;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.fab) com.melnykov.fab.FloatingActionButton fab;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.layoutRemind) View layoutRemind;
    @BindView(R.id.imgsDot) ImageView imgsDot;
    @BindView(R.id.imgGoods) ImageView imgGoods;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.fabBrowerRecord) FloatingActionButton fabBrowerRecord;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private List<SelfGoodsListBean> goodsListBeen = null;
    private SelfGoodsPre mPresenter;
    private GetSelfGoodsAdapter mAdapter;
    private CustomProgressDialog dialog = null;
    private GridLayoutManager gridLayoutManager;
    private int nextPage = 2;
    private long startDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepager_fragment, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        initTopPicture();
        initSwipeRefreshLayout();
        initBanner();
        return view;
    }

    private void initTopPicture() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String today = DateTimeUtils.getTime(System.currentTimeMillis(),formatter);
        String tomorrow = DateTimeUtils.getTime(
                System.currentTimeMillis() + ONE_DAY,formatter);
        String yesterday = DateTimeUtils.getTime(
                System.currentTimeMillis() - ONE_DAY,formatter);
        GetTodayGoodsPre todayGoodsPre = new GetTodayGoodsPre(this);
        todayGoodsPre.getGoods(getActivity(),"16",1,today);
        GetTomorrowGoodsPre tomorrowGoodsPre = new GetTomorrowGoodsPre(this);
        tomorrowGoodsPre.getGoods(getActivity(),"16",1,tomorrow);
        GetBeforeGoodsPre beforeGoodsPre = new GetBeforeGoodsPre(this);
        beforeGoodsPre.getGoods(getActivity(),"16",1,yesterday);
    }

    /**
     * 获取首页banner
     */
    private void initBanner() {
        TopBannerPre topBannerPre = new TopBannerPre(HomepageFragment.this);
        topBannerPre.getTopBanner(getActivity(),"v2");

        HomepageModelPre homepageModelPre = new HomepageModelPre(this);
        homepageModelPre.getModelData();

        NoticePre noticePre = new NoticePre(HomepageFragment.this);
        noticePre.getNotices(getActivity());
    }

    private void initRecyclerView() {
        progressShow();
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(getActivity()), 0, 0);
        recyclerview.setHasFixedSize(true);
        fab.attachToRecyclerView(recyclerview);
        fab.setShadow(true);
        fab.hide();
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isHeaderView(position) ||mAdapter.isBottomView(position))?
                        gridLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setItemAnimator(new SlideInUpAnimator());
        recyclerview.getItemAnimator().setAddDuration(500);
        recyclerview.addItemDecoration(new SpaceItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL,
                    DensityUtils.dp2px(getActivity(), 5), Color.parseColor("#F2F2F2")));
        mAdapter = new GetSelfGoodsAdapter(getActivity(),HomepageFragment.this);
        recyclerview.setAdapter(mAdapter);
        mPresenter = new SelfGoodsPre(HomepageFragment.this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerview.smoothScrollToPosition(0);

            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(-1)){
                    fab.hide();
                }
            }
        });
    }

    @Override
    public void onBuyClickListener(int position,SelfGoodsListBean mBean) {
        // TODO: 2017-09-07
    }

    @Override
    public void onLookGoodDetial(int goodId, int itemId, SelfGoodsListBean mBean) {
        Intent intent = new Intent(getActivity(), GoodsDetialsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd","no");
        bundle.putInt("goodId", goodId);
        bundle.putSerializable("goodsDetials", mBean);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @OnClick({R.id.btnMessage,R.id.layoutSearch,R.id.imgClose,R.id.fabBrowerRecord})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMessage:
                Intent intent = new Intent(MainActivity.mainActivity, ContactUsActivity.class);
                MainActivity.mainActivity.startActivity(intent);
                break;
            case R.id.layoutSearch:
                Intent intent0 = new Intent(MainActivity.mainActivity, SearchGoodsActivity.class);
                MainActivity.mainActivity.startActivity(intent0);
                MainActivity.mainActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.imgClose:
                layoutRemind.setVisibility(View.GONE);
                break;
            case R.id.fabBrowerRecord:
                Intent intent1 = new Intent(MainActivity.mainActivity, BrowerRecordActivity.class);
                MainActivity.mainActivity.startActivity(intent1);
                MainActivity.mainActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }
    }

    /**
     * 显示首页推荐32款商品
     */
    @Override
    public void showSpecialOfferGoods(final SelfGoodsResultBean resultBean, String msg) {
        if(resultBean == null){
            progressDimiss();
            ToastUtils.showError(getActivity(),msg);
        }else{
            if(resultBean.getSuccess().getList()!=null){
                nextPage = resultBean.getSuccess().getNextPage();
                mAdapter.setTotalGoodsNums(resultBean.getSuccess().getTotal());
                if(resultBean.getSuccess().isHasNextPage()){
                    swipeRefreshLayout.setLoadmoreEnable(true);
                }else{
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                if (resultBean.getSuccess().getPageNum()==1) {
                    long endDate = DateTimeUtils.getCurrentTimeInLong();
                    long cost = endDate - startDate;
                    if(cost>2000){
                        progressDimiss();
                        firstLoadView(resultBean);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDimiss();
                                firstLoadView(resultBean);
                            }
                        }, 2000 - cost);
                    }
                } else {
                    progressDimiss();
                    goodsListBeen.addAll(resultBean.getSuccess().getList());
                    swipeRefreshLayout.finishLoadmore();
                    mAdapter.setGoodsListBeen(goodsListBeen);

                }
            }else{
                progressDimiss();
            }
        }
    }

    private void firstLoadView(SelfGoodsResultBean resultBean) {
        if(goodsListBeen!=null&&goodsListBeen.size()!=0){
            goodsListBeen.clear();
        }
        goodsListBeen = resultBean.getSuccess().getList();
        if(goodsListBeen.size()==0||resultBean.getSuccess().getSize()<12){
            swipeRefreshLayout.setRefreshEnable(true);
            swipeRefreshLayout.setLoadmoreEnable(false);
        }else{
            swipeRefreshLayout.setRefreshEnable(true);
            swipeRefreshLayout.setLoadmoreEnable(true);
        }
        mAdapter.setGoodsListBeen(goodsListBeen);
    }

    /**
     * 显示首页顶部 banner页
     */
    @Override
    public void showTopBannerInfo(AdInfoResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getActivity(),msg);
        }else{
            mAdapter.setTopBanner(resultBean.getSuccess());
        }
        mPresenter.getSpecialOfferGoods(getActivity(),"recommend","16",1,null,null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.head);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        final View view = inflater.inflate(R.layout.refresh_head_view, null);
        final View view1 = inflater.inflate(R.layout.refresh_view, null);
        final ImageView imgSun = view.findViewById(R.id.imgSun);
        final TextView textView2 = view1.findViewById(R.id.title);
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(false);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                initTopPicture();
                mAdapter.clearData();
                mPresenter.getSpecialOfferGoods(getActivity(),"recommend","16",1,null,null);
                HomepageModelPre homepageModelPre = new HomepageModelPre(HomepageFragment.this);
                homepageModelPre.getModelData();
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
                mPresenter.getSpecialOfferGoods(getActivity(),"recommend","16",nextPage,null,null);
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
     * 贱货动态
     */
    @Override
    public void showNoticeInfo(NoticeBean noticeBean, String msg) {
        if(noticeBean == null){
            ToastUtils.showError(getActivity(),msg);
        }else{
            if(noticeBean.getSuccess().getList().size()!=0){
                List<String> adTextList = new ArrayList<>();
                for(int i=0;i<noticeBean.getSuccess().getList().size();i++){
                    adTextList.add(noticeBean.getSuccess().getList().get(i).getTitle());
                }
                mAdapter.setAdText(adTextList);
            }
        }
    }

    @Override
    public void showTodayGoods(SelfGoodsResultBean resultBean, String msg) {
        if(resultBean != null){
            if(resultBean.getSuccess().getList().size()>0){
                int position = new Random().nextInt(resultBean.getSuccess().getList().size())%
                        (resultBean.getSuccess().getList().size()+1);
                int repoid = resultBean.getSuccess().getList().get(position).getRepoid();
                String url;
                if(repoid ==1){
                    url = ConUtils.SElF_GOODS_IMAGE_URL;
                }else{
                    url = ConUtils.GOODS_IMAGE_URL;
                }
                mAdapter.setTodayPic(url +
                        resultBean.getSuccess().getList().get(position).getPic().split(",")[0] + "/min");
            }
        }
    }

    @Override
    public void showTomorrowGoods(SelfGoodsResultBean resultBean, String msg) {
        if(resultBean != null){
            if(resultBean.getSuccess().getList().size()>0){
                int position = new Random().nextInt(resultBean.getSuccess().getList().size())%
                        (resultBean.getSuccess().getList().size()+1);
                int repoid = resultBean.getSuccess().getList().get(position).getRepoid();
                String url;
                if(repoid ==1){
                    url = ConUtils.SElF_GOODS_IMAGE_URL;
                }else{
                    url = ConUtils.GOODS_IMAGE_URL;
                }
                mAdapter.setTomorrowPic(url +
                        resultBean.getSuccess().getList().get(position).getPic().split(",")[0] + "/min");
            }
        }
    }

    @Override
    public void showBeforeGoods(SelfGoodsResultBean resultBean, String msg) {
        if(resultBean != null){
            if(resultBean.getSuccess().getList().size()>0){
                int position = new Random().nextInt(resultBean.getSuccess().getList().size())%
                        (resultBean.getSuccess().getList().size()+1);
                String url;
                int repoid = resultBean.getSuccess().getList().get(position).getRepoid();
                if(repoid ==1){
                    url = ConUtils.SElF_GOODS_IMAGE_URL;
                }else{
                    url = ConUtils.GOODS_IMAGE_URL;
                }
                mAdapter.setBeforePic(url +
                        resultBean.getSuccess().getList().get(position).getPic().split(",")[0] + "/min");
            }
        }
    }

    @Override
    public void showModelData(AdInfoResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getActivity(),msg);
        }else{
            mAdapter.setModelData(resultBean.getSuccess());
        }
    }

    public void progressShow() {
        startDate = DateTimeUtils.getCurrentTimeInLong();
        if (dialog == null) {
            dialog = new CustomProgressDialog(getActivity(),"加载中", R.drawable.pb_loading_logo_2);
        }
        dialog.show();
    }

    public void progressDimiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            if(mAdapter!=null){
                mAdapter.getView().stopAutoPlay();
            }
        }else{
            if(mAdapter!=null) {
                mAdapter.getView().startAutoPlay();
                mAdapter.animStart();
            }
        }
    }

}
