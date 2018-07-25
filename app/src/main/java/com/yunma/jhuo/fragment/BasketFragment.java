package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.BasketAdapter;
import com.yunma.adapter.BasketAdapter.OnItemClickListener;
import com.yunma.adapter.BasketAdapter.OnNumsSelectedClick;
import com.yunma.adapter.CompletedPayMentAdapter;
import com.yunma.bean.DelGoodsBean;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.bean.OrderBean;
import com.yunma.bean.OrderDetailsBean;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.bean.SelfGoodsResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.homepage.ConfirmOrderForMoreActivity;
import com.yunma.jhuo.activity.homepage.ConfirmOrderForOneActivity;
import com.yunma.jhuo.activity.homepage.GoodsDetialsActivity;
import com.yunma.jhuo.general.DialogStyleLoginActivity;
import com.yunma.jhuo.m.GetShoppingCartListInterface.DelShoppingCartView;
import com.yunma.jhuo.m.GetShoppingCartListInterface.GetShoppingCartView;
import com.yunma.jhuo.m.SelfGoodsInterFace.SearchGoodsDetialView;
import com.yunma.jhuo.m.SelfGoodsInterFace.SearchGoodsView;
import com.yunma.jhuo.p.DeleteShoppingCartPre;
import com.yunma.jhuo.p.GetShoppingCartPre;
import com.yunma.jhuo.p.SearchGoodsPre;
import com.yunma.utils.ConUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.SPUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.Typefaces;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.MyGirdView;
import com.yunma.widget.Titanic;
import com.yunma.widget.TitanicTextView;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

