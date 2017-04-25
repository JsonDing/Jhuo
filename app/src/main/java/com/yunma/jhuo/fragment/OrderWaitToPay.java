package com.yunma.jhuo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.OrderWaitToPayAdapter;
import com.yunma.adapter.OrderWaitToPayAdapter.DelOrder;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.m.CancleOrderInterface.CancleOrderView;
import com.yunma.jhuo.m.OrderWaitToPayInterface.OrderWaitToPayView;
import com.yunma.jhuo.p.CancleOrderPre;
import com.yunma.jhuo.p.OrderWaitToPayPre;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToPay extends Fragment implements OrderWaitToPayView,
        DelOrder, CancleOrderView{
    @BindView(R.id.lvWaitToPay) ListView lvWaitToPay;
    @BindView(R.id.layoutGoLook) RelativeLayout layoutGoLook;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    private OrderWaitToPayAdapter mAdapter;
    private OrderWaitToPayPre waitToPayPre = null;
    private CancleOrderPre cancleOrderPre = null;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> listBean;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> overdueOrderBean;//过期未支付订单

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_wait_to_pay, container, false);
        ButterKnife.bind(this, view);
        getDatas();
        return view;
    }

    private void getDatas() {
        waitToPayPre = new OrderWaitToPayPre(OrderWaitToPay.this);
        waitToPayPre.getUnPayOrders(getActivity(),"12","1");
        cancleOrderPre = new CancleOrderPre(OrderWaitToPay.this);
    }


    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showCancleInfos(SuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getContext(), msg);
        } else {
            ToastUtils.showSuccess(getContext(), msg);
            waitToPayPre.getUnPayOrders(getActivity(),"12","1");
        }
    }

    @Override
    public void showOrderInfos(OrderUnPayResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getContext(), msg);
            layoutNull.setVisibility(View.VISIBLE);
            lvWaitToPay.setVisibility(View.GONE);
        } else {
            layoutNull.setVisibility(View.GONE);
            lvWaitToPay.setVisibility(View.VISIBLE);
        //    listBean = resultBean.getSuccess().getList();
            overdueOrderBean = new ArrayList<>();
            for(int i=0;i<resultBean.getSuccess().getList().size();i++){
                long orderTime = resultBean.getSuccess().getList().get(i).getDate();
                long systemTime = DateTimeUtils.getCurrentTimeInLong();
                long costTime = systemTime-orderTime;
                if(costTime/(1000*60) >20){
                    overdueOrderBean.add(resultBean.getSuccess().getList().get(i));
                    resultBean.getSuccess().getList().remove(i);
                }
            }
            listBean = resultBean.getSuccess().getList();
            if(listBean.size()!=0){
                if (mAdapter == null) {
                    mAdapter = new OrderWaitToPayAdapter(getActivity(), OrderWaitToPay.this, listBean);
                    lvWaitToPay.setAdapter(mAdapter);
                } else {
                    mAdapter.setListBean(listBean);
                    mAdapter.notifyDataSetChanged();
                }
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                lvWaitToPay.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void delOrder(String ids) {
        showWornning(ids);
    }

    private void showWornning(final String ids) {
        int dialogWith = ScreenUtils.getScreenWidth(getActivity()) - DensityUtils.dp2px(getActivity(), 42);
        new MDDialog.Builder(getActivity())
                .setIcon(R.drawable.logo_sm)
                .setTitle("温馨提示")
                .setContentView(R.layout.item_user_warning)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        TextView tvShow = (TextView) contentView.findViewById(R.id.tvShow);
                        tvShow.setText("订单一经取消，不可恢复");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ids!= null){
                            cancleOrderPre.cancleOrder(ids);
                        }else{
                            ToastUtils.showError(getActivity(),"服务器异常，请稍候重试");
                        }
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(getActivity(), dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if(waitToPayPre!=null){
                waitToPayPre.getUnPayOrders(getActivity(),"12","1");
            }
        }
    }

    @OnClick(R.id.layoutGoLook)
    public void onClick() {
        if(overdueOrderBean!= null && overdueOrderBean.size()!=0){
            layoutNull.setVisibility(View.GONE);
            lvWaitToPay.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new OrderWaitToPayAdapter(getActivity(), OrderWaitToPay.this, overdueOrderBean);
                lvWaitToPay.setAdapter(mAdapter);
            } else {
                mAdapter.setListBean(overdueOrderBean);
                mAdapter.notifyDataSetChanged();
            }
        }else{
            layoutNull.setVisibility(View.VISIBLE);
            lvWaitToPay.setVisibility(View.GONE);
        }

    }

    /*@Override
    public void checkOrderInfos(OrderUnPayResultBean resultBean, String msg) {
        if(resultBean==null){
            layoutNull.setVisibility(View.VISIBLE);
            lvWaitToPay.setVisibility(View.GONE);
            ToastUtils.showError(getActivity(),msg);
        }else{
            assert resultBean.getSuccess().getList() != null;
            listBean = resultBean.getSuccess().getList();
            if(listBean.size()!=0){
                layoutNull.setVisibility(View.GONE);
                lvWaitToPay.setVisibility(View.VISIBLE);
                if (mAdapter == null) {
                    mAdapter = new OrderWaitToPayAdapter(getActivity(), OrderWaitToPay.this, listBean);
                    lvWaitToPay.setAdapter(mAdapter);
                } else {
                    mAdapter.setListBean(listBean);
                    mAdapter.notifyDataSetChanged();
                }
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                lvWaitToPay.setVisibility(View.GONE);
            }
        }
    }*/
}
