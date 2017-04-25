package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.*;
import com.yunma.adapter.SelfGoodsNumsChooseAdapter.OnNumsSelectedClick;
import com.yunma.bean.*;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.activity.SearchGoodsByCode;
import com.yunma.jhuo.general.DialogStyleLoginActivity;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.jhuo.m.SelfGoodsInterFace.GetSelfGoodsView;
import com.yunma.jhuo.p.SelfGoodsPre;
import com.yunma.utils.*;
import com.yunma.widget.*;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;


public class SpecialPriceActivity extends MyCompatActivity
        implements OnBuyClick, GetSelfGoodsView, OnNumsSelectedClick {

    @BindView(R.id.layoutBack) View layoutBack;
    @BindView(R.id.imgGoods) ImageViewBorder imgGoods;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.tvNewAddGoods) TextView tvNewAddGoods;
    @BindView(R.id.layoutTop) LinearLayout layoutTop;
    @BindView(R.id.rvGoodsList) RecyclerView rvGoodsList;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.rvSelectSizeNums) ListView rvSelectSizeNums;
    @BindView(R.id.tvHasSelected) TextView tvHasSelected;
    @BindView(R.id.layout) RelativeLayout layout;
    @BindView(R.id.imgPhotos) CircleImageView imgPhotos;
    @BindView(R.id.layoutPhoto) LinearLayout layoutPhoto;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.view) View mView;
    @BindView(R.id.imgsClose) ImageView imgsClose;
    @BindView(R.id.lvBasket) ListView lvBasket;
    @BindView(R.id.btnGoConfirmOrder) Button btnGoConfirmOrder;
    @BindView(R.id.layoutBasket) LinearLayout layoutBasket;
    @BindView(R.id.frameAddGoods) FrameLayout frameAddGoods;
    @BindView(R.id.tvTittle) TextView tvTittle;
    @BindView(R.id.imgsRight) ImageView imgsRight;
    @BindView(R.id.layoutRight) LinearLayout layoutRight;
    @BindView(R.id.handbag) ImageView handbag;
    @BindView(R.id.view2) RelativeLayout view2;
    @BindView(R.id.view3) View view3;
    private List<ListBean> goodsListBeen = null;
    private SelfGoodsPre mPresenter = null;
    private SpecialPriceAdapter mAdapter = null;
    private SpecialPriceBasketAdapter basketAdapter;
    private SelfGoodsNumsChooseAdapter chooseAdapter = null;
    private double totalPrice = 0.00;
    private int totalNums = 0;
    private int singLeGoodsNums = 0;
    private int nextPage;
    private int selectPosition;
    private Context mContext;
    private ListBean mListBean;

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
        mPresenter.getSpecialOfferGoods(getApplicationContext(), "sale", "8", 1);
    }

    private void initView() {
        mContext = this;
        tvTittle.setText("特价限量抢");
        GlideUtils.glidLocalDrawable(mContext,imgsRight,R.drawable.re_search_big);
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(mContext), 0, 0);
        btnGoConfirmOrder.setEnabled(false);
        btnGoConfirmOrder.setClickable(false);
        layoutPhoto.setEnabled(false);
        layoutPhoto.setClickable(false);
        btnGoConfirmOrder.setBackground(getResources().getDrawable(R.drawable.gray_button_background));
        rvGoodsList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(SpecialPriceActivity.this);
        rvGoodsList.setLayoutManager(mLayoutManager);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @OnClick({R.id.layoutBack, R.id.imgClose, R.id.tvHasSelected,R.id.layoutRight,
            R.id.layoutPhoto, R.id.imgsClose, R.id.btnGoConfirmOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutRight:
                Intent intent = new Intent(this, SearchGoodsByCode.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;
            case R.id.imgClose:
                mView.setVisibility(View.GONE);
                frameAddGoods.setVisibility(View.GONE);
                break;
            case R.id.layoutPhoto:
                if (isLogin()) {
                    getBasketList();
                } else {
                    Intent intent1 = new Intent(this, DialogStyleLoginActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(0,R.anim.fade_out);
                }
                break;
            case R.id.imgsClose:
                mView.setVisibility(View.GONE);
                layoutBasket.setVisibility(View.GONE);
                break;
            case R.id.btnGoConfirmOrder:
                if (isLogin()) {
                    if (mAdapter.getGoodsInfo() != null && mAdapter.getGoodsInfo().size() > 0) {
                        getShoppingList(mAdapter.getGoodsInfo());
                        Intent intent2 = new Intent(SpecialPriceActivity.this, ConfirmOrder.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 1);
                        bundle.putString("totalNums", String.valueOf(totalNums));
                        bundle.putString("totalPrice", String.valueOf(totalPrice));
                        bundle.putSerializable("goodsListBean", getShoppingList(mAdapter.getGoodsInfo()));
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                    } else {
                        ToastUtils.showWarning(getApplicationContext(), "请添加购买商品");
                    }
                } else {
                    Intent intent1 = new Intent(this, DialogStyleLoginActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(0,R.anim.fade_out);
                }

                break;
        }
    }

    /**
     * 获取添加到虚拟购物车的商品
     * @param goodsInfo
     * @return
     */
    private GetShoppingListBean getShoppingList(List<ListBean> goodsInfo) {
        GetShoppingListBean shoppingListBean = new GetShoppingListBean();
        List<GetShoppingListBean.SuccessBean> successBeenList = new ArrayList<>();
        for (int i = 0; i < goodsInfo.size(); i++) {
            for (int j = 0; j < goodsInfo.get(i).getStocks().size(); j++) {
                if (goodsInfo.get(i).getStocks().get(j).getBuyNum() != 0) {
                    GetShoppingListBean.SuccessBean successBean = shoppingListBean.new SuccessBean();
                    successBean.setName(goodsInfo.get(i).getName());
                    successBean.setGoodsId(String.valueOf(goodsInfo.get(i).getId()));
                    successBean.setPic(goodsInfo.get(i).getPic());
                    successBean.setYunmaprice(goodsInfo.get(i).getYmprice());
                    successBean.setSaleprice(goodsInfo.get(i).getSaleprice());
                    successBean.setRepoid(goodsInfo.get(i).getRepoid());
                    successBean.setNumber(goodsInfo.get(i).getNumber());
                    successBean.setCartNum(goodsInfo.get(i).getStocks().get(j).getBuyNum());
                    successBean.setCartSize(goodsInfo.get(i).getStocks().get(j).getSize());
                    successBeenList.add(successBean);
                }
            }
            shoppingListBean.setSuccess(successBeenList);
        }
        //    LogUtils.log("shoppingListBean ---> " + new Gson().toJson(shoppingListBean));
        return shoppingListBean;
    }

    private void getBasketList() {
        mView.setVisibility(View.VISIBLE);
        frameAddGoods.setVisibility(View.GONE);
        layoutBasket.setVisibility(View.VISIBLE);
        if (basketAdapter == null) {
            basketAdapter = new SpecialPriceBasketAdapter(SpecialPriceActivity.this, mAdapter.getGoodsInfo(),
                    SpecialPriceActivity.this);
            lvBasket.setAdapter(basketAdapter);
        } else {
            basketAdapter.setGoodsInfo(mAdapter.getGoodsInfo());
            basketAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 立即购买
     * @param position
     * @param mListBean
     */
    @Override
    public void onBuyClickListener(int position, ListBean mListBean) {
        if(isLogin()){
            showSizeAndNumsCanSelect(position,mListBean);
        }else{
            Intent intent1 = new Intent(this, DialogStyleLoginActivity.class);
            startActivity(intent1);
            overridePendingTransition(0,R.anim.fade_out);
        }
    }

    private void showSizeAndNumsCanSelect(int position, ListBean mListBean) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        mView.setVisibility(View.VISIBLE);
        frameAddGoods.setVisibility(View.VISIBLE);
        layoutPhoto.startAnimation(shake);
        setListBean(mListBean);
        setSelectPosition(position);
        imgGoods.setColour(Color.parseColor("#f4bd39"));
        imgGoods.setBorderWidth(5);
        if (mListBean.getPic() != null) {
            GlideUtils.glidNormleFast(mContext, imgGoods, ConUtils.SElF_GOODS_IMAGE_URL
                    + mListBean.getPic().split(",")[0]);
        } else {
            imgGoods.setImageDrawable(getResources().getDrawable(R.drawable.default_pic));
        }
        tvGoodsName.setText(mListBean.getName());
        String s = "￥" + ValueUtils.toTwoDecimal(mListBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s, Typeface.NORMAL,
                DensityUtils.sp2px(this, 17), 1, s.indexOf("."));
        tvGoodsPrice.setText(ss);
        int remainNums = 0;
        List<StocksBean> stocksBeanList = new ArrayList<>();
        for (int i = 0; i < mListBean.getStocks().size(); i++) {
            if (mListBean.getStocks().get(i).getNum()!=0){
                remainNums = remainNums + mListBean.getStocks().get(i).getNum();
                StocksBean stocksBean = new StocksBean();
                stocksBean.setSize(mListBean.getStocks().get(i).getSize());
                stocksBean.setBuyNum(0);
                stocksBean.setId(mListBean.getStocks().get(i).getId());
                stocksBean.setNum(mListBean.getStocks().get(i).getNum());
                stocksBeanList.add(stocksBean);
                mListBean.setStock(remainNums);
            }
        }
        mListBean.setStocks(stocksBeanList);
        tvGoodsRemain.setText(String.valueOf(remainNums));
        if (chooseAdapter == null) {
            chooseAdapter = new SelfGoodsNumsChooseAdapter(
                    SpecialPriceActivity.this, SpecialPriceActivity.this);
            rvSelectSizeNums.setAdapter(chooseAdapter);
            chooseAdapter.setStocks(mListBean.getStocks());
        } else {
            chooseAdapter.setStocks(mListBean.getStocks());
            chooseAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 查看详情
     * @param goodId
     * @param itemId
     * @param mListBean
     */
    @Override
    public void onLookGoodDetial(int goodId, int itemId, ListBean mListBean) {
        Intent intent = new Intent(SpecialPriceActivity.this, SelfGoodsDetials.class);
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
    public void showSpecialOfferGoods(GetSelfGoodsResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getApplicationContext().getApplicationContext(), msg);
        } else {
            if (resultBean.getSuccess().isHasNextPage()) {
                swipeRefreshLayout.setLoadmoreEnable(true);
            } else {
                swipeRefreshLayout.setLoadmoreEnable(false);
            }
            nextPage = resultBean.getSuccess().getNextPage();
            if (resultBean.getSuccess().getPageNum() == 1) {
                goodsListBeen = resultBean.getSuccess().getList();
                if (goodsListBeen.size() != 0) {
                    for (int i = 0; i < goodsListBeen.size(); i++) {
                        int remainNums = 0;
                        for (int j = 0; i < goodsListBeen.get(i).getStocks().size(); i++) {
                            remainNums = remainNums + goodsListBeen.get(i).getStocks().get(j).getNum();
                        }
                        goodsListBeen.get(i).setRemain(remainNums);
                    }
                    if (mAdapter == null) {
                        mAdapter = new SpecialPriceAdapter(SpecialPriceActivity.this, goodsListBeen, this);
                        rvGoodsList.setAdapter(mAdapter);
                        btnGoConfirmOrder.setEnabled(true);
                        btnGoConfirmOrder.setClickable(true);
                        layoutPhoto.setEnabled(true);
                        layoutPhoto.setClickable(true);
                        btnGoConfirmOrder.setBackground(getResources()
                                .getDrawable(R.drawable.btn_selector_orange));
                        rvGoodsList.addItemDecoration(new SpaceItemDecoration(SpecialPriceActivity.this,
                                LinearLayoutManager.HORIZONTAL,
                                DensityUtils.dp2px(SpecialPriceActivity.this, 0), Color.parseColor("#F2F2F2")));
                        rvGoodsList.smoothScrollToPosition(0);
                    }
                }
            } else if (resultBean.getSuccess().getPageNum() > 1 && resultBean.getSuccess().getPageNum()
                    <= resultBean.getSuccess().getPages()) {
                goodsListBeen.addAll(resultBean.getSuccess().getList());
               /* SortList<GetSelfGoodsResultBean.SuccessBean.ListBean> sortList =
                        new SortList<>();
                sortList.sort(goodsListBeen,"stock","desc");*/
                swipeRefreshLayout.finishLoadmore();
                mAdapter.setGoodsListBeen(goodsListBeen);
                //   mAdapter.notifyDataSetChanged();
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
        final ImageView imgSun = (ImageView) view.findViewById(R.id.imgSun);
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
                mPresenter.getSpecialOfferGoods(getApplicationContext(), "sale", "8", nextPage);
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

    /**
     * 添加件数
     * @param position
     * @param buyNums
     * @param goodsRemain
     */
    @Override
    public void onAddMore(int position, int buyNums, int goodsRemain) {
        if (goodsRemain <= 0) {
            ToastUtils.showShort(this, "库存不足，无法继续添加");
            return;
        } else {
            // mAdapter.getGoodsListBeen().get(getSelectPosition()).getStocks().get(position).setNum(goodsRemain-1);
            getListBean().getStocks().get(position).setNum(goodsRemain - 1);
            getListBean().getStocks().get(position).setBuyNum(buyNums + 1);
            totalPrice = totalPrice + getListBean().getYmprice();
            String price = "共计￥" + totalPrice + "元";
            SpannableStringBuilder s1 = ValueUtils.changeText(this, R.color.color_b4, price,
                    Typeface.NORMAL, 0, 3, price.length() - 1);
            tvTotalPrice.setText(s1);
            totalNums = totalNums + 1;
            String s = "共" + totalNums + "件";
            SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s,
                    Typeface.NORMAL, 0, 1, s.length() - 1);
            tvHasSelected.setText(ss);
            tvGoodsRemain.setText("" + (Integer.valueOf(
                    tvGoodsRemain.getText().toString().trim()) - 1) + "");
            singLeGoodsNums = goodsListBeen.get(getSelectPosition()).getTotalBuyNums() + 1;
            goodsListBeen.get(getSelectPosition()).setTotalBuyNums(singLeGoodsNums);
        }
        mAdapter.notifyDataSetChanged();
        chooseAdapter.notifyDataSetChanged();
    }

    /**
     * 减少件数
     * @param position
     * @param buyNums
     * @param goodsRemain
     */
    @Override
    public void onMinusMore(int position, int buyNums, int goodsRemain) {
        if (buyNums == 0) {
            ToastUtils.showShort(this, "无法继续减少");
            return;
        } else {
            getListBean().getStocks().get(position).setBuyNum(buyNums - 1);
            getListBean().getStocks().get(position).setNum(goodsRemain + 1);
            totalPrice = totalPrice - getListBean().getYmprice();
            String price = "共计￥" + totalPrice + "元";
            SpannableStringBuilder s1 = ValueUtils.changeText(this, R.color.color_b4, price,
                    Typeface.NORMAL, 0, 3, price.length() - 1);
            tvTotalPrice.setText(s1);
            totalNums = totalNums - 1;
            String nums = "共" + totalNums + "件";
            SpannableStringBuilder s2 = ValueUtils.changeText(this, R.color.color_b4, nums,
                    Typeface.NORMAL, 0, 1, nums.length() - 1);
            tvHasSelected.setText(s2);
            tvGoodsRemain.setText("" + (Integer.valueOf(
                    tvGoodsRemain.getText().toString().trim()) + 1) + "");
            singLeGoodsNums = goodsListBeen.get(getSelectPosition()).getTotalBuyNums() - 1;
            goodsListBeen.get(getSelectPosition()).setTotalBuyNums(singLeGoodsNums);
        }
        mAdapter.notifyDataSetChanged();
        chooseAdapter.notifyDataSetChanged();
    }

    public ListBean getListBean() {
        return mListBean;
    }

    public void setListBean(ListBean listBean) {
        this.mListBean = listBean;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void updataAdapter() {
        mAdapter.setGoodsListBeen(getGoodsListBeen());
        mAdapter.notifyDataSetChanged();
        basketAdapter.setGoodsInfo(mAdapter.getGoodsInfo());
        basketAdapter.notifyDataSetChanged();

    }

    public void addDatas(double p) {
        totalNums = totalNums + 1;
        String num = "共" + totalNums + "件";
        SpannableStringBuilder s2 = ValueUtils.changeText(this, R.color.color_b4, num,
                Typeface.NORMAL, 0, 1, num.length() - 1);
        tvHasSelected.setText(s2);
        totalPrice = totalPrice + p;
        String price = "共计￥" + totalPrice + "元";
        SpannableStringBuilder s1 = ValueUtils.changeText(this, R.color.color_b4, price,
                Typeface.NORMAL, 0, 3, price.length() - 1);
        tvTotalPrice.setText(s1);
    }

    public void minusDatas(double p) {
        totalNums = totalNums - 1;
        String num = "共" + totalNums + "件";
        SpannableStringBuilder s2 = ValueUtils.changeText(this, R.color.color_b4, num,
                Typeface.NORMAL, 0, 1, num.length() - 1);
        tvHasSelected.setText(s2);
        totalPrice = totalPrice - p;
        String price = "共计￥" + totalPrice + "元";
        SpannableStringBuilder s1 = ValueUtils.changeText(this, R.color.color_b4, price,
                Typeface.NORMAL, 0, 3, price.length() - 1);
        tvTotalPrice.setText(s1);
    }

    public List<ListBean> getGoodsListBeen() {
        return goodsListBeen;
    }

    @Override
    public boolean isLogin() {
        return super.isLogin();
    }
}
