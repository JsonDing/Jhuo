package com.yunma.utils;

import android.content.Context;
import android.net.*;
import android.view.Gravity;

import com.gitonway.lee.niftynotification.lib.Configuration;

/**
 * Created on 2017-03-30
 *
 * @author Json.
 */

public class NetUtil {
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }


    public static Configuration initConf() {
        Configuration cfg = new Configuration.Builder()
                .setAnimDuration(800)
                .setDispalyDuration(4000)
                .setBackgroundColor("#ffffff")
                .setTextColor("#efaf14")
                .setIconBackgroundColor("#ffffff")
                .setTextPadding(5)
                .setViewHeight(44)
                .setTextLines(2)
                .setTextGravity(Gravity.CENTER)
                .build();

        return cfg;
    }
}
