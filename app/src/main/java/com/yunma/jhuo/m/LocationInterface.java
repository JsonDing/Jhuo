package com.yunma.jhuo.m;

import android.content.Context;
import com.amap.api.services.core.PoiItem;
import java.util.List;

/**
 * Created by Json on 2017/1/2.
 */

public class LocationInterface {

    public interface LocationModel {
        void startLocate(Context mContext, OnLocateListener onLocateListener);
        void stopLocate();
        void destroyLocation();
    }

    public interface LocationView {
        Context getContext();
        void getLocation(List<PoiItem> poiItems);
        void getCurrAddress(String address);
        
    }

    public interface OnLocateListener {
        void getAddress(String address);
        void setLocation(List<PoiItem> poiItems);
    }


}
