package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.adapter.SelfGoodsNumsChooseAdapter;
import com.yunma.adapter.SelfGoodsNumsChooseAdapter.OnNumsSelectedClick;
import com.yunma.bean.AddToCartsRequestBean;
import com.yunma.bean.CartsBean;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.bean.OrderBean;
import com.yunma.bean.OrderdetailsBean;
import com.yunma.bean.StocksBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.general.RegisterAccount;
import com.yunma.jhuo.m.AddToCartsInterface;
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

public class ChooseSizeNumsActivity extends MyCompatActivity implements
         OnNumsSelectedClick, AddToCartsInterface.AddToCartsView {
    @BindView(R.id.view) View view;
    @BindView(R.id.imgGoods) ImageView imgGoods;
    @BindView(R.id.imgClose) ImageView imgClose;
    @BindView(R.id.tvGoodsName) TextView tvGoodsName;
    @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
    @BindView(R.id.tvGoodsRemain) TextView tvGoodsRemain;
    @BindView(R.id.rvSelectSizeNums) ListView rvSelectSizeNums;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.tvTotalNums) TextView tvTotalNums;
    @BindView(R.id.btnConfirm) Button btnConfirm;
    @BindView(R.id.btnAdd) Button btnAdd;
    private AddToCartsRequestBean cartsRequestBean;
    private SelfGoodsNumsChooseAdapter mAdapter;
    private Context mContext;
    private GetSelfGoodsResultBean.SuccessBean.ListBean listBean = null;
    private double totalPrice = 0.00;
    private int totalNums = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_size_nums);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        mContext = this;
        getDatas();
    }

    /**
     * 初始化Intent数据
     */
    private void getDatas() {
        listBean = (GetSelfGoodsResultBean.SuccessBean.ListBean) getIntent().getSerializableExtra("selfGoods");
        assert listBean!=null;
        GlideUtils.glidNormleFast(this,imgGoods, ConUtils.SElF_GOODS_IMAGE_URL + listBean.getPic().split(",")[0]);
        tvGoodsName.setText(listBean.getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        int remainNums = 0;
        List<StocksBean> stocksBeanList = new ArrayList<>();
        for(int i=0;i<listBean.getStocks().size();i++){
            if (listBean.getStocks().get(i).getNum()!=0){
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
        mAdapter = new SelfGoodsNumsChooseAdapter(ChooseSizeNumsActivity.this,
                ChooseSizeNumsActivity.this);
        rvSelectSizeNums.setAdapter(mAdapter);
        mAdapter.setStocks(listBean.getStocks());
    }

    @OnClick({ R.id.view, R.id.imgClose,R.id.btnConfirm,R.id.btnAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.fade_out, 0);
                break;
            case R.id.imgClose:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.fade_out, 0);
                break;
            case R.id.btnConfirm:
                if(isLogin()) {
                    getOrderDatas();
                }else{
                    Intent intent = new Intent(mContext, RegisterAccount.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(ChooseSizeNumsActivity.this);
                    overridePendingTransition(0,
                            R.anim.fade_out);
                }
                break;
            case R.id.btnAdd:
                if(isLogin()) {
                    addTOCarts();
                }else{
                    Intent intent = new Intent(mContext, RegisterAccount.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(ChooseSizeNumsActivity.this);
                    overridePendingTransition(0,
                            R.anim.fade_out);
                }
                break;
        }
    }

    private void addTOCarts() {
        cartsRequestBean = new AddToCartsRequestBean();
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

    @Override
    public boolean isLogin() {
        return super.isLogin();
    }

    private void getOrderDatas() {
        OrderBean orderBean = new OrderBean();
        List<OrderdetailsBean> orderBeanList = new ArrayList<>();
        String goodsPic = null;
        double singlePrice = 0.00;
        for(int i=0;i<listBean.getStocks().size();i++){
            OrderdetailsBean orderdetailsBean = new OrderdetailsBean();
            if(listBean.getStocks().get(i).getBuyNum()>0){
                orderdetailsBean.setGid(String.valueOf(listBean.getId()));
                orderdetailsBean.setNum(String.valueOf(listBean.getStocks().get(i).getBuyNum()));
                orderdetailsBean.setSize(listBean.getStocks().get(i).getSize());
                orderBeanList.add(orderdetailsBean);
                goodsPic = listBean.getPic();
                singlePrice = listBean.getYmprice();
            }
        }
        orderBean.setOrderdetails(orderBeanList);
        if(orderBeanList.size()!=0){
            Intent intent = new Intent(ChooseSizeNumsActivity.this, ConfirmOrder.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type",0);//单一商品多个尺码
            bundle.putString("storageType","自仓");
            bundle.putSerializable("order",orderBean);
            bundle.putString("goodsName",tvGoodsName.getText().toString());
            bundle.putString("goodsPic",goodsPic);
            bundle.putDouble("singlePrice",singlePrice);
            bundle.putString("totalNums",String.valueOf(totalNums));
            bundle.putDouble("totalPrice",totalPrice);
            intent.putExtras(bundle);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(this);
        }
    }

    @Override
    public void onAddMore(int position,int buyNums, int goodsRemain) {
        if(goodsRemain<=0){
            ToastUtils.showShort(this,"库存不足，无法继续添加");
            return;
        }else{
            listBean.getStocks().get(position).setNum(goodsRemain-1);
            listBean.getStocks().get(position).setBuyNum(buyNums+1);
            totalPrice = totalPrice + listBean.getYmprice();
            tvTotalPrice.setText("￥" + ValueUtils.toTwoDecimal(totalPrice));
            totalNums = totalNums + 1;
            String s = "共" + totalNums + "件";
            SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s,
                    Typeface.NORMAL, 0, 1, s.length()-1);
            tvTotalNums.setText(ss);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusMore(int position,int buyNums,int goodsRemain) {
        if(buyNums==0){
            ToastUtils.showShort(this,"无法继续减少");
            return;
        }else{
            listBean.getStocks().get(position).setBuyNum(buyNums -1);
            listBean.getStocks().get(position).setNum(goodsRemain + 1);
            totalPrice = totalPrice - listBean.getYmprice();
            tvTotalPrice.setText("￥" + ValueUtils.toTwoDecimal(totalPrice));
            totalNums = totalNums - 1;
            String s = "共" + totalNums + "件";
            SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s,
                    Typeface.NORMAL, 0, 1, s.length()-1);
            tvTotalNums.setText(ss);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAddCartsInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean != null){
            Intent mIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("cartsRequestBean",  cartsRequestBean);
            bundle.putString("totalNums", String.valueOf(totalNums));
            bundle.putString("tvTotalPrice",ValueUtils.toTwoDecimal(totalPrice));
            mIntent.putExtras(bundle);
            setResult(1, mIntent);
            AppManager.getAppManager().finishActivity(ChooseSizeNumsActivity.this);
            overridePendingTransition(0,
                    R.anim.fade_out);
        }else{
            ToastUtils.showError(mContext,msg);
        }
    }
}
