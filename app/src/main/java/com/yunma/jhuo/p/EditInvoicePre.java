package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.EditInvoiceModelImpl;
import com.yunma.jhuo.m.InvoiceInterface;

/**
 * Created on 2017-03-21
 *
 * @author Json.
 */

public class EditInvoicePre {
    private InvoiceInterface.EditInvoiceModel editModel;
    private InvoiceInterface.EditInvoiceView editView;

    public EditInvoicePre(InvoiceInterface.EditInvoiceView mView) {
        this.editView = mView;
        this.editModel = new EditInvoiceModelImpl();
    }

    //修改
    public void editInvoice(Context mContext, AddInvoicesBean addInvoicesBean){
        editModel.editInvoice(mContext, addInvoicesBean, new InvoiceInterface.EditInvoiceListener() {
            @Override
            public void onListener(SuccessResultBean resultBean, String msg) {
                editView.toShowEditInvoice(resultBean,msg);
            }
        });
    }
}
