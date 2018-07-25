package com.yunma.jhuo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.yunma.R;
import com.yunma.adapter.OrderWaitToReceiveAdapter;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.ApplyAfterSalesActivity;
import com.yunma.jhuo.activity.mine.ApplySingleAfterSalesActivity;
import com.yunma.jhuo.activity.mine.InvoiceInfosActivity;
import com.yunma.jhuo.activity.mine.MyOrderManage;
import com.yunma.jhuo.activity.mine.OrderTraceActivity;
import com.yunma.jhuo.m.CancleOrderInterface.CancleOrderView;
import com.yunma.jhuo.m.OrderHadConfirmInterFace.OnAdapterClick;
import com.yunma.jhuo.m.OrderWaitToReceiveInterface.OrderWaitToReceiveView;
import com.yunma.jhuo.p.CancleOrderPre;
import com.yunma.jhuo.p.OrderWaitToReceivePre;
import com.yunma.utils.AppManager;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.LogUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.carbs.android.library.MDDialog;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToReceive  extends Fragment implements
        OrderWaitToReceiveView, OnAdapterClick,CancleOrderView {
    @BindView(R.id.lvWaitToReceive) ListView lvWaitToReceive;
    @BindView(R.id.layoutGoLook) RelativeLayout layoutGoLook;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    private Context mContext;
    private OrderWaitToReceiveAdapter mAdapter;
    private OrderWaitToReceivePre receivePre = null;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> listBean = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_wait_to_receive, container, false);
        ButterKnife.bind(this, view);
        getDatas();
        return view;
    }

    private void getDatas() {
        mContext = getActivity();
        receivePre = new OrderWaitToReceivePre(this);
        receivePre.getUnReceiveOrders(getActivity(),"100","1");
    }

    @Override
    public void showCancleInfos(SuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getActivity(), msg);
        } else {
            ToastUtils.showSuccess(getActivity(), msg);
            receivePre.getUnReceiveOrders(getActivity(),"100","1");
        }
    }

    @Override
    public void showOrderInfos(OrderUnPayResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getActivity(),msg);
            layoutNull.setVisibility(View.VISIBLE);
            lvWaitToReceive.setVisibility(View.GONE);
        }else{
            if(EmptyUtil.isEmpty(resultBean.getSuccess().getList())){
                layoutNull.setVisibility(View.VISIBLE);
                lvWaitToReceive.setVisibility(View.GONE);
            }else {
                listBean = resultBean.getSuccess().getList();
                if(listBean!=null){
                    layoutNull.setVisibility(View.GONE);
                    lvWaitToReceive.setVisibility(View.VISIBLE);
                    if(mAdapter==null){
                        mAdapter = new OrderWaitToReceiveAdapter(mContext,listBean,OrderWaitToReceive.this);
                        lvWaitToReceive.setAdapter(mAdapter);
                    }else{
                        mAdapter.setListBean(listBean);
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    layoutNull.setVisibility(View.VISIBLE);
                    lvWaitToReceive.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onDeleteClickListener(int position, final String id) {
        final String[] messages = new String[]{"删除后订单将不再显示","是否确定删除"};
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 42);
        new MDDialog.Builder(mContext)
                .setIcon(R.drawable.logo_sm)
                .setMessages(messages)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CancleOrderPre cancleOrderPre = new CancleOrderPre(OrderWaitToReceive.this);
                        cancleOrderPre.cancleOrder(getActivity(),id);
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(true)
                .setShowButtons(true)
                .create()
                .show();

    }

    @Override
    public void onApplyClickListener(int position, ListBean orderdetails, int totalNums) {
        Intent intent;
        Bundle bundle;
        if(totalNums == 1){
            intent = new Intent(mContext,ApplySingleAfterSalesActivity.class);
            bundle = new Bundle();
            bundle.putSerializable("orderdetails",orderdetails.getOrderdetails().get(0));
        }else{
            intent = new Intent(mContext,ApplyAfterSalesActivity.class);
            bundle = new Bundle();
            bundle.putSerializable("orderdetails",orderdetails);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onTraceClickListener(int orderId,String code,String name,String number) {
        Intent intent = new Intent(mContext,OrderTraceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("code",code);
        bundle.putString("orderId",String.valueOf(orderId));
        bundle.putString("number",number);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onGetTicketListener(int position, ListBean orderdetails) {
        LogUtils.json("---> 当前日期 " + DateTimeUtils.getCurrentDay());
        if(10 <= DateTimeUtils.getCurrentDay()
                &&DateTimeUtils.getCurrentDay()<=15){
            Intent intent = new Intent(getActivity(),InvoiceInfosActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("orderid",String.valueOf(orderdetails.getId()));
            bundle.putString("totalPrice",String.valueOf(orderdetails.getTotalcost()));
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            showDialog();
        }
    }

    private void showDialog() {
        int dialogWith = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext,42);
        new MDDialog.Builder(mContext)
                .setContentView(R.layout.invoice_info)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .setBackgroundCornerRadius(15)
                .setWidthMaxDp((int) DensityUtils.px2dp(mContext, dialogWith))
                .setShowTitle(false)
                .setShowButtons(false)
                .create()
                .show();
    }

    @OnClick(R.id.layoutGoLook)
    public void onClick() {
        Intent intent = new Intent(getActivity(), SpecialPriceActivity.class);
        getActivity().startActivity(intent);
        AppManager.getAppManager().finishActivity(MyOrderManage.orderManageContext);
    }
}
