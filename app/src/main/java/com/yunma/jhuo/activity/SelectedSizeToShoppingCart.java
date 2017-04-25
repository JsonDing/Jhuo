package com.yunma.jhuo.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.AddShoppingCartsNumsAdapter;
import com.yunma.adapter.AddShoppingCartsNumsAdapter.OnNumsSelectedClick;
import com.yunma.bean.*;
import com.yunma.bean.GoodsInfoResultBean.SuccessBean.ListBean.StocksBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.AddShoppingCartsInterface.AddShoppingCartsView;
import com.yunma.jhuo.p.AddShoppingCartsPre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

public class SelectedSizeToShoppingCart extends MyCompatActivity implements
        OnNumsSelectedClick, AddShoppingCartsView {

    @BindView(R.id.view) View view;
    @BindView(R.id.imgGoods) ImageView imgGoods;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.rvSelectSizeNums) ListView rvSelectSizeNums;
    @BindView(R.id.layout) View layout;
    private AddShoppingCartsNumsAdapter mAdapter = null;
    private GoodsInfoResultBean.SuccessBean.ListBean listBean = null;
    private Context mContext;
    private ShoppingCartsBean shoppingCartsBean;
    private AddShoppingCartsPre addShoppingCartsPre = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_size_to_shopping_cart);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        getDatas();
        setDatas();
    }

    private void initView() {
        mContext = this;
        addShoppingCartsPre = new AddShoppingCartsPre(this);
    }

    private void getDatas() {
        listBean = (GoodsInfoResultBean.SuccessBean.ListBean) getIntent().getSerializableExtra("goodsBean");
        GlideUtils.glidNormleFast(this,imgGoods,ConUtils.GOODS_IMAGE_URL+listBean.getPic());
        tvGoodsName.setText(listBean.getBrand()+ "_" + listBean.getYear()+ "_" + listBean.getNumber()+ "_"
                + listBean.getSeason()+ "_" + listBean.getName()+ "_" + listBean.getType());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsRemain.setText("库存"+listBean.getStock()+"件");
    }

    private void setDatas() {
        initBuyNums(listBean.getStocks());
        mAdapter = new AddShoppingCartsNumsAdapter(SelectedSizeToShoppingCart.this,listBean.getStocks(),
                SelectedSizeToShoppingCart.this);
        rvSelectSizeNums.setAdapter(mAdapter);
    }

    private void initBuyNums(List<GoodsInfoResultBean.SuccessBean.ListBean.StocksBean> stocks) {
        for(int i=0;i<stocks.size();i++){
            stocks.get(i).setBuyNum(0);
        }
    }

    @OnClick({R.id.view, R.id.imgClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view:
                view.setVisibility(View.INVISIBLE);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_out, 0);
                break;
            case R.id.imgClose:
                view.setVisibility(View.INVISIBLE);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_out, 0);
                break;
        }
    }

    @Override
    public void onAddMore(int position,int buyNums, int goodsRemain) {
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
    public void onAddToShoppingCarts(int postion, int numbers, List<StocksBean> stocks) {
        ShoppingCartsBean shoppingCartsBean = new ShoppingCartsBean();
        shoppingCartsBean.setToken(SPUtils.getToken(mContext));
        shoppingCartsBean.setGid(stocks.get(postion).getGid());
        shoppingCartsBean.setNum(numbers);
        shoppingCartsBean.setSize(stocks.get(postion).getSize());
        setShoppingCartsBean(shoppingCartsBean);
        addShoppingCartsPre.addToBasket();
    }

    private void setShoppingCartsBean(ShoppingCartsBean shoppingCartsBean) {
        this.shoppingCartsBean = shoppingCartsBean;
    }

    @Override
    public Context getContext() {
        return mContext;
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
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            mAdapter = null;
        }
        if(listBean!=null){
            listBean = null;
        }
    }
}
