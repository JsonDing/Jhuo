package com.yunma.jhuo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.jhuo.activity.mine.*;
import com.yunma.adapter.GoodsReturnCompleteAdapter;
import com.yunma.bean.ServiceResultBean;
import com.yunma.bean.ServiceResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.m.GoodsRefundInterface.OnCompleteClick;
import com.yunma.jhuo.m.ServiceInterface.GetServiceView;
import com.yunma.jhuo.p.GoodsServicePre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class ServiceComplete extends Fragment implements GetServiceView,OnCompleteClick {
    @BindView(R.id.lvRebackGoodsList) ListView lvRebackGoodsList;
    @BindView(R.id.layoutNull) View layoutNull;
    @BindView(R.id.layoutGoLook) View layoutGoLook;
    private Context mContext;
    private GoodsReturnCompleteAdapter mAdapter;
    private GoodsServicePre goodsServicePre = null;
    private List<ListBean> listBean;
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
        goodsServicePre.getService(mContext,"3");
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
                    mAdapter = new GoodsReturnCompleteAdapter(mContext,ServiceComplete.this,listBean);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onLookDetial(ListBean listBean) {
        Intent intent = new Intent(mContext,ReturnDetialActivity.class);
        intent.putExtra("goodsDetial", listBean);
        mContext.startActivity(intent);
    }

    @Override
    public void onRefundStatus(ListBean listBean) {
        Intent intent = new Intent(mContext,ReturnProgressActivity.class);
        intent.putExtra("goodsDetial", listBean);
        mContext.startActivity(intent);
    }

    @Override
    public void onLookMoney(ListBean listBean) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            goodsServicePre.getService(mContext,"3");
        }
    }
}
