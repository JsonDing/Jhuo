package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.ExpressTracesBean;
import com.yunma.jhuo.i.ExpressTracesModeImpl;
import com.yunma.jhuo.m.ExpressTracesInterface;

/**
 * Created on 2017-03-17
 *
 * @author Json.
 */

public class ExpressTracesPre {
    private ExpressTracesInterface.ExpressTracesMode mModel;
    private ExpressTracesInterface.ExpressTracesView mView;

    public ExpressTracesPre(ExpressTracesInterface.ExpressTracesView mView) {
        this.mView = mView;
        this.mModel = new ExpressTracesModeImpl();
    }

    public void getExpressTraces(Context context,String code,String number){
        mModel.getExpressTraces(context, code, number, new ExpressTracesInterface.Onlistener() {
            @Override
            public void toShowExpressTraces(ExpressTracesBean expressTracesBean, String msg) {
                mView.toShowExpressTraces(expressTracesBean,msg);
            }
        });
    }
}
