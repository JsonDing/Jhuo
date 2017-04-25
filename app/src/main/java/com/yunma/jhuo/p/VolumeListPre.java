package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.VolumeResultBean;
import com.yunma.jhuo.i.VolumeListModelImpl;
import com.yunma.jhuo.m.VolumeListInterface;

/**
 * Created on 2017-04-11
 *
 * @author Json.
 */

public class VolumeListPre {
    private VolumeListInterface.VolumeListModel mModel;
    private VolumeListInterface.VolumeListView mView;

    public VolumeListPre(VolumeListInterface.VolumeListView mView) {
        this.mView = mView;
        this.mModel = new VolumeListModelImpl();
    }

    public void getUnuseVolumeList(Context mContext){
        mModel.getUnuseVolumeList(mContext, new VolumeListInterface.UnUsestListener() {
            @Override
            public void onUnUseVolumeList(VolumeResultBean resultBean, String msg) {
                mView.showUnuseVolumeList(resultBean,msg);
            }
        });
    }

    public void getTimeOutVolumeList(Context mContext){
        mModel.getTimeOutVolumeList(mContext,new VolumeListInterface.TimeOutListener() {
            @Override
            public void onTimeOutVolumeList(VolumeResultBean resultBean, String msg) {
                mView.showTimeOutVolumeList(resultBean,msg);
            }
        });
    }
}
