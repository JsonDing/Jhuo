package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.ExpressTracesBean;

/**
 * Created on 2017-03-17
 *
 * @author Json.
 */

public class ExpressTracesInterface {
    public interface ExpressTracesMode{
        void getExpressTraces(Context mContext,String code,String number,Onlistener onlistener);
    }

    public interface ExpressTracesView{
        void toShowExpressTraces(ExpressTracesBean expressTracesBean, String msg);
    }
    public interface Onlistener{
        void toShowExpressTraces(ExpressTracesBean expressTracesBean,String msg);
    }
}
