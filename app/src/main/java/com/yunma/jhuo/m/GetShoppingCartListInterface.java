package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class GetShoppingCartListInterface {
    public interface GetShoppingCartModel{
        void getShoppingCartList(Context mContext,OnGetShoppingCart onGetShoppingCart);
    }

    public interface GetShoppingCartView{
        void showShoppingCartList(GetShoppingListBean resultBean, String msg);
    }

    public interface OnGetShoppingCart{
        void onGetShoppingCartListener(GetShoppingListBean resultBean, String msg);
    }

    public interface DelShoppingCartModel{
        void delShoppingCartList(Context mContext,String delId,OnDelShoppingCart onDelShoppingCart);
    }

    public interface DelShoppingCartView{
        Context getContext();
        String getDelId();
        void showDelInfos(SuccessResultBean resultBean, String msg);
    }

    public interface OnDelShoppingCart{
        void onDelShoppingCartListener(SuccessResultBean resultBean, String msg);
    }

}
