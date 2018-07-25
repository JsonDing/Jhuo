package com.yunma.utils;

import android.util.Log;

/**
 * Created by Json on 2016/4/7.
 */
public class LogUtils {
    private static final boolean isDebug = true;
    public static void json(Object o){
        if(isDebug){
            String tag = "JsonData";
            String msg = String.valueOf(o);
            if (tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;
            int segmentSize = 3600;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                Log.i(tag, msg);
            } else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }
                Log.i(tag, msg);// 打印剩余日志
            }
        }
    }

    public static void test(Object o){
        if(isDebug){
            String tag = "TestData";
            String msg = String.valueOf(o);
            if (tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;
            Log.i(tag, msg);
        }
    }

    public static void urlRequest(String logTitle,Object o){
        if(isDebug){
            String tag = "URL";
            String msg = String.valueOf(o);
            if (tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;
            Log.i(tag,logTitle + "网络请求：" + msg);
        }
    }
}
