package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-03-20
 *
 * @author Json.
 */

public class InvoiceInterface {

    public interface AddInvoiceModel{
        void addInvoice(Context mContext,AddInvoicesBean addBean,AddInvoiceListener onListener);
    }

    public interface AddInvoiceView{
        void toShowInvoice(SuccessResultBean resultBean,String msg);
    }

    public interface AddInvoiceListener{
        void onListener(SuccessResultBean resultBean, String msg);
    }

    public interface GetInvoiceModel{
        void getInvoice(Context mContext,String nums,int page,GetInvoiceListener onListener);
    }

    public interface GetInvoiceView{
        void toShowInvoice(InvoiceBean resultBean,String msg);
    }

    public interface GetInvoiceListener{
        void onListener(InvoiceBean resultBean,String msg);
    }

    public interface EditInvoiceModel{
        void editInvoice(Context mContext,AddInvoicesBean addBean,EditInvoiceListener onListener);
    }

    public interface EditInvoiceView{
        void toShowEditInvoice(SuccessResultBean resultBean,String msg);
    }

    public interface EditInvoiceListener{
        void onListener(SuccessResultBean resultBean,String msg);
    }

    public interface DelInvoiceModel{
        void getInvoice(Context mContext,DeleteBean deleteBean,DelInvoiceListener onListener);
    }

    public interface DelInvoiceView{
        void toShowDelInvoice(SuccessResultBean resultBean,String msg);
    }

    public interface DelInvoiceListener{
        void onListener(SuccessResultBean resultBean,String msg);
    }
}