import static com.yunma.R.id.lvBasket;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class BasketFragment extends Fragment implements GetShoppingCartView,
        DelShoppingCartView, BasketAdapter.SelectGoods, OnNumsSelectedClick,SearchGoodsDetialView,
        OnItemClickListener, SearchGoodsView, CompletedPayMentAdapter.OnItemClickListener {

    @BindView(R.id.layoutClose) LinearLayout layoutClose;
    @BindView(R.id.layoutTopMind) RelativeLayout layoutTopMind;
    @BindView(lvBasket) RecyclerView mRecyclerView;
    @BindView(R.id.imgSelectAll) ImageView imgSelectAll;
    @BindView(R.id.tvHasSelected) TextView tvHasSelected;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.btnGoPay) Button btnGoPay;
    @BindView(R.id.btnGoDetele) Button btnGoDetele;
    @BindView(R.id.layoutEdit) LinearLayout layoutEdit;
    @BindView(R.id.layoutComplete) LinearLayout layoutComplete;
    @BindView(R.id.layouStatusBar) FrameLayout layouStatusBar;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.textview) TextView textview;
    @BindView(R.id.layoutNotNull) LinearLayout layoutNotNull;
    @BindView(R.id.gvIntro) MyGirdView gvIntro;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.layoutSelect) View layoutSelect;
    @BindView(R.id.layoutBottom) View layoutBottom;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private BasketAdapter mAdapter;
    private GetShoppingCartPre mPresenter;
    private DeleteShoppingCartPre delPresenter;
    private List<GetShoppingListBean.SuccessBean> successBeanList = null;
    private Activity mActivity;
    private boolean isSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basket_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        initSwipeRefreshLayout();
        setDatas();
        return view;
    }

    private void initView() {
        mActivity = getActivity();
        layouStatusBar.setPadding(0, SPUtils.getStatusHeight(getActivity()), 0, 0);
        btnGoPay.setVisibility(View.VISIBLE);
        btnGoDetele.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new ScaleInLeftAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(400);
        mRecyclerView.getItemAnimator().setMoveDuration(400);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new BasketAdapter(getActivity(),BasketFragment.this,BasketFragment.this,
                BasketFragment.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setDatas() {
        mPresenter = new GetShoppingCartPre(BasketFragment.this);
        delPresenter = new DeleteShoppingCartPre(BasketFragment.this);
        if(MainActivity.mainActivity.isLogin()){
            mPresenter.getShoppingCartList(mActivity);
        }else{
            Intent intent = new Intent(MainActivity.mainActivity,DialogStyleLoginActivity.class);
            MainActivity.mainActivity.startActivity(intent);
            MainActivity.mainActivity.overridePendingTransition(0,
                    R.anim.fade_out);
        }
    }

    @OnClick({R.id.layoutClose, R.id.layoutSelect, R.id.btnGoPay, R.id.layoutEdit,
            R.id.layoutComplete, R.id.btnGoDetele})
    public void onClick(View view) {
        if(MainActivity.mainActivity.isLogin()) {
            switch (view.getId()) {
                case R.id.layoutClose:
                    layoutTopMind.setVisibility(View.GONE);
                    break;
                case R.id.layoutSelect:
                    if (successBeanList != null){
                        getSelectGoods();
                        mAdapter.notifyDataSetChanged();
                        countTotalMoney(mAdapter.getPaylist());
                    }
                    break;
                case R.id.btnGoPay:
                    if (successBeanList != null) {
                        packagePayInfo();
                    }
                    break;
                case R.id.layoutEdit:
                    mAdapter.resetAll();
                    mAdapter.setEditor(true);
                    layoutEdit.setVisibility(View.GONE);
                    btnGoPay.setVisibility(View.GONE);
                    layoutComplete.setVisibility(View.VISIBLE);
                    btnGoDetele.setVisibility(View.VISIBLE);
                    imgSelectAll.setImageDrawable(mActivity.getResources()
                            .getDrawable(R.drawable.unchecked));
                    tvHasSelected.setText("0");
                    tvTotalPrice.setText("0.00");
                    break;
                case R.id.layoutComplete:
                    mAdapter.resetAll();
                    mAdapter.setEditor(false);
                    layoutEdit.setVisibility(View.VISIBLE);
                    btnGoPay.setVisibility(View.VISIBLE);
                    layoutComplete.setVisibility(View.GONE);
                    btnGoDetele.setVisibility(View.GONE);
                    imgSelectAll.setImageDrawable(mActivity.getResources()
                            .getDrawable(R.drawable.unchecked));
                    tvHasSelected.setText("0");
                    tvTotalPrice.setText("0.00");
                    break;
                case R.id.btnGoDetele:
                    if (mAdapter.getSelectedId().isEmpty()) {
                        ToastUtils.showWarning(mActivity, "请选择需要删除的商品");
                    } else {
                        delPresenter.delGoods(mActivity,mAdapter.getSelectedId());
                    }
                    break;
            }
        }else{
            Intent intent = new Intent(MainActivity.mainActivity,DialogStyleLoginActivity.class);
            MainActivity.mainActivity.startActivity(intent);
            MainActivity.mainActivity.overridePendingTransition(0,
                    R.anim.fade_out);
        }
    }

    /**
     * 商品支付信息
     */
    private void packagePayInfo() {
        if (mAdapter.getPaylist() != null && mAdapter.getPaylist().size() != 0) {
            GetShoppingListBean getShoppingListBean = new GetShoppingListBean();
            getShoppingListBean.setSuccess(mAdapter.getPaylist());
            getOrderDatas(getShoppingListBean);
        }else {
            ToastUtils.showInfo(mActivity, "请勾选您想要购买的商品");
        }
    }

    private void getOrderDatas(GetShoppingListBean mListBean) {
        OrderBean orderBean = new OrderBean();
        List<OrderDetailsBean> orderBeanList = new ArrayList<>();
        for(int i=0;i<mListBean.getSuccess().size();i++){
            OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
            orderDetailsBean.setGid(String.valueOf(mListBean.getSuccess().get(i).getGoodsId()));
            orderDetailsBean.setNum(String.valueOf(mListBean.getSuccess().get(i).getCartNum()));
            orderDetailsBean.setSize(mListBean.getSuccess().get(i).getCartSize());
            orderBeanList.add(orderDetailsBean);
        }
        orderBean.setOrderdetails(orderBeanList);
        SPUtils.setDelCartGoodsIds(mActivity,mAdapter.getSelectedId());
        if(mListBean.getSuccess().size()==1){ //单样货品
            Intent intent = new Intent(mActivity, ConfirmOrderForOneActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order",orderBean);
            bundle.putSerializable("goodsBean",mListBean.getSuccess().get(0));
            bundle.putString("totalNums",tvHasSelected.getText().toString().trim());
            bundle.putDouble("totalPrice", Double.valueOf(tvTotalPrice.getText().toString().trim()));
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }else{ //多样货品
            Intent intent = new Intent(mActivity, ConfirmOrderForMoreActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order",orderBean);
            bundle.putSerializable("goodsBean",mListBean);
            bundle.putString("totalNums",tvHasSelected.getText().toString().trim());
            bundle.putDouble("totalPrice", Double.valueOf(tvTotalPrice.getText().toString().trim()));
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 选择支付商品
     */
    private void getSelectGoods() {
        if (isSelected) {
            mAdapter.clearAll();
            isSelected = false;
            imgSelectAll.setImageDrawable(mActivity.getResources()
                    .getDrawable(R.drawable.unchecked));
        } else {
            mAdapter.selectAll();
            isSelected = true;
            imgSelectAll.setImageDrawable(mActivity.getResources()
                    .getDrawable(R.drawable.pitch_on));
        }
    }


    @Override
    public void showDelInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mActivity,msg);
        }else{
            mAdapter.removeMore();
            mAdapter.resetAll();
            isSelected = false;
            layoutEdit.setVisibility(View.VISIBLE);
            btnGoPay.setVisibility(View.VISIBLE);
            layoutComplete.setVisibility(View.GONE);
            btnGoDetele.setVisibility(View.GONE);
            imgSelectAll.setImageDrawable(ContextCompat.getDrawable(mActivity,R.drawable.unchecked));
            tvHasSelected.setText("0");
            tvTotalPrice.setText("0.00");
            int remain = mAdapter.getRemainDatas();
            if(remain==0){
                layoutNull.setVisibility(View.VISIBLE);
                layoutNotNull.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.GONE);
                SearchGoodsPre searchSelfGoodsPre = new SearchGoodsPre(this);
                searchSelfGoodsPre.searchSelfGoodsByStocks(getActivity(),"1","8","1");
            }
        }
    }

    /**
     * 显示购物车货品
     */
    @Override
    public void showShoppingCartList(GetShoppingListBean resultBean, String msg) {
        if(resultBean==null){
            if(msg.equals("token错误，请重新登陆获取token。")){
                ToastUtils.showError(getActivity(),"请登录后查看");
            }else{
                ToastUtils.showError(getActivity(),msg);
            }
        }else{
            if(resultBean.getSuccess().size()!= 0){
                layoutNotNull.setVisibility(View.VISIBLE);
                layoutNull.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.VISIBLE);
                layoutComplete.setVisibility(View.GONE);
                successBeanList = resultBean.getSuccess();
                setRemainStocks(successBeanList);
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                layoutNotNull.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.GONE);
                SearchGoodsPre searchSelfGoodsPre = new SearchGoodsPre(this);
                searchSelfGoodsPre.searchSelfGoodsByStocks(getActivity(),"1","8","1");
            }
        }
    }

    private void setRemainStocks(List<GetShoppingListBean.SuccessBean> successBeanList) {
        int mSize = successBeanList.size();
        List<GetShoppingListBean.SuccessBean> mList = new ArrayList<>();
        for (int position = 0; position < mSize; position++) {
            List<GetShoppingListBean.SuccessBean.StocksBean> stocks =
                    successBeanList.get(position).getStocks();
            if (stocks != null) {
                int iSize = stocks.size();
                for (int i = 0; i < iSize; i++) {
                    if (stocks.get(i).getSize().equals
                            (successBeanList.get(position).getCartSize())) {
                        successBeanList.get(position).setStock(
                                stocks.get(i).getNum());
                        break;
                    } else {
                        successBeanList.get(position).setStock(0);
                    }
                }
                if (successBeanList.get(position).getStock() == 0) {
                    mList.add(successBeanList.get(position));
                }

            } else {
                successBeanList.get(position).setStock(0);
            }
        }
        Collections.reverse(successBeanList);
        successBeanList.removeAll(mList);
        successBeanList.addAll(mList);
        mAdapter.addShoppingCartsList(successBeanList);
        swipeRefreshLayout.finishRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.mainActivity.isLogin()){
            init();
            mPresenter.getShoppingCartList(mActivity);
        }
      // ToastUtils.showInfo(getActivity(),"onResume()");
    }

    public void init(){
        tvHasSelected.setText("0");
        tvTotalPrice.setText("0.00");
        layoutEdit.setVisibility(View.VISIBLE);
        layoutComplete.setVisibility(View.GONE);
        btnGoPay.setVisibility(View.VISIBLE);
        btnGoDetele.setVisibility(View.GONE);
        isSelected = false;
        imgSelectAll.setImageDrawable(ContextCompat.getDrawable(mActivity,R.drawable.unchecked));
        mAdapter.clearAll();
    }

    @Override
    public void selectedGoods(int position, List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        if(shoppingCartsList.get(position).getIsSelected()==1){
            shoppingCartsList.get(position).setIsSelected(0);
        }else{
            shoppingCartsList.get(position).setIsSelected(1);
        }
        int cartNum = shoppingCartsList.get(position).getCartNum();
        int stockNum = shoppingCartsList.get(position).getStock();
        if(cartNum > stockNum){
            shoppingCartsList.get(position).setCartNum(stockNum);
        }
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getPaylist().size()==mAdapter.getShoppingCartsList().size()){
            imgSelectAll.setImageDrawable(
                    ContextCompat.getDrawable(mActivity,R.drawable.pitch_on));
            isSelected = true;
        }else{
            imgSelectAll.setImageDrawable(
                    ContextCompat.getDrawable(mActivity,R.drawable.unchecked));
            isSelected = false;
        }
        countTotalMoney(mAdapter.getPaylist());
    }

    private void countTotalMoney(List<GetShoppingListBean.SuccessBean> paylist) {
        double totalMoney = 0.0;
        int totalNums = 0;
        for(int i=0;i<paylist.size();i++){
            double price;
            if(paylist.get(i).getSpecialprice() != 0.0){
                price = paylist.get(i).getSpecialprice();
            }else{
                price = paylist.get(i).getYunmaprice();
            }
            totalMoney = totalMoney + paylist.get(i).getCartNum()*price;
            totalNums = totalNums + paylist.get(i).getCartNum();
        }
        String str = ValueUtils.toTwoDecimal(totalMoney);
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.RED),0,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvTotalPrice.setText(style);
        tvHasSelected.setText(String.valueOf(totalNums));
    }

    @Override
    public void onAddMore(int position, int buyNums,int remainNums,
                          List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        if(remainNums == buyNums){
            ToastUtils.showShort(mActivity,"库存不足，无法添加更多");
        }else{
            shoppingCartsList.get(position).setCartNum(buyNums+1);
            mAdapter.setCartsList(position,shoppingCartsList);
            countTotalMoney(mAdapter.getPaylist());
        }
    }

    @Override
    public void onMinusMore(int position, int buyNums,int remainNums,
                            List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        if(buyNums==1){
            ToastUtils.showShort(mActivity,"无法继续减少");
        }else{
            shoppingCartsList.get(position).setCartNum(buyNums-1);
            mAdapter.setCartsList(position,shoppingCartsList);
            countTotalMoney(mAdapter.getPaylist());
        }
    }

    @Override
    public void onItemClick( int position) {
        SearchGoodsPre searchPre = new SearchGoodsPre(this,"detial");
        searchPre.getGoodsDetials(getActivity(),successBeanList.get(position).getGoodsId());
    }



    @Override
    public void onItemLongClick(final int position, int id) {
        RequestParams params = new RequestParams(ConUtils.DEL_SHOPPING_CARTS);
        DelGoodsBean delGoodsBean = new DelGoodsBean();
        delGoodsBean.setToken(SPUtils.getToken(getActivity()));
        delGoodsBean.setIds(String.valueOf(id));
        params.setAsJsonContent(true);
        Gson gson = new Gson();
        String strLogin = gson.toJson(delGoodsBean);
        params.setBodyContent(strLogin);
        x.http().post(params, new Callback.CommonCallback<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                    if(result.contains("success")){
                       mAdapter.removeSaleOutGoods(position);
                    }else{
                        ToastUtils.showError(getActivity(),"删除失败，请稍候重试");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    LogUtils.json("responseCode: " + responseCode + "\n" + "--- responseMsg: "
                            + responseMsg + "\n" +"--- errorResult: " + errorResult);
                    ToastUtils.showError(getActivity(),"网络异常，请稍候重试");
                } else { // 其他错误
                    ToastUtils.showError(getActivity(),"服务器异常，请稍候重试");
                    LogUtils.json("------------> " + ex.getMessage());
                }
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    LogUtils.json("删除订单 : " + result);
                }
            }
        });
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.mainActivity);
        final View view = inflater.inflate(R.layout.head_view, null);
        final TitanicTextView tv = view.findViewById(R.id.textview);
        tv.setTypeface(Typefaces.get(MainActivity.mainActivity, "Delicious.ttf"));
        swipeRefreshLayout.setHeaderView(view);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setRefreshEnable(true);
        final Titanic titanic = new Titanic();
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                mPresenter.getShoppingCartList(MainActivity.mainActivity);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        titanic.cancel();
                        swipeRefreshLayout.finishRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onLoading() {
                // TODO: 2017-04-21
            }

            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        break;
                    case SHSwipeRefreshLayout.START:
                        titanic.start(tv);
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
                        break;
                }
            }
        });
    }

    @Override
    public void showSearchInfos(SelfGoodsResultBean resultBean, String msg) {
        if(resultBean !=null){
            List<SelfGoodsListBean> listBean = resultBean.getSuccess().getList();
            if(listBean.size()!=0){
                CompletedPayMentAdapter nAdapter = new CompletedPayMentAdapter(getActivity(),
                        BasketFragment.this);
                gvIntro.setAdapter(nAdapter);
                gvIntro.smoothScrollToPosition(0);
                nAdapter.setListBean(listBean);
            }
        }
    }

    @Override
    public void onItemClick(int position, SelfGoodsListBean listBean) {
        intent(listBean);
    }

    @Override
    public void showGoodsDetials(SelfGoodsListBean resultBean, String msg) {
        if (resultBean != null) {
            if(resultBean.getIssue() == 0){
                ToastUtils.showShort(getActivity(),"\u3000该件商品已下架\u3000");
            }else if(resultBean.getStock() == 0){
                ToastUtils.showShort(getActivity(),"\u3000该件商品已售罄\u3000");
            }else{
                intent(resultBean);
            }
        }else{
            ToastUtils.showShort(getActivity(),msg);
        }
    }

    private void intent(SelfGoodsListBean resultBean) {
        Intent intent = new Intent(getActivity(), GoodsDetialsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("isToEnd", "no");
        bundle.putInt("goodId", resultBean.getId());
        bundle.putSerializable("goodsDetials", resultBean);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }
}
