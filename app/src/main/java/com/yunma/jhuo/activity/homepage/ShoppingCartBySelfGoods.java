package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.adapter.AddSelfGoodsToCartsAdapter;
import com.yunma.adapter.AddSelfGoodsToCartsAdapter.OnNumsSelectedClick;
import com.yunma.bean.AddToCartsRequestBean;
import com.yunma.bean.CartsBean;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.bean.ShoppingCartsBean;
import com.yunma.bean.StocksBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AddShoppingCartsInterface;
import com.yunma.jhuo.m.AddToCartsInterface;
import com.yunma.jhuo.p.AddShoppingCartsPre;
import com.yunma.jhuo.p.AddToCartsPre;
import com.yunma.utils.AppManager;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCartBySelfGoods extends MyCompatActivity implements
        OnNumsSelectedClick, AddShoppingCartsInterface.AddShoppingCartsView, AddToCartsInterface.AddToCartsView {
    @BindView(R.id.view) View view;
    @BindView(R.id.imgGoods) ImageView imgGoods;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.rvSelectSizeNums) ListView rvSelectSizeNums;
    @BindView(R.id.btnAddCarts) Button btnAddCarts;
    private Context mContext;
    private AddSelfGoodsToCartsAdapter mAdapter = null;
    private AddShoppingCartsPre addShoppingCartsPre = null;
    private ShoppingCartsBean shoppingCartsBean;
    private GetSelfGoodsResultBean.SuccessBean.ListBean listBean = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_by_self_goods);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        getDatas();
    }

    private void getDatas() {
        listBean = (GetSelfGoodsResultBean.SuccessBean.ListBean) getIntent().getSerializableExtra("selfGoods");
        GlideUtils.glidNormleFast(this,imgGoods, ConUtils.SElF_GOODS_IMAGE_URL + listBean.getPic().split(",")[0]);
        tvGoodsName.setText(listBean.getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        int remainNums = 0;
        List<StocksBean> stocksBeanList = new ArrayList<>();
        for(int i=0;i<listBean.getStocks().size();i++){
            if(listBean.getStocks().get(i).getNum()!=0){
                remainNums = remainNums + listBean.getStocks().get(i).getNum();
                StocksBean stocksBean = new StocksBean();
                stocksBean.setSize(listBean.getStocks().get(i).getSize());
                stocksBean.setBuyNum(0);
                stocksBean.setId(listBean.getStocks().get(i).getId());
                stocksBean.setNum(listBean.getStocks().get(i).getNum());
                stocksBeanList.add(stocksBean);
                listBean.setStock(remainNums);
            }
        }
        listBean.setStocks(stocksBeanList);
        tvGoodsRemain.setText("库存"+ remainNums +"件");
        mAdapter = new AddSelfGoodsToCartsAdapter(ShoppingCartBySelfGoods.this,
                ShoppingCartBySelfGoods.this);
        rvSelectSizeNums.setAdapter(mAdapter);
        mAdapter.setStocks(listBean.getStocks());
    }

    @OnClick({R.id.view, R.id.imgClose,R.id.btnAddCarts})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view:
                view.setVisibility(View.INVISIBLE);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(0, R.anim.bottom_out);
                break;
            case R.id.imgClose:
                view.setVisibility(View.INVISIBLE);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(0, R.anim.bottom_out);
                break;
            case R.id.btnAddCarts:
                addTOCarts();
                break;
        }
    }

    private void addTOCarts() {
        AddToCartsRequestBean cartsRequestBean = new AddToCartsRequestBean();
        cartsRequestBean.setList("");
        cartsRequestBean.setToken(SPUtils.getToken(mContext));
        List<CartsBean> cartsBeanList = new ArrayList<>();
        for(int i=0;i<listBean.getStocks().size();i++){
            if(listBean.getStocks().get(i).getBuyNum()>0){
                CartsBean cartsBean = new CartsBean();
                cartsBean.setGid(String.valueOf(listBean.getId()));
                cartsBean.setNum(String.valueOf(listBean.getStocks().get(i).getBuyNum()));
                cartsBean.setSize(listBean.getStocks().get(i).getSize());
                cartsBeanList.add(cartsBean);
            }
        }
        cartsRequestBean.setCarts(cartsBeanList);
        AddToCartsPre addToCartsPre = new AddToCartsPre(this);
        addToCartsPre.addToBasket(mContext,cartsRequestBean);
    }

    private void initView() {
        mContext = this;
        addShoppingCartsPre = new AddShoppingCartsPre(this);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public ShoppingCartsBean getShoppingCartsBean() {
        return shoppingCartsBean;
    }

    @Override
    public void showAddShoppingCartsInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else{
            ToastUtils.showSuccess(mContext,msg);
        }
    }

    @Override
    public void onAddMore(int position, int buyNums, int goodsRemain) {
        if(goodsRemain<=0){
            ToastUtils.showShort(this,"库存不足，无法加入购物车");
            return;
        }else{
            listBean.getStocks().get(position).setNum(goodsRemain-1);
            listBean.getStocks().get(position).setBuyNum(buyNums+1);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusMore(int position, int buyNums, int goodsRemain) {
        if(buyNums<=0){
            ToastUtils.showShort(this,"添加数量不能为0");
            return;
        }else{
            listBean.getStocks().get(position).setNum(goodsRemain+1);
            listBean.getStocks().get(position).setBuyNum(buyNums-1);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddToShoppingCarts(int postion, int numbers, List<StocksBean> stocks) {
        ShoppingCartsBean shoppingCartsBean = new ShoppingCartsBean();
        shoppingCartsBean.setToken(SPUtils.getToken(mContext));
        shoppingCartsBean.setGid(String.valueOf(listBean.getId()));
        shoppingCartsBean.setNum(numbers);
        shoppingCartsBean.setSize(stocks.get(postion).getSize());
        setShoppingCartsBean(shoppingCartsBean);
        addShoppingCartsPre.addToBasket();
    }

    public void setShoppingCartsBean(ShoppingCartsBean shoppingCartsBean) {
        this.shoppingCartsBean = shoppingCartsBean;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            mAdapter = null;
        }
        if(listBean!=null){
            listBean = null;
        }
    }

    @Override
    public void showAddCartsInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean != null){
            ToastUtils.showSuccess(mContext,msg);
        }else{
            ToastUtils.showError(mContext,msg);
        }

    }
}
