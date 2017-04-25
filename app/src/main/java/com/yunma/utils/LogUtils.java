package com.yunma.utils;

import android.util.Log;

/**
 * Created by Json on 2016/4/7.
 */
public class LogUtils {
    private static final boolean isDebug = true;
    public static void  log (Object o){
        if(isDebug){
            String tag = "MyLog";
            String  msg = String.valueOf(o);
            if (tag == null || tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;

            int segmentSize = 3600;
            long length = msg.length();
            if (length <= segmentSize ) {// 长度小于等于限制直接打印
                Log.i(tag, msg);
            }else {
                while (msg.length() > segmentSize ) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize );
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }
                Log.i(tag, msg);// 打印剩余日志
            }
        }
    }
}
