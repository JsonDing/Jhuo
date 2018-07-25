package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class AddShoppingCartsInterface {
    public interface AddShoppingCartsModel{
        void addToBasket(Context mContext,ShoppingCartsBean shoppingCartsBean,OnAddShoppingCartsListener onListener);
    }
    public interface AddShoppingCartsView{
        void showAddShoppingCartsInfos(SuccessResultBean resultBean,String msg);
    }
    public interface OnAddShoppingCartsListener{
        void onListener(SuccessResultBean resultBean,String msg);
    }
}
