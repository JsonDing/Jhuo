package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.GetShoppingCartImpl;
import com.yunma.jhuo.m.GetShoppingCartListInterface.*;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class GetShoppingCartPre {
    private GetShoppingCartModel mModel;
    private GetShoppingCartView mView;

    public GetShoppingCartPre(GetShoppingCartView mView) {
        this.mView = mView;
        this.mModel = new GetShoppingCartImpl();
    }

    public void getShoppingCartList(Context mContext){
        mModel.getShoppingCartList(mContext,new OnGetShoppingCart() {
            @Override
            public void onGetShoppingCartListener(GetShoppingListBean resultBean, String msg) {
                mView.showShoppingCartList(resultBean,msg);
            }
        });
    }
}
