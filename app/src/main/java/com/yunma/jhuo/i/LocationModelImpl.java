package com.yunma.jhuo.i;

import android.content.Context;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.yunma.jhuo.m.LocationInterface.*;
import com.yunma.serves.MyLocationListener;
import com.yunma.utils.LogUtils;

/**
 * Created on 2016-12-09.
 * @author Json
 */
public class LocationModelImpl implements LocationModel{

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption mOption = new AMapLocationClientOption();
    @Override
    public void startLocate(Context mContext, OnLocateListener onLocateListener) {
        init(mContext,onLocateListener);
        locationClient.startLocation();
    }

    @Override
    public void stopLocate() {
        if (null != locationClient) {
            locationClient.stopLocation();
            LogUtils.json("stopLocation() --->");
        }
    }

    @Override
    public void destroyLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            mOption = null;
            LogUtils.json("destroyLocation() --->");
        }
    }

    private void init(Context mContext, OnLocateListener onLocateListener) {
        MyLocationListener mLocationListener = new MyLocationListener(mContext,onLocateListener);
        // 初始化client
        locationClient = new AMapLocationClient(mContext);
        // 设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(mLocationListener);
    }

    private AMapLocationClientOption getDefaultOption() {
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        return mOption;
    }

}
