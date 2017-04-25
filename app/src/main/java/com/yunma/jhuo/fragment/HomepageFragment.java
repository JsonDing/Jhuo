package com.yunma.jhuo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.*;
import com.melnykov.fab.FloatingActionButton;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.tencent.connect.auth.QQAuth;
import com.tencent.open.wpa.WPA;
import com.yunma.R;
import com.yunma.adapter.GetSelfGoodsAdapter;
import com.yunma.bean.*;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.emchat.DemoHelper;
import com.yunma.emchat.ui.EMMainActivity;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.SearchGoodsByCode;
import com.yunma.jhuo.activity.homepage.SelfGoodsDetials;
import com.yunma.jhuo.activity.homepage.ShoppingCartBySelfGoods;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.jhuo.m.SelfGoodsInterFace.*;
import com.yunma.jhuo.p.*;
import com.yunma.utils.*;
import com.yunma.widget.CustomProgressDialog;
import com.yunma.widget.SpaceItemDecoration;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.Arrays;
import java.util.List;

import butterknife.*;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class HomepageFragment extends Fragment implements OnBuyClick,
        GetSelfGoodsView, GetIssueView, GetBannerView,GetAdsView {
    private final static int MSG_REFRESH = 2;
    @BindView(R.id.layouStatusBar) FrameLayout layouStatusBar;
    @BindView(R.id.layoutSearch) FrameLayout layoutSearch;
    @BindView(R.id.btnMessage) RelativeLayout btnMessage;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.layoutRemind) View layoutRemind;
    @BindView(R.id.imgsDot) ImageView imgsDot;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private List<ListBean> goodsListBeen = null;
    private AdInfoResultBean adInfoResultBean = null;
    private SelfGoodsPre mPresenter;
    private SelfIssueGoodsPre issueGoodsPre;
    private GetSelfGoodsAdapter mAdapter;
    private CustomProgressDialog dialog = null;
    private GridLayoutManager gridLayoutManager;
    private int nextPage = 2;
    private List<String> adList;
    private List<String> bannerList;
    private long startDate;
    private List<ListBean> tomorrowListBean = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepager_fragment, container, false);
        ButterKnife.bind(this, view);
        initBanner();
        initRecyclerView();
        initSwipeRefreshLayout();
        return view;
    }

    /**
     * 获取首页banner
     */
    private void initBanner() {
        BannerPre bannerPre = new BannerPre(HomepageFragment.this);
        bannerPre.getNewsBanner(getActivity());
    }
    /**
     * 获取产品页插入的广告
     */
    private void initAds() {
        AdsPre adsPre = new AdsPre(HomepageFragment.this);
        adsPre.getAdInfo(getActivity());
        issueGoodsPre.getSpecialOfferGoods(getActivity(),"issue","8", 1);
    }

    /**
     * 上架提醒
     */
    private void showRemind() {
        if(SPUtils.isSaleRemind(MainActivity.mainContext)){
            layoutRemind.setVisibility(View.VISIBLE);
            // TODO: 2017-04-20  
        }else{
            layoutRemind.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        progressShow();
        int statusHeight = ScreenUtils.getStatusHeight(getActivity());
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        recyclerview.setHasFixedSize(true);
        fab.attachToRecyclerView(recyclerview);
        fab.setShadow(true);
        fab.hide();
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isHeaderView(position)||mAdapter.isAdView(position)
                        ||mAdapter.isBottomView(position)||mAdapter.isTomorrowUpdate(position)) ?
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
        issueGoodsPre = new SelfIssueGoodsPre(HomepageFragment.this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerview.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onBuyClickListener(int position,ListBean mBean) {
        if(MainActivity.mainContext.isLogin()){
            Intent intent = new Intent(getActivity(),ShoppingCartBySelfGoods.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selfGoods",mBean);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }else {
            ToastUtils.showWarning(getActivity(),"请登陆后购买");
        }

    }

    @Override
    public void onLookGoodDetial(int goodId, int itemId, ListBean mBean) {
        Intent intent = new Intent(getActivity(), SelfGoodsDetials.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd","no");
        bundle.putInt("goodId", goodId);
        bundle.putSerializable("goodsDetials", mBean);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @OnClick({R.id.btnMessage,R.id.layoutSearch,R.id.imgClose})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMessage:
                if(MainActivity.mainContext.isLogin()){
                    Intent intent = new Intent(MainActivity.mainContext, EMMainActivity.class);
                    MainActivity.mainContext.startActivity(intent);
                }else{
                    QQAuth mqqAuth = QQAuth.createInstance("1106058796",MainActivity.mainContext);
                    WPA mWPA = new WPA(MainActivity.mainContext, mqqAuth.getQQToken());
                    String ESQ = "2252162352";  //客服QQ号
                    int ret = mWPA.startWPAConversation(getActivity(),ESQ,null);
                    if (ret != 0) {
                        Toast.makeText(MainActivity.mainContext,
                                "抱歉，联系客服出现了错误~. error:" + ret,
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.layoutSearch:
                Intent intent = new Intent(MainActivity.mainContext, SearchGoodsByCode.class);
                MainActivity.mainContext.startActivity(intent);
                MainActivity.mainContext.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.imgClose:
                layoutRemind.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 显示首页推荐32款商品
     * @param resultBean
     * @param msg
     */
    @Override
    public void showSpecialOfferGoods(final GetSelfGoodsResultBean resultBean, String msg) {
        if(resultBean == null){
            progressDimiss();
            ToastUtils.showError(getActivity(),msg);
        }else{
            if(resultBean.getSuccess().getList()!=null){
                nextPage = resultBean.getSuccess().getNextPage();
                if(resultBean.getSuccess().isHasNextPage()){
                    swipeRefreshLayout.setLoadmoreEnable(true);
                }else{
                    mAdapter.setEnd(true);
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                if (resultBean.getSuccess().getPageNum()==1) {
                    long endDate = DateTimeUtils.getCurrentTimeInLong();
                    long cost = endDate - startDate;
                    if(cost>2000){
                        progressDimiss();
                        firstLoadView(resultBean);
                    }else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDimiss();
                                firstLoadView(resultBean);
                            }
                        }, 2000 - cost);
                    }
                } else if(resultBean.getSuccess().getPageNum()<=resultBean.getSuccess().getPages()){
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

    private void firstLoadView(GetSelfGoodsResultBean resultBean) {
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
        mAdapter.setDatas(bannerList,adList,adInfoResultBean,tomorrowListBean,goodsListBeen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showRemind();
            }
        },1500);
    }

    /**
     * 显示首页tittle banner页
     * @param resultBean
     * @param msg
     */
    @Override
    public void showNewsBanner(BannerResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(getActivity(),msg);
            initAds();
        }else{
            initAds();
            for(int i=0;i<resultBean.getSuccess().size();i++){
                if(resultBean.getSuccess().get(i).getType().equals("adword")){
                    adList = Arrays.asList(resultBean.getSuccess()
                            .get(i).getValue().split(","));
                }else if(resultBean.getSuccess().get(i).getType().equals("banner")){
                    bannerList = Arrays.asList(resultBean.getSuccess()
                            .get(i).getValue().split(","));
                }
            }
        }
    }

    /**
     * 显示货品list插播广告页
     * @param resultBean
     * @param msg
     */
    @Override
    public void showAdsInfo(AdInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            mPresenter.getSpecialOfferGoods(getActivity(),"recommend","12",1);
        }else{
            mPresenter.getSpecialOfferGoods(getActivity(),"recommend","12",1);
            adInfoResultBean = resultBean;
        }
    }

    /**
     * 显示明日带发布商品
     * @param resultBean
     * @param msg
     */
    @Override
    public void showIssueGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null){
            ToastUtils.showError(getActivity(),msg);
        }else{
            if(resultBean.getSuccess().getList().size()!=0){
                tomorrowListBean = resultBean.getSuccess().getList();
            }
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
        final ImageView imgSun = (ImageView)view.findViewById(R.id.imgSun);
        final TextView textView2 = (TextView) view1.findViewById(R.id.title);
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(false);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getSpecialOfferGoods(getActivity(),"recommend","12",1);
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
                mPresenter.getSpecialOfferGoods(getActivity(),"recommend","12",nextPage);
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

    public void refresh() {
        if(!handler.hasMessages(MSG_REFRESH)){
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                 //   onConnectionDisconnected();
                    break;
                case 1:
                 //   onConnectionConnected();
                    break;

                case MSG_REFRESH:
                {
                    imgsDot.setVisibility(View.VISIBLE);
                    break;
                }
                default:
                    break;
            }
        }
    };

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
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnreadLabel();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    public void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onStop();
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private void refreshUIWithMessage() {
        MainActivity.mainContext.runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();

            }
        });
    }

    private void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            imgsDot.setVisibility(View.VISIBLE);
        } else {
            imgsDot.setVisibility(View.INVISIBLE);
        }
    }

    private int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMessageCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

}
