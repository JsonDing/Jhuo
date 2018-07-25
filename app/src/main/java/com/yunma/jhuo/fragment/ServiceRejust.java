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
import com.yunma.adapter.GoodsReturnRejustAdapter;
import com.yunma.bean.ServiceResultBean;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.ReturnDetialActivity;
import com.yunma.jhuo.m.GoodsRefundInterface;
import com.yunma.jhuo.m.ServiceInterface;
import com.yunma.jhuo.p.GoodsServicePre;
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

public class ServiceRejust extends Fragment implements ServiceInterface.GetServiceView,
        GoodsRefundInterface.OnRejeckClick {
    @BindView(R.id.lvRebackGoodsList) ListView lvRebackGoodsList;
    @BindView(R.id.layoutNull) View layoutNull;
    @BindView(R.id.layoutGoLook) View layoutGoLook;
    private Context mContext;
    private GoodsReturnRejustAdapter mAdapter;
    private GoodsServicePre goodsServicePre = null;
    private List<ServiceResultBean.SuccessBean.ListBean> listBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_not_handle, container, false);
        ButterKnife.bind(this, view);
        getDatas();
        return view;
    }

    private void getDatas() {
        goodsServicePre = new GoodsServicePre(this);
        goodsServicePre.getService(mContext,"-1");
        layoutGoLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SpecialPriceActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
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
                    mAdapter = new GoodsReturnRejustAdapter(mContext,ServiceRejust.this,listBean);
                    lvRebackGoodsList.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }else{
                lvRebackGoodsList.setVisibility(View.GONE);
                layoutNull.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            goodsServicePre.getService(mContext,"-1");
        }
    }

    @Override
    public void onLookDetial(ServiceResultBean.SuccessBean.ListBean listBean) {
        Intent intent = new Intent(mContext,ReturnDetialActivity.class);
        intent.putExtra("goodsDetial", listBean);
        mContext.startActivity(intent);
    }

    @Override
    public void onRejectReason() {
        ToastUtils.showConfusing(mContext,"您提交的相关信息，不符合退货条件，请重新提交");
    }
}
