package com.yunma.jhuo.activity.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.district.*;
import com.yunma.R;
import com.yunma.jhuo.general.MyCompatActivity;
import com.yunma.utils.AppManager;
import com.yunma.utils.ToastUtils;

import java.util.*;

import butterknife.*;

public class SelectAddress extends MyCompatActivity implements AdapterView.OnItemSelectedListener,
        DistrictSearch.OnDistrictSearchListener {

    public static final String COUNTRY = "country"; // 行政区划，国家级
    public static final String PROVINCE = "province"; // 行政区划，省级
    public static final String CITY = "city"; // 行政区划，市级
    public static final String DISTRICT = "district"; // 行政区划，区级
    //当前选中的级别
    private String selectedLevel = COUNTRY;
    // 当前行政区划
    private DistrictItem currentDistrictItem = null;
    // 下级行政区划集合
    private Map<String, List<DistrictItem>> subDistrictMap = new HashMap<>();
    // 省级列表
    private List<DistrictItem> provinceList = new ArrayList<>();
    // 市级列表
    private List<DistrictItem> cityList = new ArrayList<>();
    // 区县级列表
    private List<DistrictItem> districtList = new ArrayList<>();
    private String strProvince = "";
    private String strCity = "";
    private String strDistrict = "";
    // 是否已经初始化
    private boolean isInit = false;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.sProvince)
    Spinner sProvince;
    @BindView(R.id.sCity)
    Spinner sCity;
    @BindView(R.id.sDistrict)
    Spinner sDistrict;
    @BindView(R.id.layout)
    View layout;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initNavigationBar();
        initSpanner();
    }

    private void initSpanner() {
        sProvince.setOnItemSelectedListener(this);
        sCity.setOnItemSelectedListener(this);
        sDistrict.setOnItemSelectedListener(this);
        init();
    }

    private void init() {
        // 设置行政区划查询监听
        DistrictSearch districtSearch = new DistrictSearch(this);
        districtSearch.setOnDistrictSearchListener(this);
        // 查询中国的区划
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("中国");
        districtSearch.setQuery(query);
        // 异步查询行政区
        districtSearch.searchDistrictAsyn();
    }

    private void initNavigationBar() {
        mContext = this;
    }

    @OnClick({R.id.view, R.id.imgClose})
    public void onClick(View view) {
        Intent mIntent;
        switch (view.getId()) {
            case R.id.view:
                view.setVisibility(View.INVISIBLE);
                mIntent = new Intent();
                mIntent.putExtra("address", strProvince + strCity + strDistrict);
                // 设置结果，并进行传送
                this.setResult(2, mIntent);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_out,0);
                break;
            case R.id.imgClose:
                view.setVisibility(View.INVISIBLE);
                mIntent = new Intent();
                mIntent.putExtra("address", strProvince + strCity + strDistrict);
                // 设置结果，并进行传送
                this.setResult(2, mIntent);
                AppManager.getAppManager().finishActivity(this);
                overridePendingTransition(R.anim.bottom_out,0);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DistrictItem districtItem = null;
        switch (parent.getId()) {
            case R.id.sProvince:
                districtItem = provinceList.get(position);
                selectedLevel = PROVINCE;
                strProvince = null;
                strProvince = getDistrictInfoStr(districtItem);
                break;
            case R.id.sCity:
                selectedLevel = CITY;
                districtItem = cityList.get(position);
                strCity = null;
                strCity = getDistrictInfoStr(districtItem);
                break;
            case R.id.sDistrict:
                selectedLevel = DISTRICT;
                districtItem = districtList.get(position);
                strDistrict = null;
                strDistrict = getDistrictInfoStr(districtItem);
                break;
            default:
                break;
        }
        if (districtItem != null) {
            currentDistrictItem = districtItem;
            // 先查缓存如果缓存存在则直接从缓存中查找，无需再执行查询请求
            List<DistrictItem> cache = subDistrictMap.get(districtItem
                    .getAdcode());
            if (null != cache) {
                setSpinnerView(cache);
            } else {
                querySubDistrict(districtItem);

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDistrictSearched(DistrictResult result) {
        List<DistrictItem> subDistrictList  = null;
        if (result != null) {
            if (result.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {

                List<DistrictItem> district = result.getDistrict();

                if (!isInit) {
                    isInit = true;
                    currentDistrictItem = district.get(0);
                }

                // 将查询得到的区划的下级区划写入缓存
                for (int i = 0; i < district.size(); i++) {
                    DistrictItem districtItem = district.get(i);
                    subDistrictMap.put(districtItem.getAdcode(),
                            districtItem.getSubDistrict());
                }
                // 获取当前区划的下级区划列表
                subDistrictList = subDistrictMap
                        .get(currentDistrictItem.getAdcode());
                for(int i= 0;i<subDistrictList.size();i++){
					Log.i("--------->", "subDistrictList --------------->"
							+ subDistrictList.get(i).getName());
				}
            } else {
                ToastUtils.showShort(mContext,result.getAMapException().getErrorCode() + "");
            }
        }
        setSpinnerView(subDistrictList);
    }

    private void setSpinnerView(List<DistrictItem> subDistrictList) {
        List<String> nameList = new ArrayList<>();
        if(subDistrictList != null && subDistrictList.size() > 0){
            for (int i = 0; i < subDistrictList.size(); i++) {
                nameList.add(subDistrictList.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.simple_spinner_item, nameList);

            if (selectedLevel.equalsIgnoreCase(COUNTRY)) {
                provinceList = subDistrictList;
                sProvince.setAdapter(adapter);
            }

            if (selectedLevel.equalsIgnoreCase(PROVINCE)) {
                cityList = subDistrictList;
                sCity.setAdapter(adapter);
            }

            if (selectedLevel.equalsIgnoreCase(CITY)) {
                districtList = subDistrictList;
                //如果没有区县，将区县说明置空
                if(nameList.size() <= 0){
                    //  tv_districtInfo.setText("");
                }
                sDistrict.setAdapter(adapter);
            }
        } else {
            List<String> emptyNameList = new ArrayList<>();
            ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, emptyNameList);
            if (selectedLevel.equalsIgnoreCase(COUNTRY)) {
                sProvince.setAdapter(emptyAdapter);
                sCity.setAdapter(emptyAdapter);
                sDistrict.setAdapter(emptyAdapter);
            }

            if (selectedLevel.equalsIgnoreCase(PROVINCE)) {
                sCity.setAdapter(emptyAdapter);
                sDistrict.setAdapter(emptyAdapter);
            }

            if (selectedLevel.equalsIgnoreCase(CITY)) {
                sDistrict.setAdapter(emptyAdapter);
            }
        }

    }

    /**
     * 获取行政区划的信息字符串
     * @param districtItem
     * @return
     */
    private String getDistrictInfoStr(DistrictItem districtItem){
        StringBuffer sb = new StringBuffer();
        String name = districtItem.getName();
       /* String adcode = districtItem.getAdcode();
        String level = districtItem.getLevel();
        String citycode = districtItem.getCitycode();
        LatLonPoint center = districtItem.getCenter();*/
        sb.append( name );
        /*sb.append("区域编码:" + adcode + "\n");
        sb.append("城市编码:" + citycode + "\n");
        sb.append("区划级别:" + level + "\n");*/
       /* if (null != center) {
            sb.append("经纬度:(" + center.getLongitude() + ", "
                    + center.getLatitude() + ")\n");
        }*/
        return sb.toString();
    }

    /**
     * 查询下级区划
     *
     * @param districtItem
     *            要查询的区划对象
     */
    private void querySubDistrict(DistrictItem districtItem) {
        DistrictSearch districtSearch = new DistrictSearch(SelectAddress.this);
        districtSearch.setOnDistrictSearchListener(SelectAddress.this);
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(districtItem.getName());
        districtSearch.setQuery(query);
        districtSearch.searchDistrictAsyn();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            view.setVisibility(View.INVISIBLE);
            Intent mIntent = new Intent();
            mIntent.putExtra("address", strProvince + strCity + strDistrict);
            // 设置结果，并进行传送
            this.setResult(2, mIntent);
            AppManager.getAppManager().finishActivity(this);
            overridePendingTransition(R.anim.bottom_out,0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
