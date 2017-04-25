package com.yunma.jhuo.p;

import com.yunma.bean.*;
import com.yunma.jhuo.i.DeleteShoppingCartImpl;
import com.yunma.jhuo.m.GetShoppingCartListInterface.*;
/**
 * Created on 2017-03-02
 *
 * @author Json.
 */

public class DeleteShoppingCartPre {
    private DelShoppingCartModel mModel;
    private DelShoppingCartView mView;

    public DeleteShoppingCartPre(DelShoppingCartView mView) {
        this.mView = mView;
        this.mModel = new DeleteShoppingCartImpl();
    }

    public void delGoods(){
        mModel.delShoppingCartList(mView.getContext(), mView.getDelId(), new OnDelShoppingCart() {
            @Override
            public void onDelShoppingCartListener(SuccessResultBean resultBean, String msg) {
                mView.showDelInfos(resultBean,msg);
            }
        });
    }
}
