package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.MyOrderManage;
import com.yunma.adapter.OrderWaitToSendAdapter;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.jhuo.m.OrderWaitToSendInterface;
import com.yunma.jhuo.p.OrderWaitToSendPre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToSend extends Fragment implements OrderWaitToSendInterface.OrderWaitToSendView {
    @BindView(R.id.lvWaitToSend)
    ListView lvWaitToSend;
    @BindView(R.id.layoutNull)
    LinearLayout layoutNull;
    @BindView(R.id.layoutGoLook)
    RelativeLayout layoutGoLook;
    private Context mContext;
    private OrderWaitToSendAdapter mAdapter = null;
    private OrderWaitToSendPre sendPre = null;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> listBean = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_wait_to_send, container, false);
        ButterKnife.bind(this, view);
        getDatas();
        return view;
    }

    private void getDatas() {
        sendPre = new OrderWaitToSendPre(OrderWaitToSend.this);
        sendPre.getUnSendOrders();
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
    public void showOrderInfos(OrderUnPayResultBean resultBean, String msg) {

        if (resultBean == null) {
            ToastUtils.showError(getContext(), msg);
        } else {
            if (EmptyUtil.isEmpty(resultBean.getSuccess().getList())) {
                lvWaitToSend.setVisibility(View.GONE);
                layoutNull.setVisibility(View.VISIBLE);
            } else {
                lvWaitToSend.setVisibility(View.VISIBLE);
                layoutNull.setVisibility(View.GONE);
                listBean = resultBean.getSuccess().getList();
                if (listBean != null) {
                    if (mAdapter == null) {
                        mAdapter = new OrderWaitToSendAdapter(mContext, listBean);
                        lvWaitToSend.setAdapter(mAdapter);
                    } else {
                        mAdapter.setListBean(listBean);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && sendPre != null) {
            sendPre.getUnSendOrders();
        }
    }

    @OnClick(R.id.layoutGoLook)
    public void onClick() {
        Intent intent = new Intent(getContext(), SpecialPriceActivity.class);
        getContext().startActivity(intent);
        AppManager.getAppManager().finishActivity(MyOrderManage.orderManageContext);
    }
}
