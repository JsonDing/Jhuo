package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yunma.R;
import com.yunma.adapter.SelectTicketsAdapter;
import com.yunma.bean.InvoiceBean;
import com.yunma.jhuo.general.MyActivity;
import com.yunma.jhuo.m.InvoiceInterface;
import com.yunma.jhuo.p.InvoicesPre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

public class SelectTickets extends MyActivity implements InvoiceInterface.GetInvoiceView,
        SelectTicketsAdapter.OnSelectClick {

    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutSure) LinearLayout layoutSure;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvInvoice) ListView lvInvoice;
    @BindView(R.id.layoutNull) LinearLayout layoutNull;
    private Context mContext;
    private int selectPos = -1;
    private InvoicesPre invoicesPre = null;
    private SelectTicketsAdapter mAdapter;
    private List<InvoiceBean.SuccessBean.ListBean> listBean = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tickets);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initStatusBar();
        getInvoice();
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(SelectTickets.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    private void getInvoice() {
        invoicesPre = new InvoicesPre(SelectTickets.this);
        invoicesPre.getInvoices(mContext,"12",1);
    }

    @OnClick({R.id.layoutBack, R.id.layoutSure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutSure:
                if(listBean != null&&selectPos!=-1){
                    Intent intent=new Intent();
                    intent.putExtra("tickBean", listBean.get(selectPos));
                    setResult(1,intent);
                    AppManager.getAppManager().finishActivity(this);
                }else{
                    ToastUtils.showError(mContext,"请选择发票");
                }
                break;
        }
    }


    @Override
    public void toShowInvoice(InvoiceBean resultBean, String msg) {
        if (resultBean == null) {
            layoutNull.setVisibility(View.VISIBLE);
            lvInvoice.setVisibility(View.GONE);
            ToastUtils.showError(mContext, msg);
        } else {
            if (resultBean.getSuccess().getList().size() != 0) {
                layoutNull.setVisibility(View.GONE);
                lvInvoice.setVisibility(View.VISIBLE);
                listBean = resultBean.getSuccess().getList();
                for(int i=0;i<resultBean.getSuccess().getList().size();i++){
                    if (!listBean.get(i).getAuth().equals("1")){
                        listBean.remove(i);
                    }
                }
                if(listBean.size()!=0){
                    if (mAdapter == null) {
                        mAdapter = new SelectTicketsAdapter(this, listBean,this);
                        lvInvoice.setAdapter(mAdapter);
                    }else{
                        mAdapter.setListBean(listBean);
                        mAdapter.notifyDataSetChanged();
                    }
                }else {
                    layoutNull.setVisibility(View.VISIBLE);
                    lvInvoice.setVisibility(View.GONE);
                }
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                lvInvoice.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSelectClick(int position,
                              List<InvoiceBean.SuccessBean.ListBean> listBean) {
        selectPos = position;
        listBean.get(position).setSelect(1);
        mAdapter.setListBean(listBean);
    }

}
