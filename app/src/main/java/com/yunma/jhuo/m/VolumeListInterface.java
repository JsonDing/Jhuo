package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.VolumeResultBean;

/**
 * Created on 2017-04-11
 *
 * @author Json.
 */

public class VolumeListInterface {

    public interface VolumeListModel{
        void getUnuseVolumeList(Context mContext,
                                UnUsestListener onVolumeListListener);
        void getTimeOutVolumeList(Context mContext,
                                  TimeOutListener onVolumeListListener);
    }

    public interface VolumeListView{
        void showUnuseVolumeList(VolumeResultBean resultBean,String msg);
        void showTimeOutVolumeList(VolumeResultBean resultBean,String msg);
    }

    public interface UnUsestListener {
        void onUnUseVolumeList(VolumeResultBean resultBean, String msg);
    }

    public interface TimeOutListener {
        void onTimeOutVolumeList(VolumeResultBean resultBean,String msg);
    }
}
