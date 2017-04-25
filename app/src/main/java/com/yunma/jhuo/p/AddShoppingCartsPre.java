package com.yunma.jhuo.p;

import com.yunma.bean.*;
import com.yunma.jhuo.i.AddShoppingCartsImpl;
import com.yunma.jhuo.m.AddShoppingCartsInterface.*;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class AddShoppingCartsPre {
    private AddShoppingCartsModel mModel;
    private AddShoppingCartsView mView;

    public AddShoppingCartsPre(AddShoppingCartsView mView) {
        this.mView = mView;
        this.mModel = new AddShoppingCartsImpl();
    }

    public void addToBasket(){
        mModel.addToBasket(mView.getContext(), mView.getShoppingCartsBean(), new OnAddShoppingCartsListener() {
            @Override
            public void onListener(SuccessResultBean resultBean, String msg) {
                mView.showAddShoppingCartsInfos(resultBean,msg);
            }
        });
    }

    public void addToBasket(ShoppingCartsBean shoppingCartsBean){
        mModel.addToBasket(mView.getContext(), shoppingCartsBean, new OnAddShoppingCartsListener() {
            @Override
            public void onListener(SuccessResultBean resultBean, String msg) {
                mView.showAddShoppingCartsInfos(resultBean,msg);
            }
        });
    }
}
