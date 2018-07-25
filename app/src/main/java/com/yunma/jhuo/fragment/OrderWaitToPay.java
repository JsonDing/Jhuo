package com.yunma.jhuo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.yunma.R;
import com.yunma.adapter.OrderWaitToPayAdapter;
import com.yunma.adapter.OrderWaitToPayAdapter.DelOrder;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.m.CancleOrderInterface.CancleOrderView;
import com.yunma.jhuo.m.OrderWaitToPayInterface.OrderWaitToPayView;
import com.yunma.jhuo.p.CancleOrderPre;
import com.yunma.jhuo.p.OrderWaitToPayPre;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.Typefaces;
import com.yunma.widget.Titanic;
import com.yunma.widget.TitanicTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.library.MDDialog;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToPay extends Fragment implements OrderWaitToPayView,
        DelOrder, CancleOrderView {
    @BindView(R.id.lvWaitToPay) RecyclerView lvWaitToPay;
    @BindView(R.id.layoutGoLook) RelativeLayout layoutGoLook;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    @BindView(R.id.swipeRefreshLayout) SHSwipeRefreshLayout swipeRefreshLayout;
    private OrderWaitToPayAdapter mAdapter;
    private OrderWaitToPayPre waitToPayPre = null;
    private CustomLinearLayoutManager linearLayoutManager;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> listBean;
    private int nextPage = 2;
    private int operationType = -1;
    private int delPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.order_wait_to_pay, container, false);
        ButterKnife.bind(this, rootView);
        initSwipeRefreshLayout();
        getDatas();

        return rootView;
    }

    private void initSwipeRefreshLayout() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view1 = inflater.inflate(R.layout.order_head_view, null);
        final View view2  = inflater.inflate(R.layout.order_foot_view, null);
        final TitanicTextView tv = view1.findViewById(R.id.textview);
        tv.setTypeface(Typefaces.get(getActivity(), "Delicious.ttf"));
        swipeRefreshLayout.setHeaderView(view2);
        swipeRefreshLayout.setFooterView(view1);
        swipeRefreshLayout.setLoadmoreEnable(true);
        swipeRefreshLayout.setRefreshEnable(true);
        final Titanic titanic = new Titanic();
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                operationType = 0;
                linearLayoutManager.setScrollEnabled(false);
                waitToPayPre.getUnPayOrders(getActivity(),"24","1");
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                    }
                }, 1800);
            }

            @Override
            public void onLoading() {
                operationType = 1;
                linearLayoutManager.setScrollEnabled(false);
                waitToPayPre.getUnPayOrders(getActivity(),"24",String.valueOf(nextPage));
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        titanic.cancel();
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
                        titanic.start(tv);
                        break;
                }
            }
        });
    }

    private void getDatas() {
        lvWaitToPay.setHasFixedSize(true);
        lvWaitToPay.setItemAnimator(new FadeInLeftAnimator());
        lvWaitToPay.getItemAnimator().setAddDuration(700);
        lvWaitToPay.getItemAnimator().setMoveDuration(500);
        linearLayoutManager = new CustomLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(true);
        lvWaitToPay.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderWaitToPayAdapter(getActivity(), OrderWaitToPay.this);
        lvWaitToPay.setAdapter(mAdapter);
        waitToPayPre = new OrderWaitToPayPre(OrderWaitToPay.this);
        waitToPayPre.getUnPayOrders(getActivity(),"24","1");
    }

    @Override
    public void showCancleInfos(SuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getActivity(), msg);
        } else {
            ToastUtils.showSuccess(getActivity(), msg);
          //  waitToPayPre.getUnPayOrders(getActivity(),"24","1");
            mAdapter.refreshData(delPos);
        }
    }

    @Override
    public void showOrderInfos(OrderUnPayResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getActivity(), msg);
            layoutNull.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            layoutNull.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            listBean = resultBean.getSuccess().getList();
            if(listBean.size()!=0){
                nextPage = resultBean.getSuccess().getNextPage();
                if(resultBean.getSuccess().isHasNextPage()){
                    swipeRefreshLayout.setLoadmoreEnable(true);
                }else{
                    swipeRefreshLayout.setLoadmoreEnable(false);
                }
                linearLayoutManager.setScrollEnabled(true);
                if(operationType==0){
                    mAdapter.setListBean(listBean);
                    swipeRefreshLayout.finishRefresh();
                }else if(operationType == 1){
                    mAdapter.addListBean(listBean);
                    swipeRefreshLayout.finishLoadmore();
                }else{
                    mAdapter.addListBean(listBean);
                }
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void delOrder(String ids,int delPos) {
        this.delPos = delPos;
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
                        TextView tvShow = contentView.findViewById(R.id.tvShow);
                        tvShow.setText("订单一经取消，不可恢复");
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ids!= null){
                            CancleOrderPre cancleOrderPre = new CancleOrderPre(OrderWaitToPay.this);
                            cancleOrderPre.cancleOrder(getActivity(),ids);
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


    @OnClick(R.id.layoutGoLook)
    public void onClick() {
        Intent intent = new Intent(getActivity(), SpecialPriceActivity.class);
        getActivity().startActivity(intent);
    }

    private class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;
        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
