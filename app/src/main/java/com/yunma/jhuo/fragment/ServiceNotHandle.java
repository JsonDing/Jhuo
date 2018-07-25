package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yunma.R;
import com.yunma.adapter.GoodsReturnNotHandleAdapter;
import com.yunma.bean.ServiceResultBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.ReturnDetialActivity;
import com.yunma.jhuo.m.GoodsRefundInterface.DelRefundView;
import com.yunma.jhuo.m.GoodsRefundInterface.OnUnHandleClick;
import com.yunma.jhuo.m.ServiceInterface.GetServiceView;
import com.yunma.jhuo.p.GoodsServicePre;
import com.yunma.jhuo.p.RefundPre;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class ServiceNotHandle extends Fragment implements GetServiceView,OnUnHandleClick, DelRefundView {
    @BindView(R.id.lvRebackGoodsList) ListView lvRebackGoodsList;
    @BindView(R.id.layoutNull) View layoutNull;
    @BindView(R.id.layoutGoLook) View layoutGoLook;
    private Context mContext;
    private GoodsReturnNotHandleAdapter mAdapter;
    private GoodsServicePre goodsServicePre = null;
    private List<ServiceResultBean.SuccessBean.ListBean> listBean;
    private RefundPre refundPre = null;
    private int delId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_not_handle, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        getDatas();
        return view;
    }

    private void getDatas() {
        goodsServicePre = new GoodsServicePre(ServiceNotHandle.this);
        goodsServicePre.getService(mContext,"0");
        refundPre = new RefundPre(ServiceNotHandle.this);
        layoutGoLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SpecialPriceActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void toShowGetInfos(ServiceResultBean resultBean, String msg) {
        if (EmptyUtil.isEmpty(resultBean)){
            ToastUtils.showError(mContext,msg);
            lvRebackGoodsList.setVisibility(View.GONE);
            layoutNull.setVisibility(View.VISIBLE);
        }else{
            if(EmptyUtil.isNotEmpty(resultBean.getSuccess().getList())){
                lvRebackGoodsList.setVisibility(View.VISIBLE);
                layoutNull.setVisibility(View.GONE);
                listBean = resultBean.getSuccess().getList();
                if(mAdapter==null){
                    mAdapter = new GoodsReturnNotHandleAdapter(mContext,ServiceNotHandle.this,listBean);
                    lvRebackGoodsList.setAdapter(mAdapter);
                }else{
                    mAdapter.setListBean(listBean);
                    mAdapter.notifyDataSetChanged();
                }
            }else{
                lvRebackGoodsList.setVisibility(View.GONE);
                layoutNull.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 查看退货详情
     * @param listBean
     */
    @Override
    public void onLookDetial(ServiceResultBean.SuccessBean.ListBean listBean) {
        Intent intent = new Intent(mContext,ReturnDetialActivity.class);
        intent.putExtra("goodsDetial", listBean);
        mContext.startActivity(intent);
    }

    /**
     * 取消退货
     * @param ids
     */
    @Override
    public void onCancleRefund(int position,String ids) {
        setDelId(position);
        refundPre.delRefund(mContext,ids);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            goodsServicePre.getService(mContext,"0");
        }
    }

    @Override
    public void showDelInfo(SuccessResultBean resultBean, String msg) {
        if (resultBean==null){
            ToastUtils.showError(mContext,msg);
        }else {
            ToastUtils.showSuccess(mContext, msg);
            mAdapter.delRefund(getDelId());
        }
    }

    public int getDelId() {
        return delId;
    }

    public void setDelId(int delId) {
        this.delId = delId;
    }
}
