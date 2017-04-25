package com.yunma.jhuo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.yunma.R;
import com.yunma.adapter.GoodsNumSelectedAdapter;
import com.yunma.bean.GoodsInfoResultBean.SuccessBean.ListBean;
import com.yunma.bean.OrderBean;
import com.yunma.bean.OrderdetailsBean;
import com.yunma.jhuo.activity.homepage.ConfirmOrder;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.general.RegisterAccount;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;

public class SelectedGoodsSizeAndNums extends MyCompatActivity implements
        GoodsNumSelectedAdapter.OnNumsSelectedClick {
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
    private GoodsNumSelectedAdapter nAdapter;
    private Context mContext;
    private ListBean listBean = null;
    private double totalPrice = 0.00;
    private int totalNums = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_size_and_color);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initView();
        getDatas();
        setDatas();
    }

    private void getDatas() {
        listBean = (ListBean) getIntent().getSerializableExtra("goodsBean");
        GlideUtils.glidNormleFast(this,imgGoods,ConUtils.GOODS_IMAGE_URL + listBean.getPic());
        tvGoodsName.setText(listBean.getBrand()+ "_" + listBean.getYear()+ "_" + listBean.getNumber()+ "_"
                + listBean.getSeason()+ "_" + listBean.getName()+ "_" + listBean.getType());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(this,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(this,17),1,s.indexOf("."));
        tvGoodsPrice.setText(ss);
        tvGoodsRemain.setText("库存"+listBean.getStock()+"件");
    }

    private void setDatas() {
        for(int i=0;i<listBean.getStocks().size();i++){
            listBean.getStocks().get(i).setBuyNum(0);
            if(listBean.getStocks().get(i).getNum()==0){
               listBean.getStocks().remove(i);
            }
        }
        if(listBean.getStocks().size()!=0){
            nAdapter = new GoodsNumSelectedAdapter(SelectedGoodsSizeAndNums.this,listBean.getStocks(),
                    SelectedGoodsSizeAndNums.this);
            rvSelectSizeNums.setAdapter(nAdapter);
        }
    }

    private void initView() {
        mContext = this;
    }

    @OnClick({ R.id.view, R.id.imgClose,R.id.btnConfirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_out, 0);
                break;
            case R.id.imgClose:
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_out, 0);
                break;
            case R.id.btnConfirm:
                if(isLogin()) {
                    if(totalNums>0){
                        getOrderDatas();
                    }else{
                       ToastUtils.showConfusing(mContext,"请选择您要购买的尺码及件数");
                    }
                }else{
                    Intent intent = new Intent(mContext, RegisterAccount.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishActivity(this);
                    overridePendingTransition(0,
                            android.R.animator.fade_out);
                }
                break;
        }
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
                orderdetailsBean.setGid(listBean.getStocks().get(i).getGid());
                orderdetailsBean.setNum(String.valueOf(listBean.getStocks().get(i).getBuyNum()));
                orderdetailsBean.setSize(listBean.getStocks().get(i).getSize());
                orderBeanList.add(orderdetailsBean);
                goodsPic = listBean.getPic();
                singlePrice = listBean.getUserPrice();
            }
        }
        orderBean.setOrderdetails(orderBeanList);
        String jsonOrder = new Gson().toJson(orderBeanList);
        LogUtils.log("----> " + jsonOrder + singlePrice);
        if(orderBeanList.size()!=0){
            Intent intent = new Intent(SelectedGoodsSizeAndNums.this, ConfirmOrder.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type",0);//单一商品多个尺码
            bundle.putString("storageType","大仓");
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
            totalPrice = totalPrice + listBean.getUserPrice();
            tvTotalPrice.setText("￥" + ValueUtils.toTwoDecimal(totalPrice));
            totalNums = totalNums + 1;
            String s = "共" + totalNums + "件";
            SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s,
                    Typeface.NORMAL, 0, 1, s.length()-1);
            tvTotalNums.setText(ss);
        }
        nAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusMore(int position,int buyNums,int goodsRemain) {
        if(buyNums==0){
            ToastUtils.showShort(this,"无法继续减少");
            return;
        }else{
            listBean.getStocks().get(position).setBuyNum(buyNums -1);
            listBean.getStocks().get(position).setNum(goodsRemain + 1);
            totalPrice = totalPrice - listBean.getUserPrice();
            tvTotalPrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(totalPrice)));
            totalNums = totalNums - 1;
            String s = "共" + totalNums + "件";
            SpannableStringBuilder ss = ValueUtils.changeText(this, R.color.color_b4, s,
                    Typeface.NORMAL, 0, 1, s.length()-1);
            tvTotalNums.setText(ss);
        }
        nAdapter.notifyDataSetChanged();
    }
}
