package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;

import com.google.gson.Gson;
import com.yunma.R;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.homepage.BasketGoodsDetial;
import com.yunma.jhuo.activity.homepage.ConfirmOrder;
import com.yunma.adapter.BasketAdapter;
import com.yunma.adapter.CompletedPayMentAdapter;
import com.yunma.bean.*;
import com.yunma.dao.AppInfo;
import com.yunma.dao.GreenDaoManager;
import com.yunma.jhuo.general.RegisterAccount;
import com.yunma.greendao.AppInfoDao;
import com.yunma.jhuo.m.GetGoodsInterface;
import com.yunma.jhuo.m.GetShoppingCartListInterface.DelShoppingCartView;
import com.yunma.jhuo.m.GetShoppingCartListInterface.GetShoppingCartView;
import com.yunma.jhuo.p.DeleteShoppingCartPre;
import com.yunma.jhuo.p.GetShoppingCartPre;
import com.yunma.utils.*;
import com.yunma.widget.MyGirdView;

import java.util.List;

import butterknife.*;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

/**
 * Created on 2016-12-15
 *
 * @author Json.
 */

public class BasketFragment extends Fragment implements GetShoppingCartView,
        DelShoppingCartView, BasketAdapter.SelectGoods, BasketAdapter.OnNumsSelectedClick,
        GetGoodsInterface.GetGoodsHotView,
        BasketAdapter.OnItemClickListener{

    @BindView(R.id.layoutClose) LinearLayout layoutClose;
    @BindView(R.id.layoutTopMind) RelativeLayout layoutTopMind;
    @BindView(R.id.lvBasket) RecyclerView mRecyclerView;
    @BindView(R.id.imgSelectAll) ImageView imgSelectAll;
    @BindView(R.id.tvHasSelected) TextView tvHasSelected;
    @BindView(R.id.tvTotalPrice) TextView tvTotalPrice;
    @BindView(R.id.btnGoPay) Button btnGoPay;
    @BindView(R.id.btnGoDetele) Button btnGoDetele;
    @BindView(R.id.layoutEdit) LinearLayout layoutEdit;
    @BindView(R.id.layoutComplete) LinearLayout layoutComplete;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.textview) TextView textview;
    @BindView(R.id.layoutNotNull) LinearLayout layoutNotNull;
    @BindView(R.id.gvIntro) MyGirdView gvIntro;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    @BindView(R.id.scroll) ScrollView scroll;
    @BindView(R.id.layoutSelect) View layoutSelect;
    private BasketAdapter mAdapter;
    private GetShoppingCartPre mPresenter;
    private DeleteShoppingCartPre delPresenter;
    private CompletedPayMentAdapter nAdapter;
    private SortList<GetShoppingListBean.SuccessBean> sortList = null;
    private List<GetShoppingListBean.SuccessBean> successBeanList = null;
    private Activity mActivity;
    private boolean isSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.basket_fragment, container, false);
        ButterKnife.bind(this, view);

        initView();
        setDatas();
        return view;
    }

    private void initView() {
        mActivity = getActivity();
        int statusHeight = ScreenUtils.getStatusHeight(getActivity());
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
        btnGoPay.setVisibility(View.VISIBLE);
        btnGoDetele.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new ScaleInLeftAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(400);
        mRecyclerView.getItemAnimator().setMoveDuration(400);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        sortList = new SortList<>();
        mAdapter = new BasketAdapter(getActivity(),BasketFragment.this,BasketFragment.this,
                BasketFragment.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setDatas() {
        mPresenter = new GetShoppingCartPre(BasketFragment.this);
        delPresenter = new DeleteShoppingCartPre(BasketFragment.this);
        if(isLogin()){
            mPresenter.getShoppingCartList(mActivity);
        }else{
            Intent intent = new Intent(MainActivity.mainContext,RegisterAccount.class);
            MainActivity.mainContext.startActivity(intent);
            (MainActivity.mainContext).overridePendingTransition(0,
                    android.R.animator.fade_out);
        }
    }

    @OnClick({R.id.layoutClose, R.id.layoutSelect, R.id.btnGoPay, R.id.layoutEdit,
            R.id.layoutComplete, R.id.btnGoDetele})
    public void onClick(View view) {
        if(isLogin()) {
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
                    mAdapter.clearAll();
                    mAdapter.setEditor(true);
                    mAdapter.notifyDataSetChanged();
                    layoutEdit.setVisibility(View.GONE);
                    btnGoPay.setVisibility(View.GONE);
                    layoutComplete.setVisibility(View.VISIBLE);
                    btnGoDetele.setVisibility(View.VISIBLE);
                    break;
                case R.id.layoutComplete:
                    mAdapter.clearAll();
                    mAdapter.setEditor(false);
                    mAdapter.notifyDataSetChanged();
                    layoutEdit.setVisibility(View.VISIBLE);
                    btnGoPay.setVisibility(View.VISIBLE);
                    layoutComplete.setVisibility(View.GONE);
                    btnGoDetele.setVisibility(View.GONE);
                    break;
                case R.id.btnGoDetele:
                    if (mAdapter.getSelectedId().isEmpty()) {
                        ToastUtils.showWarning(mActivity, "请选择需要删除的商品");
                    } else {
                        delPresenter.delGoods();
                    }
                    break;
            }
        }else{
            Intent intent = new Intent(MainActivity.mainContext,RegisterAccount.class);
            MainActivity.mainContext.startActivity(intent);
            (MainActivity.mainContext).overridePendingTransition(0,
                    android.R.animator.fade_out);
        }
    }

    /**
     * 商品支付信息
     */
    private void packagePayInfo() {
        if (mAdapter.getPaylist() != null && mAdapter.getPaylist().size() != 0) {
            GetShoppingListBean getShoppingListBean = new GetShoppingListBean();
            getShoppingListBean.setSuccess(mAdapter.getPaylist());
            Intent intent = new Intent(mActivity, ConfirmOrder.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putString("totalNums", tvHasSelected.getText().toString().trim());
            bundle.putString("totalPrice", tvTotalPrice.getText().toString().trim());
            LogUtils.log("goodsListBean" + new Gson().toJson(getShoppingListBean));
            bundle.putSerializable("goodsListBean", getShoppingListBean);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        } else {
            ToastUtils.showInfo(mActivity, "请勾选您想要购买的商品");
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
    public void showHotGoodsInfos(GoodsInfoResultBean resultBean, String msg) {
        if(resultBean==null){
            ToastUtils.showError(mActivity,msg);
        }else {
            if(resultBean.getSuccess().getList()!=null&&
                    resultBean.getSuccess().getList().size()!=0){
                List<GoodsInfoResultBean.SuccessBean.ListBean> listBean = resultBean.getSuccess().getList();
                if(nAdapter==null){
                    nAdapter = new CompletedPayMentAdapter(mActivity,listBean);
                    gvIntro.setAdapter(nAdapter);
                }else {
                    nAdapter.notifyDataSetChanged();
                }
                scroll.smoothScrollTo(0,0);
            }
        }
    }

    @Override
    public String getDelId() {
        return mAdapter.getSelectedId();
    }

    @Override
    public void showDelInfos(SuccessResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(mActivity,msg);
        }else{
            mAdapter.remove();
        }
    }

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
                LogUtils.log("shoppingCartsList: " + successBeanList.size());
                setRemainStocks(successBeanList);
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                layoutNotNull.setVisibility(View.GONE);
                layoutEdit.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.GONE);
            }
        }
    }

    private void setRemainStocks(List<GetShoppingListBean.SuccessBean> successBeanList) {
        for(int position=0;position<successBeanList.size();position++){
            if(successBeanList.get(position).getStocks()!=null){
                for(int i=0;i<successBeanList.get(position).getStocks().size();i++){
                    if(successBeanList.get(position).getStocks().get(i).getSize().equals
                            (successBeanList.get(position).getCartSize())){
                        successBeanList.get(position).setStock(
                                successBeanList.get(position).getStocks().get(i).getNum());
                        break;
                    }else{
                        successBeanList.get(position).setStock(0);
                    }
                }
            }else{
                successBeanList.get(position).setStock(0);
            }
        }
        sortList.sort(successBeanList,"stock","desc");
        mAdapter.setShoppingCartsList(successBeanList);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(isLogin()){
                tvHasSelected.setText("0");
                tvTotalPrice.setText("0.00");
                layoutEdit.setVisibility(View.VISIBLE);
                layoutComplete.setVisibility(View.GONE);
                btnGoPay.setVisibility(View.VISIBLE);
                btnGoDetele.setVisibility(View.GONE);
                isSelected = false;
                imgSelectAll.setImageDrawable(mActivity.getResources()
                        .getDrawable(R.drawable.unchecked));
                mPresenter.getShoppingCartList(mActivity);
            }else{
                Intent intent = new Intent(mActivity, RegisterAccount.class);
                mActivity.startActivity(intent);
                (MainActivity.mainContext).overridePendingTransition(0,
                        android.R.animator.fade_out);
            }
        }
    }

    @Override
    public void selectedGoods(int position, List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        if(shoppingCartsList.get(position).getIsSelected()==1){
            shoppingCartsList.get(position).setIsSelected(0);
        }else{
            shoppingCartsList.get(position).setIsSelected(1);
        }
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getPaylist().size()==mAdapter.getShoppingCartsList().size()){
            imgSelectAll.setImageDrawable(mActivity.getResources()
                    .getDrawable(R.drawable.pitch_on));
            isSelected = true;
        }else{
            imgSelectAll.setImageDrawable(mActivity.getResources()
                    .getDrawable(R.drawable.unchecked));
            isSelected = false;
        }
        countTotalMoney(mAdapter.getPaylist());

    }

    private void countTotalMoney(List<GetShoppingListBean.SuccessBean> paylist) {
        double totalMoney = 0.0;
        int totalNums = 0;
        for(int i=0;i<paylist.size();i++){
            totalMoney = totalMoney + paylist.get(i).getCartNum()*paylist.get(i).getYunmaprice();
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
        if(remainNums<=buyNums){
            ToastUtils.showWarning(mActivity,"库存不足，无法添加更多");
        }else{
            shoppingCartsList.get(position).setCartNum(buyNums+1);
            mAdapter.setShoppingCartsList(shoppingCartsList);
            mAdapter.notifyDataSetChanged();
            countTotalMoney(mAdapter.getPaylist());
        }

    }

    @Override
    public void onMinusMore(int position, int buyNums,int remainNums,
                            List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        if(buyNums<2){
            ToastUtils.showShort(mActivity,"无法继续减少");
            return;
        }
        shoppingCartsList.get(position).setCartNum(buyNums-1);
        mAdapter.setShoppingCartsList(shoppingCartsList);
        mAdapter.notifyDataSetChanged();
        countTotalMoney(mAdapter.getPaylist());
    }

    private boolean isLogin() {
        List<AppInfo> appInfos = getDao().loadAll();
        return appInfos.size() != 0 && appInfos.get(0).getIsLogin() != 0;
    }

    private AppInfoDao getDao(){
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent;Bundle bundle;
        if(successBeanList.get(position).getRepoid()==1){
            intent =  new Intent(mActivity,BasketGoodsDetial.class);
            bundle = new Bundle();
            bundle.putString("type","自仓");
            bundle.putString("goodsNumber",successBeanList.get(position).getNumber());
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }else{
            intent =  new Intent(mActivity,BasketGoodsDetial.class);
            bundle = new Bundle();
            bundle.putString("type","大仓");
            bundle.putString("goodsNumber",successBeanList.get(position).getNumber());
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }
}
