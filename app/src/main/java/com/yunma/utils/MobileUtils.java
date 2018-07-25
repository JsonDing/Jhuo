package com.yunma.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;

import com.yunma.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;


/**
 * Created on 2017-03-24
 *
 * @author Json.
 */

public class MobileUtils {

    /**
     * @return 手机品牌
     */
    public static String getMobileBrand(){
        return Build.BRAND;
    }

    /**
     * @return 手机型号
     */
    public static String getMobileModel(){
        return Build.MODEL;
    }


    /**
     * @return 手机SDK版本号
     */
    public static int getMobileSDK(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * @return 手机系统版本号
     */
    public static String getMobileOSVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     *
     * @param context
     * @return
     */
    public String getVersion(Context context) {
        String version = "0.0.0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    public static List<ContactBean> getPhoneNumberFromMobile(Context context) {
        // TODO Auto-generated constructor stub
        List<ContactBean> contactBeanList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if (cursor != null) {
            //moveToNext方法返回的是一个boolean类型的数据
            while (cursor.moveToNext()) {
                //读取通讯录的姓名
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //读取通讯录的号码
                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactBean phoneInfo = new ContactBean(name, number);
                contactBeanList.add(phoneInfo);
            }
            cursor.close();
        }
        return contactBeanList;
    }
}
