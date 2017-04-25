package com.yunma.jhuo.p;

import com.amap.api.services.core.PoiItem;
import com.yunma.jhuo.i.LocationModelImpl;
import com.yunma.jhuo.m.LocationInterface.*;

import java.util.List;

/**
 * Created on 2016-12-09.
 * @author Json
 */

public class LocationPre {
    private LocationModel locationModel;
    private LocationView lovationView;

    public LocationPre(LocationView lovationView) {
        this.lovationView = lovationView;
        this.locationModel = new LocationModelImpl();
    }

    public void getLocation(){
        locationModel.startLocate(lovationView.getContext(), new OnLocateListener() {
            @Override
            public void getAddress(String address) {
                lovationView.getCurrAddress(address);
            }

            @Override
            public void setLocation(List<PoiItem> poiItems) {
                lovationView.getLocation(poiItems);

            }
        });
    }

    public void stopLocation(){
        locationModel.stopLocate();
    }

    public void destroyLocation(){
        locationModel.destroyLocation();
    }

}
