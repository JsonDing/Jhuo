package com.yunma.jhuo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.adapter.TopBannerGoodsAdapter;
import com.yunma.bean.BannerBean;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.SortList;
import com.yunma.utils.ToastUtils;
import com.yunma.widget.SpaceItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class AdInfosDetialsActivity extends MyCompatActivity implements
        TopBannerGoodsAdapter.OnItemClick {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgsRight) ImageView imgsRight;
    @BindView(R.id.recyclerview) RecyclerView recyclerview;
    @BindView(R.id.tvSortPrice) TextView tvSortPrice;
    @BindView(R.id.imgSortPrice) ImageView imgSortPrice;
    @BindView(R.id.tvSortSales) TextView tvSortSales;
    @BindView(R.id.tvSortStock) TextView tvSortStock;
    @BindView(R.id.imgSortStock) ImageView imgSortStock;
    private GridLayoutManager gridLayoutManager;
    private TopBannerGoodsAdapter mAdapter;
    private boolean isLineView = false;
    private int sortType = 0;// 0：默认排序 1：价格 2：销量 3：库存
    private int sortWays = 0;//0:降序 1：升序
    private List<SelfGoodsListBean> yunmaBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_infos_detials);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(this), 0, 0);
        initRecyclerView();
        getDatas();
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new ScaleInAnimator());
        recyclerview.getItemAnimator().setAddDuration(600);
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (mAdapter.isLineView(position) || mAdapter.isTittleItem(position)) ?
                        gridLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(this, LinearLayoutManager.HORIZONTAL,
                DensityUtils.dp2px(this, 5), Color.parseColor("#F2F2F2")));
    }

    private void getDatas() {
        mAdapter = new TopBannerGoodsAdapter(this, this);
        recyclerview.setAdapter(mAdapter);
        BannerBean adInfo =
                (BannerBean) getIntent().getSerializableExtra("adInfos");
        assert adInfo != null;
        mAdapter.setTittleContent(adInfo.getContent());
        tvTittle.setText(Html.fromHtml(adInfo.getTitle()));
        yunmaBean = adInfo.getYunmas();
        if (yunmaBean != null && yunmaBean.size() != 0) {
            for (int i = 0; i < yunmaBean.size(); i++) {
                adInfo.getYunmas().get(i).setLineView(isLineView);
            }
            mAdapter.setDatas(yunmaBean);
            recyclerview.scrollTo(0, 0);
        }
    }

    @OnClick(R.id.layoutBack)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    @Override
    public void onItemClick(SelfGoodsListBean mListBean) {
        Intent intent = new Intent(this, BannerGoodsDetial.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd", "yes");
        bundle.putInt("goodId", mListBean.getId());
        bundle.putSerializable("goodsDetials", mListBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void itemChange(View view) {
        if (isLineView) {
            isLineView = false;
            mAdapter.switchView(false);
            GlideUtils.glidLocalDrawable(this, imgsRight, R.drawable.grid_view);
        } else {
            isLineView = true;
            mAdapter.switchView(true);
            GlideUtils.glidLocalDrawable(this, imgsRight, R.drawable.line_view);
        }
    }

    // 按价格排序
    public void sortByPriceClick(View view) {
        SortList<SelfGoodsListBean> sort = new SortList<>();
        if(sortType ==1){
            if(sortWays == 0){
                sortWays = 1;
                sort.sort(yunmaBean,"ymprice","desc");
                GlideUtils.glidLocalDrawable(this, imgSortPrice, R.drawable.sort_up);
            }else{
                sort.sort(yunmaBean,"ymprice","asc");
                GlideUtils.glidLocalDrawable(this, imgSortPrice, R.drawable.sort_down);
            }
        }else{
            sortType = 1;
            sortWays = 0;
            sort.sort(yunmaBean,"ymprice","asc");
            GlideUtils.glidLocalDrawable(this, imgSortPrice, R.drawable.sort_down);
        }
        mAdapter.setDatas(yunmaBean);
        tvSortPrice.setTextColor(ContextCompat.getColor(this,R.color.b3));
    }

    // 按销量排序
    public void sortBySalesClick(View view) {
        if(sortType != 2){
            sortType = 2;
            SortList<SelfGoodsListBean> sort = new SortList<>();
            sort.sort(yunmaBean,"sales","desc");
            tvSortSales.setTextColor(ContextCompat.getColor(this,R.color.b3));
        }else{
            ToastUtils.showWarning(this,"当前已按销量排序");
        }
        mAdapter.setDatas(yunmaBean);
    }

    // 按库存排序
    public void sortByStockClick(View view) {

    }
}
