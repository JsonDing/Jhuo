package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.SpecialPriceAdapter;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.bean.SelfGoodsResultBean;
import com.yunma.jhuo.activity.NewBasketActivity;
import com.yunma.jhuo.general.DialogStyleLoginActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetSelfGoodsView;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.SpaceItemDecoration;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SpecialPriceActivity extends MyCompatActivity
        implements OnBuyClick, GetSelfGoodsView{

    @BindView(R.id.layoutBack) View layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.rvGoodsList) RecyclerView rvGoodsList;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgsRight) ImageView imgsRight;
    @BindView(R.id.layoutRight) LinearLayout layoutRight;
    @BindView(R.id.tvSortPrice) TextView tvSortPrice;
    @BindView(R.id.imgSortPrice) ImageView imgSortPrice;
    @BindView(R.id.tvSortStocks) TextView tvSortStocks;
    @BindView(R.id.imgSortStock) ImageView imgSortStock;
    @BindView(R.id.tvSortSales) TextView tvSortSales;
    private List<SelfGoodsListBean> goodsListBeen = null;
    private SelfGoodsPre mPresenter = null;
    private SpecialPriceAdapter mAdapter = null;
    private int nextPage;
    private Context mContext;
    private String sortBy = null;
    private String desc = null; //desc 1 是降序， desc 不填 或 != 1 是升序
    private boolean isRefreshItem  = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_price);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        initSwipeRefreshLayout();
        getDatabase();
    }

    private void getDatabase() {
        mPresenter = new SelfGoodsPre(SpecialPriceActivity.this);
        mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
    }

    private void initView() {
        mContext = this;
        tvTittle.setText("好货推荐");
        GlideUtils.glidLocalDrawable(mContext,imgsRight,R.drawable.shopping_cart);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(mContext), 0, 0);
        rvGoodsList.setHasFixedSize(true);
        rvGoodsList.addItemDecoration(new SpaceItemDecoration(SpecialPriceActivity.this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dp2px(SpecialPriceActivity.this, 0), Color.parseColor("#F2F2F2")));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(SpecialPriceActivity.this);
        rvGoodsList.setLayoutManager(mLayoutManager);
        mAdapter = new SpecialPriceAdapter(SpecialPriceActivity.this, this);
        rvGoodsList.setAdapter(mAdapter);
    }

    @OnClick({R.id.layoutBack,R.id.layoutRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutRight:
                Intent intent = new Intent(this, NewBasketActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
        }
    }


    /**
     * 立即购买
     * @param position
     * @param mListBean
     */
    @Override
    public void onBuyClickListener(int position, SelfGoodsListBean mListBean) {
        if(isLogin()){
            Intent intent = new Intent(this, ChooseSizeNumsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selfGoods", mListBean);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
        }else{
            Intent intent = new Intent(this, DialogStyleLoginActivity.class);
            startActivity(intent);
            overridePendingTransition(0,R.anim.fade_out);
        }
    }

    /**
     * 查看详情
     * @param goodId
     * @param itemId
     * @param mListBean
     */
    @Override
    public void onLookGoodDetial(int goodId, int itemId, SelfGoodsListBean mListBean) {
        Intent intent = new Intent(SpecialPriceActivity.this, GoodsDetialsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("goodId", goodId);
        bundle.putString("isToEnd", "no");
        bundle.putSerializable("goodsDetials", mListBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 显示商品详情
     * @param resultBean
     * @param msg
     */
    @Override
    public void showSpecialOfferGoods(SelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext().getApplicationContext(), msg);
        } else {
            if (resultBean.getSuccess().isHasNextPage()) {
                swipeRefreshLayout.setLoadmoreEnable(true);
                nextPage = resultBean.getSuccess().getNextPage();
            } else {
                swipeRefreshLayout.setLoadmoreEnable(false);
            }
            if(isRefreshItem){
                goodsListBeen = resultBean.getSuccess().getList();
                mAdapter.refreshDatas(goodsListBeen);
            }else{
                goodsListBeen.addAll(resultBean.getSuccess().getList());
                swipeRefreshLayout.finishLoadmore();
                mAdapter.addListBeen(goodsListBeen);
            }
        }
    }

    /**
     * 初始化加载控件
     */
    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.head);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        final View view = inflater.inflate(R.layout.refresh_head_view, null);
        final View view1 = inflater.inflate(R.layout.refresh_view, null);
        final ImageView imgSun = view.findViewById(R.id.imgSun);
        final TextView textView2 = view1.findViewById(R.id.title);
        swipeRefreshLayout.setRefreshEnable(false);
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
                isRefreshItem = false;
                mPresenter.getSpecialOfferGoods(getApplicationContext(), "sale", "16", nextPage,sortBy,desc);
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


    public void fabBrowerRecord(View view) {
        Intent intent = new Intent(SpecialPriceActivity.this,BrowerRecordActivity.class);
        startActivity(intent);
    }

    /**
     * 按价格排序
     * @param view
     */
    public void sortByPrice(View view) {
        isRefreshItem = true;
        if(sortBy == null ){
            sortBy = "price";
            desc = "1";
            tvSortPrice.setTextColor(ContextCompat.getColor(mContext,R.color.b3));
            GlideUtils.glidLocalDrawable(this,imgSortPrice,R.drawable.sort_down);
            mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
        }else{
            if(sortBy.equals("price")){
                if(desc == null){
                    desc = "1";
                    GlideUtils.glidLocalDrawable(this,imgSortPrice,R.drawable.sort_down);
                }else if(desc.equals("1")){ // 1 降序 《---》 升序
                    desc = "-1";
                    GlideUtils.glidLocalDrawable(this,imgSortPrice,R.drawable.sort_up);
                }else{
                    desc = "1";
                    GlideUtils.glidLocalDrawable(this,imgSortPrice,R.drawable.sort_down);
                }
                mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
            }else{
                clearSelectStatus();
                sortBy = "price";
                desc = "1";
                tvSortPrice.setTextColor(ContextCompat.getColor(mContext,R.color.b3));
                GlideUtils.glidLocalDrawable(this,imgSortPrice,R.drawable.sort_down);
                mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
            }
        }
        rvGoodsList.smoothScrollToPosition(0);
    }

    /**
     * 按库存排序
     * @param view
     */
    public void sortByStocks(View view) {
        isRefreshItem = true;
        if(sortBy == null ){
            sortBy = "stock";
            desc = "1";
            tvSortStocks.setTextColor(ContextCompat.getColor(mContext,R.color.b3));
            GlideUtils.glidLocalDrawable(this,imgSortStock,R.drawable.sort_down);
            mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
        }else{
            if(sortBy.equals("stock")){
                if(desc == null){
                    desc = "1";
                    GlideUtils.glidLocalDrawable(this,imgSortStock,R.drawable.sort_down);
                }else if(desc.equals("1")){ // 1 降序 《---》 升序
                    desc = "-1";
                    GlideUtils.glidLocalDrawable(this,imgSortStock,R.drawable.sort_up);
                }else{
                    desc = "1";
                    GlideUtils.glidLocalDrawable(this,imgSortStock,R.drawable.sort_down);
                }
                mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
            }else{
                clearSelectStatus();
                sortBy = "stock";
                desc = "1";
                tvSortStocks.setTextColor(ContextCompat.getColor(mContext,R.color.b3));
                GlideUtils.glidLocalDrawable(this,imgSortStock,R.drawable.sort_down);
                mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,desc);
            }
        }
        rvGoodsList.smoothScrollToPosition(0);
    }

    /**
     * 按销量排序
     * @param view
     */
    public void SortBySales(View view) {
        isRefreshItem = true;
        if(sortBy == null ){
            sortBy = "sales";
            tvSortSales.setTextColor(ContextCompat.getColor(mContext,R.color.b3));
            mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,null);
        }else{
            if(sortBy.equals("sales")){
                ToastUtils.showInfo(this,"当前已按销量排序");
            }else{
                clearSelectStatus();
                sortBy = "sales";
                desc = null;
                tvSortSales.setTextColor(ContextCompat.getColor(mContext,R.color.b3));
                mPresenter.getSpecialOfferGoods(this, "sale", "16", 1,sortBy,null);
            }
        }
        rvGoodsList.smoothScrollToPosition(0);
    }

    private void clearSelectStatus() {
        switch (sortBy) {
            case "sales":
                tvSortSales.setTextColor(ContextCompat.getColor(mContext,R.color.b2));
                break;
            case "stock":
                tvSortStocks.setTextColor(ContextCompat.getColor(mContext,R.color.b2));
                GlideUtils.glidLocalDrawable(this,imgSortStock,R.drawable.sort_no);
                break;
            case "price":
                tvSortPrice.setTextColor(ContextCompat.getColor(mContext,R.color.b2));
                GlideUtils.glidLocalDrawable(this,imgSortPrice,R.drawable.sort_no);
                break;
        }
    }
}
