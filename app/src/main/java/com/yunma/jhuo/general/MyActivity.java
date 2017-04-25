package com.yunma.jhuo.general;

import android.app.Activity;
import android.os.*;
import android.view.*;

import com.yunma.serves.NetBroadcastReceiver;
import com.yunma.utils.*;

/**
 * Created on 2016-11-25.
 * @author Json
 */

public abstract class MyActivity extends Activity implements NetBroadcastReceiver.NetEvevt {
    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 网络类型
     */
    private int netMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        evevt = this;
        inspectNet();
    }

    /**
     * 初始化时判断有没有网络
     */
    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(MyActivity.this);
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

}
