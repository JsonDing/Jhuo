package com.yunma.jhuo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.BoutiqueAdapter;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.GetGoodsInterface;
import com.yunma.jhuo.p.GetGoodsRecommendPre;
import com.yunma.utils.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.List;

import butterknife.*;

public class EveryDayBoutique extends MyCompatActivity implements
        GetGoodsInterface.GetGoodsRecommendView, BoutiqueAdapter.LayoutOnClick {

    private static int PRICE_UP_TO_DOWN = 1;//价格由高到低
    private static int PRICE_DOWN_TO_UP = 2;//价格由低到高
    private static int SALE_VOLUME_UP_TO_DOWN = 3;//销量由高到低
    private static int SALE_VOLUME_DOWN_TO_UP = 4;//销量由低到高
    private int currPrice = 1;// 1:由高到低，2：由低到高。默认 1
    private int currSaleVolume = 3;// 1:由高到低，2：由低到高。默认 1
    @BindView(R.id.tvAll) TextView tvAll;
    @BindView(R.id.layoutAll) LinearLayout layoutAll;
    @BindView(R.id.tvClothes) TextView tvClothes;
    @BindView(R.id.layoutClothes) LinearLayout layoutClothes;
    @BindView(R.id.tvPants) TextView tvPants;
    @BindView(R.id.layouPants) LinearLayout layouPants;
    @BindView(R.id.tvShoes) TextView tvShoes;
    @BindView(R.id.layoutShoes) LinearLayout layoutShoes;
    @BindView(R.id.tvparts) TextView tvparts;
    @BindView(R.id.layoutParts) LinearLayout layoutParts;
    @BindView(R.id.tvEquip) TextView tvEquip;
    @BindView(R.id.layoutEquip) LinearLayout layoutEquip;
    @BindView(R.id.layoutType) LinearLayout layoutType;
    @BindView(R.id.tvMan) TextView tvMan;
    @BindView(R.id.tvWoman) TextView tvWoman;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutNews) RelativeLayout layoutNews;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.layoutSelectGender) LinearLayout layoutSelectGender;
    @BindView(R.id.layoutSelectType) LinearLayout layoutSelectType;
    @BindView(R.id.layoutPrice) LinearLayout layoutPrice;
    @BindView(R.id.layoutSalesVolume) LinearLayout layoutSalesVolume;
    @BindView(R.id.lvGoodsList) RecyclerView lvGoodsList;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layout) View layout;
    @BindView(R.id.imgMessage) ImageView imgMessage;
    @BindView(R.id.imgRemind) ImageView imgRemind;
    @BindView(R.id.tvGender) TextView tvGender;
    @BindView(R.id.imgsGenderTags) ImageView imgsGenderTags;
    @BindView(R.id.tvType) TextView tvType;
    @BindView(R.id.imgsTypeTags) ImageView imgsTypeTags;
    @BindView(R.id.tvPrice) TextView tvPrice;
    @BindView(R.id.imgsPriceTagsUp) ImageView imgsPriceTagsUp;
    @BindView(R.id.imgsPriceTagsDown) ImageView imgsPriceTagsDown;
    @BindView(R.id.tvSalesVolume) TextView tvSalesVolume;
    @BindView(R.id.imgsSalesVolumeTagsUp) ImageView imgsSalesVolumeTagsUp;
    @BindView(R.id.imgsSalesVolumeTagsDown) ImageView imgsSalesVolumeTagsDown;
    @BindView(R.id.layoutGender) View layoutGender;
    @BindView(R.id.layoutMan) View layoutMan;
    @BindView(R.id.layoutWomen) View layoutWomen;
    @BindView(R.id.viewShade) View viewShade;
    private int nextPage;
    private BoutiqueAdapter mAdapter;
    private GetGoodsRecommendPre recommendPre = null;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_boutique);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBarAndNavigationBar();
        initSwipeRefreshLayout();
        getDatas();
    }

    private void getDatas() {
        recommendPre = new GetGoodsRecommendPre(this);
        recommendPre.getRecommendGoods("8",1);
    }

    private void initStatusBarAndNavigationBar() {
        int statusHeight = ScreenUtils.getStatusHeight(EveryDayBoutique.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        lvGoodsList.setLayoutManager(mgr);
    }

    @OnClick({R.id.layoutBack, R.id.layoutSelectGender, R.id.layoutSelectType, R.id.layoutPrice,
            R.id.layoutSalesVolume, R.id.layoutMan, R.id.layoutWomen, R.id.layoutAll, R.id.layoutClothes, R.id.layouPants, R.id.layoutShoes,
            R.id.layoutParts, R.id.layoutEquip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutSelectGender:
                clearGenderStatus();
                changeGender();
                break;
            case R.id.layoutSelectType:
                clearTypeStatus();
                selectedType();
                break;
            case R.id.layoutPrice:
                changePriceSort();
                break;
            case R.id.layoutSalesVolume:
                changeSaleVolumeSort();
                break;
            case R.id.layoutMan:
                setAllClick();
                tvGender.setText("男");
                viewShade.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                tvGender.setTextColor(getResources().getColor(R.color.color_b1));
                imgsGenderTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layoutWomen:
                setAllClick();
                tvGender.setText("女");
                viewShade.setVisibility(View.GONE);
                layoutGender.setVisibility(View.GONE);
                tvGender.setTextColor(getResources().getColor(R.color.color_b1));
                imgsGenderTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layoutAll:
                setAllClick();
                tvType.setText("全部");
                viewShade.setVisibility(View.GONE);
                layoutType.setVisibility(View.GONE);
                tvType.setTextColor(getResources().getColor(R.color.color_b1));
                imgsTypeTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layoutClothes:
                setAllClick();
                tvType.setText("上装");
                viewShade.setVisibility(View.GONE);
                layoutType.setVisibility(View.GONE);
                tvType.setTextColor(getResources().getColor(R.color.color_b1));
                imgsTypeTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layouPants:
                setAllClick();
                tvType.setText("下装");
                viewShade.setVisibility(View.GONE);
                layoutType.setVisibility(View.GONE);
                tvType.setTextColor(getResources().getColor(R.color.color_b1));
                imgsTypeTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layoutShoes:
                setAllClick();
                tvType.setText("鞋品");
                viewShade.setVisibility(View.GONE);
                layoutType.setVisibility(View.GONE);
                tvType.setTextColor(getResources().getColor(R.color.color_b1));
                imgsTypeTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layoutParts:
                setAllClick();
                tvType.setText("配件");
                viewShade.setVisibility(View.GONE);
                layoutType.setVisibility(View.GONE);
                tvType.setTextColor(getResources().getColor(R.color.color_b1));
                imgsTypeTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
            case R.id.layoutEquip:
                setAllClick();
                tvType.setText("装备");
                viewShade.setVisibility(View.GONE);
                layoutType.setVisibility(View.GONE);
                tvType.setTextColor(getResources().getColor(R.color.color_b1));
                imgsTypeTags.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
                break;
        }
    }

    private void changeSaleVolumeSort() {
        if(currSaleVolume == SALE_VOLUME_UP_TO_DOWN){
            imgsSalesVolumeTagsUp.setImageDrawable(getResources().getDrawable(R.drawable.yellow_up));
            imgsSalesVolumeTagsDown.setImageDrawable(getResources().getDrawable(R.drawable.grey_down));
            currSaleVolume = SALE_VOLUME_DOWN_TO_UP;
            ToastUtils.showShort(EveryDayBoutique.this,"向上");
        }else if(currSaleVolume == SALE_VOLUME_DOWN_TO_UP){
            imgsSalesVolumeTagsUp.setImageDrawable(getResources().getDrawable(R.drawable.grey_up));
            imgsSalesVolumeTagsDown.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
            currSaleVolume = SALE_VOLUME_UP_TO_DOWN;
            ToastUtils.showShort(EveryDayBoutique.this,"向下");
        }
    }

    private void changePriceSort() {
        if(currPrice == PRICE_UP_TO_DOWN){
            imgsPriceTagsUp.setImageDrawable(getResources().getDrawable(R.drawable.yellow_up));
            imgsPriceTagsDown.setImageDrawable(getResources().getDrawable(R.drawable.grey_down));
            currPrice = PRICE_DOWN_TO_UP;
            ToastUtils.showShort(EveryDayBoutique.this,"向上");
        }else if(currPrice == PRICE_DOWN_TO_UP){
            imgsPriceTagsUp.setImageDrawable(getResources().getDrawable(R.drawable.grey_up));
            imgsPriceTagsDown.setImageDrawable(getResources().getDrawable(R.drawable.yellow_down));
            currPrice = PRICE_UP_TO_DOWN;
            ToastUtils.showShort(EveryDayBoutique.this,"向下");
        }
    }

    private void clearTypeStatus() {
        layoutGender.setVisibility(View.GONE);
        tvAll.setTextColor(Color.parseColor("#323232"));
        tvClothes.setTextColor(Color.parseColor("#323232"));
        tvPants.setTextColor(Color.parseColor("#323232"));
        tvShoes.setTextColor(Color.parseColor("#323232"));
        tvparts.setTextColor(Color.parseColor("#323232"));
        tvEquip.setTextColor(Color.parseColor("#323232"));
        tvAll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tvClothes.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tvPants.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tvShoes.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tvparts.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tvEquip.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    private void selectedType() {
        tvType.setTextColor(getResources().getColor(R.color.color_b3));
        viewShade.setVisibility(View.VISIBLE);
        layoutType.setVisibility(View.VISIBLE);
        clearAllClick();
        switch (tvType.getText().toString()){
            case "全部":
                tvAll.setTextColor(Color.parseColor("#f4bd39"));
                tvAll.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
                break;
            case "上装":
                tvClothes.setTextColor(Color.parseColor("#f4bd39"));
                tvClothes.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
                break;
            case "下装":
                tvPants.setTextColor(Color.parseColor("#f4bd39"));
                tvPants.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
                break;
            case "鞋品":
                tvShoes.setTextColor(Color.parseColor("#f4bd39"));
                tvShoes.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
                break;
            case "配件":
                tvparts.setTextColor(Color.parseColor("#f4bd39"));
                tvparts.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
                break;
            case "装备":
                tvEquip.setTextColor(Color.parseColor("#f4bd39"));
                tvEquip.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
                break;

        }
     //   startAnimation(layoutGender);
    }

    private void clearAllClick() {
        layoutSelectGender.setClickable(false);
        layoutSelectType.setClickable(false);
        layoutPrice.setClickable(false);
        layoutSalesVolume.setClickable(false);
    }

    private void setAllClick(){
        layoutSelectGender.setClickable(true);
        layoutSelectType.setClickable(true);
        layoutPrice.setClickable(true);
        layoutSalesVolume.setClickable(true);
    }

    private void clearGenderStatus() {
        clearAllClick();
        layoutType.setVisibility(View.GONE);
        tvMan.setTextColor(Color.parseColor("#323232"));
        tvWoman.setTextColor(Color.parseColor("#323232"));
        tvMan.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tvWoman.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    private void changeGender() {
        tvGender.setTextColor(Color.parseColor("#f4bd39"));

        if (tvGender.getText().toString().equals("男")) {
            tvMan.setTextColor(Color.parseColor("#f4bd39"));
            tvMan.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
        }else if(tvGender.getText().toString().equals("女")){
            tvWoman.setTextColor(Color.parseColor("#f4bd39"));
            tvWoman.setBackground(getResources().getDrawable(R.drawable.bg_text_storage_type));
        }
        viewShade.setVisibility(View.VISIBLE);
        layoutGender.setVisibility(View.VISIBLE);
        layoutSelectGender.setClickable(false);
    //    startAnimation(layoutGender);
    }

    private void startAnimation(View v) {
        //初始化 Scale动画
        ScaleAnimation animation = new ScaleAnimation(0f, 1.0f, 0f, 1.0f);
        //初始化 Alpha动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        //动画集
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(alphaAnimation);
        set.setRepeatCount(0);
        set.setDuration(500);
        set.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        set.setStartOffset(100);//执行前的等待时间
        v.startAnimation(set);
    }

    private void closeAnimation(View v) {

        //初始化 Scale动画
        ScaleAnimation animation = new ScaleAnimation(1.0f, 0f, 1.0f, 0f);
        //初始化 Alpha动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        //动画集
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(alphaAnimation);
        set.setRepeatCount(0);
        set.setDuration(200);
        set.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        set.setStartOffset(50);//执行前的等待时间
        v.startAnimation(set);
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
                recommendPre.getRecommendGoods("8",nextPage);
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
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showRecommendGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean == null) {
            ToastUtils.showError(getApplicationContext(), msg);
        }else{
            nextPage = resultBean.getSuccess().getNextPage();
            if (nextPage == 2) {
                listBean = resultBean.getSuccess().getList();
            } else {
                List<GoodsInfoResultBean.SuccessBean.ListBean> newlist =
                        resultBean.getSuccess().getList();
                for (int i = 0; i < newlist.size(); i++) {
                    listBean.add(newlist.get(i));
                }
                listBean.addAll(resultBean.getSuccess().getList());
                mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
            }
            if (listBean.size() == 0) {
                ToastUtils.showConfusing(getApplicationContext(), "暂无每日精选");
            } else {
                if (mAdapter == null) {
                    mAdapter = new BoutiqueAdapter(EveryDayBoutique.this, listBean,EveryDayBoutique.this);
                    lvGoodsList.setAdapter(mAdapter);
                }
            }
        }

    }

    @Override
    public void onLayoutClick(GoodsInfoResultBean.SuccessBean.ListBean bean) {
        Intent intent = new Intent(EveryDayBoutique.this, EntrepotGoodsDetial.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd","yes");
        bundle.putSerializable("goodsBean",bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(EveryDayBoutique.this);
    }
}
