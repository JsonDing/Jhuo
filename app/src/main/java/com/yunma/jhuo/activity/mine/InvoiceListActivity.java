package com.yunma.jhuo.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.yunma.R;
import com.yunma.adapter.InvoiceListAdapter;
import com.yunma.bean.InvoiceBean;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.jhuo.m.InvoiceInterface;
import com.yunma.jhuo.p.InvoicesPre;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

public class InvoiceListActivity extends MyCompatActivity implements
        InvoiceInterface.GetInvoiceView, AdapterView.OnItemClickListener {
    private static final int ADD_SUCCESS = 0;
    private static final int EDIT_SUCCESS = 1;
    private static final int DEL_SUCCESS = 2;
    private static final int DETIAL_REQUEST = 4;
    @BindView(R.id.layoutBack) LinearLayout layoutBack;
    @BindView(R.id.layoutAdd) LinearLayout layoutAdd;
    @BindView(R.id.layouStatusBar) LinearLayout layouStatusBar;
    @BindView(R.id.lvInvoice) ListView lvInvoice;
    @BindView(R.id.layoutNull) View layoutNull;
    private Context mContext;
    private InvoicesPre invoicesPre = null;
    private InvoiceListAdapter mAdapter;
    private List<InvoiceBean.SuccessBean.ListBean> listBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initStatusBar();
        getInvoice();
    }

    private void getInvoice() {
        lvInvoice.setOnItemClickListener(this);
        invoicesPre = new InvoicesPre(this);
        invoicesPre.getInvoices(mContext,"12",1);
    }

    private void initStatusBar() {
        mContext = this;
        int statusHeight = ScreenUtils.getStatusHeight(InvoiceListActivity.this);
        layouStatusBar.setPadding(0, statusHeight, 0, 0);
    }

    @OnClick({R.id.layoutBack, R.id.layoutAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutBack:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.layoutAdd:
                Intent intent = new Intent(InvoiceListActivity.this, AddInvoiceActivity.class);
                startActivityForResult(intent, ADD_SUCCESS);
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
                if (mAdapter == null) {
                    mAdapter = new InvoiceListAdapter(this, listBean);
                    lvInvoice.setAdapter(mAdapter);
                }else{
                    mAdapter.setListBean(listBean);
                    mAdapter.notifyDataSetChanged();
                }
            }else{
                layoutNull.setVisibility(View.VISIBLE);
                lvInvoice.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_SUCCESS){
            if(resultCode == RESULT_OK){
                invoicesPre.getInvoices(mContext,"12",1);
            }
        }else if(requestCode == DETIAL_REQUEST){
            if(resultCode == EDIT_SUCCESS){
                invoicesPre.getInvoices(mContext,"12",1);
            }else if(resultCode == DEL_SUCCESS){
                int position = data.getIntExtra("position",-1);
                listBean.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(InvoiceListActivity.this,InvoiceDetials.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putSerializable("invoiceDetials",listBean.get(position));
        intent.putExtras(bundle);
        startActivityForResult(intent,DETIAL_REQUEST);
    }

}
