package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class GoodsCollectInterFace {

    public interface GetGoodsCollectModel{
        void GetCollect(Context mContext,OnGetCollect onGetCollect);
    }

    public interface GetGoodsCollectView{
        void onGetCollectShow(GetCollectResultBean resultBean, String msg);
    }

    public interface OnGetCollect{
        void onGetCollectListener(GetCollectResultBean resultBean, String msg);
    }




    public interface AddGoodsCollectModel{
        void addToCollect(Context mContext,String goodId,OnAddCollect onAddCollect);
    }

    public interface AddGoodsCollectView{
        void onAddCollectShow(AddGoodsCollectBean resultBean,String msg);
    }

    public interface OnAddCollect{
        void onAddCollectListener(AddGoodsCollectBean resultBean,String msg);
    }




    public interface DelGoodsCollectModel{
        void delCollect(Context mContext,String goodId,OnDelCollect onDelCollect);
    }

    public interface DelGoodsCollectView{
        void onDelCollectShow(SuccessResultBean resultBean, String msg);
    }

    public interface OnDelCollect{
        void onDelCollectListener(SuccessResultBean resultBean, String msg);
    }


    public interface IfcollectModel{
        void  ifCollect(Context mContext,String goodsId,IfcollectListener onListener);
    }
    public interface IfcollectView{
        void showCollectInfos(AddGoodsCollectBean resultBean,String msg);
    }
    public interface IfcollectListener{
        void onListener(AddGoodsCollectBean resultBean,String msg);
    }
}
