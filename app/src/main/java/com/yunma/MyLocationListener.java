package com.yunma;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.*;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yunma.jhuo.m.LocationInterface.OnLocateListener;
import com.yunma.utils.LogUtils;

import java.util.List;
/**
 * Created on 2016-12-14.
 * @author Json
 */

public class MyLocationListener implements AMapLocationListener,PoiSearch.OnPoiSearchListener {
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp = null;
    private OnLocateListener onLocateListener;
    private Context mContext;
    public MyLocationListener(Context mContext, OnLocateListener onLocateListener) {
        this.onLocateListener = onLocateListener;
        this.mContext = mContext;
    }
    @Override
    public void onLocationChanged(AMapLocation location) {
        if(null!=location){
            if(location.getErrorCode() == 0){
                onLocateListener.getAddress(location.getCountry() + location.getProvince()
                        + location.getCity() + location.getDistrict());
                LogUtils.log("定位成功：---> " + location.getAddress());
                lp = new LatLonPoint(location.getLatitude(),location.getLongitude());
                doSearchQuery(lp,location.getAdCode());
            } else {
                //定位失败
                LogUtils.log("定位失败：---> \n" + "错误码    : " + location.getErrorCode() + "\n" +
                        "错误信息  : " + location.getErrorInfo() + "\n" + "错误描述  : " +
                        location.getLocationDetail());
            }
        }else {
            LogUtils.log("location：---> location = null" );
        }

    }

    private void doSearchQuery(LatLonPoint lp, String city) {
        int currentPage = 0;
        query = new PoiSearch.Query("", "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        if(lp !=null&&city!= null){
            PoiSearch poiSearch = new PoiSearch(mContext, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 500, true));//
            // 设置搜索区域为以lp点为圆心，其周围500米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    List<PoiItem> poiItems = result.getPois();
                    onLocateListener.setLocation(poiItems);
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
