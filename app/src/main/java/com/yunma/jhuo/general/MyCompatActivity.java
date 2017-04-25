package com.yunma.jhuo.general;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.yunma.dao.AppInfo;
import com.yunma.dao.GreenDaoManager;
import com.yunma.greendao.AppInfoDao;
import com.yunma.serves.NetBroadcastReceiver;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2016-11-24.
 * @author Json
 */

public abstract class MyCompatActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt{
    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    public int netMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
       // MyApplication.addActivity(this);
        AppManager.getAppManager().addActivity(this);
        //注册一个监听连接状态的listener
        evevt = this;
        inspectNet();
    }
    /**
     * 初始化时判断有没有网络
     */
    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(MyCompatActivity.this);
        if (netMobile == 1) {
            LogUtils.log("当前网络：连接wifi");
        } else if (netMobile == 0) {
            LogUtils.log("当前网络:连接移动数据");
        } else if (netMobile == -1) {
            LogUtils.log("当前网络:当前没有网络");
        }
        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;
        }
        return false;
    }

    public void hideSoftInput(){
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
    }

    public boolean isLogin() {
        List<AppInfo> appInfos = getDao().loadAll();
        return appInfos.size() != 0 && appInfos.get(0).getIsLogin() != 0;
    }

    private AppInfoDao getDao() {
        return GreenDaoManager.getInstance().getSession().getAppInfoDao();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideUtils.glidClearMemory(this);
    }
}
