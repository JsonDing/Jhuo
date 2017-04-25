package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.*;
import com.yunma.jhuo.m.InvoiceInterface.*;

/**
 * Created on 2017-03-20
 *
 * @author Json.
 */

public class InvoicesPre {
    private GetInvoiceModel getModel;
    private GetInvoiceView getView;
    private AddInvoiceModel addModel;
    private AddInvoiceView addView;
    private DelInvoiceModel delModel;
    private DelInvoiceView delView;
    public InvoicesPre(GetInvoiceView mView) {
        this.getView = mView;
        this.getModel = new GetInvoiceModelImpl();
    }

    public InvoicesPre(AddInvoiceView mView) {
        this.addView = mView;
        this.addModel = new AddInvoiceImpl();
    }

    public InvoicesPre(DelInvoiceView mView) {
        this.delView = mView;
        this.delModel = new DelInvoiceModelImp();
    }

    //获取
    public void getInvoices(Context mContext,String nums,int page){
        getModel.getInvoice(mContext, nums,page,new GetInvoiceListener() {
            @Override
            public void onListener(InvoiceBean resultBean, String msg) {
                getView.toShowInvoice(resultBean,msg);
            }
        });
    }

    //添加
    public void addInvoice(Context mContext, AddInvoicesBean addInvoicesBean){
        addModel.addInvoice(mContext, addInvoicesBean, new AddInvoiceListener() {
            @Override
            public void onListener(SuccessResultBean resultBean, String msg) {
                addView.toShowInvoice(resultBean,msg);
            }
        });
    }

    //删除
    public void delInvoice(Context mContext, DeleteBean deleteBean){
       delModel.getInvoice(mContext, deleteBean, new DelInvoiceListener() {
           @Override
           public void onListener(SuccessResultBean resultBean, String msg) {
               delView.toShowDelInvoice(resultBean,msg);
           }
       });
    }

}
