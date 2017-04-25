package com.yunma.jhuo.fragment;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;

import com.yunma.R;
import com.yunma.jhuo.activity.mine.OrderTrace;
import com.yunma.adapter.OrderHadConfirmAdapter;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.jhuo.m.OrderHadConfirmInterFace.OnAdapterClick;

import butterknife.*;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderHadConfirm  extends Fragment implements OnAdapterClick{
    @BindView(R.id.lvHadConfirm)
    ListView lvHadConfirm;
    private Context mContext;
    private OrderHadConfirmAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_had_confirm, container, false);
        ButterKnife.bind(this, view);
        getDatas();
        setDatas();
        return view;
    }

    private void getDatas() {

    }

    private void setDatas() {
        mAdapter = new OrderHadConfirmAdapter(mContext,OrderHadConfirm.this);
        lvHadConfirm.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onDeleteClickListener(int position,String id) {
     //   ToastUtils.showSuccess(getActivity().getApplicationContext(),"删除第" + position + "个订单");
    }

    @Override
    public void onApplyClickListener(int position, OrderUnPayResultBean.SuccessBean.ListBean orderdetails, int totalNums) {

    }


    @Override
    public void onTraceClickListener(int orderId,String code,String name,String number) {
        Intent intent = new Intent(mContext,OrderTrace.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onGetTicketListener(int position, OrderUnPayResultBean.SuccessBean.ListBean orderdetails) {

    }
}
