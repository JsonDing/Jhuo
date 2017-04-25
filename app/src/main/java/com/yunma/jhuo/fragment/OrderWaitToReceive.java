package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.*;
import com.yunma.adapter.OrderWaitToReceiveAdapter;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.m.CancleOrderInterface.CancleOrderView;
import com.yunma.jhuo.m.OrderHadConfirmInterFace.OnAdapterClick;
import com.yunma.jhuo.m.OrderWaitToReceiveInterface.OrderWaitToReceiveView;
import com.yunma.jhuo.p.CancleOrderPre;
import com.yunma.jhuo.p.OrderWaitToReceivePre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;
import cn.carbs.android.library.MDDialog;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToReceive  extends Fragment implements
        OrderWaitToReceiveView, OnAdapterClick,CancleOrderView {
    @BindView(R.id.lvWaitToReceive)
    ListView lvWaitToReceive;
    @BindView(R.id.layoutGoLook)
    RelativeLayout layoutGoLook;
    @BindView(R.id.layoutNull)
    LinearLayout layoutNull;
    private Context mContext;
    private OrderWaitToReceiveAdapter mAdapter;
    private OrderWaitToReceivePre receivePre = null;
    private CancleOrderPre cancleOrderPre = null;
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
        receivePre = new OrderWaitToReceivePre(this);
        receivePre.getUnReceiveOrders();
        cancleOrderPre = new CancleOrderPre(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }


    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void showCancleInfos(SuccessResultBean resultBean, String msg) {
        if (resultBean == null) {
            ToastUtils.showError(getContext(), msg);
        } else {
            ToastUtils.showSuccess(getContext(), msg);
            receivePre.getUnReceiveOrders();
        }
    }

    @Override
    public void showOrderInfos(OrderUnPayResultBean resultBean, String msg) {
        if(resultBean == null){
            ToastUtils.showError(getContext(),msg);
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
             //   .setContentView(R.layout.wheelview_add_size)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancleOrderPre.cancleOrder(id);
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
            intent = new Intent(mContext,ApplySingleGoodsAftermarket.class);
            bundle = new Bundle();
            bundle.putSerializable("orderdetails",orderdetails.getOrderdetails().get(0));
        }else{
            intent = new Intent(mContext,ApplyMoreGoodsAftermarket.class);
            bundle = new Bundle();
            bundle.putSerializable("orderdetails",orderdetails);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onTraceClickListener(int orderId,String code,String name,String number) {
        Intent intent = new Intent(mContext,OrderTrace.class);
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
        LogUtils.log("---> 当前日期 " + DateTimeUtils.getCurrentDay());
        if(10 <= DateTimeUtils.getCurrentDay()
                &&DateTimeUtils.getCurrentDay()<=15){
            Intent intent = new Intent(getContext(),InvoiceInfos.class);
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
        Intent intent = new Intent(getContext(), SpecialPriceActivity.class);
        getContext().startActivity(intent);
        AppManager.getAppManager().finishActivity(MyOrderManage.orderManageContext);
    }
}
