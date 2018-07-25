package com.yunma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "ShareData";
    private static final String STRING ="J·货";
    private static final Long LONG = -1L;
    private static final float FLOAT = -1f;
    private static final int INT = -1;
    private static final boolean BOOLEAN = true;
    private static final String DOUBLE = "0.00";

    /**
     * 清除缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }


    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    //  获取文件大小
    //  Context.getExternalFilesDir() -->
    //  SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //  Context.getExternalCacheDir() -->
    //  SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    /**
     * 保存List
     */
    public <T> void setDataList(Context context,String tag, List<T> datalist) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 获取List
     */
    public <T> List<T> getDataList(Context context,String tag) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        List<T> datalist= new ArrayList<>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else
        {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public static String getString(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key, STRING);
    }

    public static int getInt(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, INT);
    }

    public static float getFloat(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, FLOAT);
    }

    public static float getLong(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getLong(key, LONG);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod()
        {
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        static void apply(SharedPreferences.Editor editor) {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException
                    | InvocationTargetException e) {
                LogUtils.json("--- --- > " + e.getMessage());
            }
            editor.commit();
        }
    }

    /**
     * 用户ID/推荐码
     */
    public static void setUserId(Context context,String value){
        put(context,"userId",value);
    }

    public static String getUserId(Context context){
        return String.valueOf(get(context,"userId",STRING));
    }
    /**
     * 用户手机号码/登录帐号
     */
    public static void setPhoneNumber(Context context,String value){
        put(context,"phoneNumber",value);
    }

    public static String getPhoneNumber(Context context){
        return String.valueOf(get(context,"phoneNumber",STRING));
    }

    /**
     * 登录密码
     */
    public static void setPassWd(Context context,String value){
        put(context,"passWd",value);
    }

    public static String getPassWd(Context context){
        return String.valueOf(get(context,"passWd",STRING));
    }
    /**
     * 用户Token值
     */
    public static void setToken(Context context,String value){
        put(context,"token",value);
    }

    public static String getToken(Context context){
        return String.valueOf(get(context,"token",STRING));
    }

    /**
     * 用户角色
     */
    public static void setRole(Context context,String value){
        put(context,"role",value);
    }

    public static String getRole(Context context){
        return String.valueOf(get(context,"role",STRING));
    }

    /**
     * 用户角色Id
     */
    public static void setRoleId(Context context,int value){
        put(context,"roleId",value);
    }

    public static int getRoleId(Context context){
        return (int) get(context,"roleId",INT);
    }

    /*****************************************************/

                    /* agent info */

    public static void setIsAnget(Context context,boolean value){
        put(context,"isAgent",value);
    }

    public static boolean isAgent(Context context){
        return (boolean) get(context,"isAgent",false);
    }

    public static void setAgentId(Context context,int value){
        put(context,"agentId",value);
    }

    public static int getAgentId(Context context){
        return (int) get(context,"agentId",INT);
    }

    public static void setAgentDiscount(Context context,String value){
        put(context,"agentDiscount",value);
    }

    public static String getAgentDiscount(Context context){
        return (String) get(context,"agentDiscount",DOUBLE);
    }

    public static void setAgentName(Context context,String value){
        put(context,"agentName",value);
    }

    public static String getAgentName(Context context){
        return (String) get(context,"agentName",STRING);
    }

    public static void setAgentNick(Context context,String value){
        put(context,"agentNick",value);
    }

    public static String getAgentNick(Context context){
        return (String) get(context,"agentNick",STRING);
    }

    public static void setParentDiscount(Context context,String value){
        put(context,"parentDiscount",value);
    }

    public static String getParentDiscount(Context context){
        return (String) get(context,"parentDiscount",DOUBLE);
    }

    public static void setRootDiscount(Context context,String value){
        put(context,"rootDiscount",value);
    }

    public static String getRootDiscount(Context context){
        return (String) get(context,"rootDiscount",DOUBLE);
    }

    public static void setAgentPoints(Context context,int value){
        put(context,"agentPoints",value);
    }

    public static int getAgentPoints(Context context){
        return (int) get(context,"agentPoints",INT);
    }
    /*****************************************************/

    /**
     * 手机状态栏高度
     */
    public static void setStatusHeight(Context context,int value){
        put(context,"statusBar",value);
    }

    public static int getStatusHeight(Context context){
        return (int)get(context,"statusBar",INT);
    }

    /**
     * 货品上架提醒
     */
    public static void setSaleRemind(Context context,boolean value){
        put(context,"SaleRemind",value);
    }

    public static boolean isSaleRemind(Context context){
        return (boolean) get(context,"SaleRemind",BOOLEAN);
    }

    /**
     * 添加购物车是否提醒
     */
    public static void setAddCartRemind(Context context,boolean value){
        put(context,"AddCart",value);
    }

    public static boolean isAddCartRemind(Context context){
        return (boolean) get(context,"AddCart",BOOLEAN);
    }


    /**
     * 分享货品是否提醒
     */
    public static void setShareRemind(Context context,boolean value){
        put(context,"share",value);
    }

    public static boolean isShareRemind(Context context){
        return (boolean) get(context,"share",true);
    }

    /**
     * 是否显示“我的”数字角标
     */
    public static void setIsShowNumberRemind(Context context,boolean value){
        put(context,"NumberRemind",value);
    }

    public static boolean IsShowNumberRemind(Context context){
        return (boolean) get(context,"NumberRemind",true);
    }

    /**
     * 微信订单ID
     */
    public static void setWeChatOrderId(Context context,String value){
        put(context,"WeChatOrderId",value);
    }

    public static String  getWeChatOrderId(Context context){
        return  String.valueOf(get(context,"WeChatOrderId",STRING));
    }

    public static void setWeChatOrderPay(Context context,String value){
        put(context,"WeChatOrderPay",value);
    }

    public static String  getWeChatOrderPay(Context context){
        return  String.valueOf(get(context,"WeChatOrderPay",STRING));
    }

    /**
     * 删除购物车货品ID
     */
    public static void setDelCartGoodsIds(Context context,String value){
        put(context,"delCartGoodsIds",value);
    }

    public static String getDelCartGoodsIds(Context context){
        return  String.valueOf(get(context,"delCartGoodsIds",STRING));
    }


    public static void setIntegral(Context context,int value){
        put(context,"integral",value);
    }

    public static int getIntegral(Context context){
        return (int)get(context,"integral",0);
    }

    /**
     * 设置纬度
     */
    public static void setLatitude(Context context,String value){
        put(context,"latitude",value);
    }

    public static String getLatitude(Context context){
        return String.valueOf(get(context,"latitude",STRING));
    }

    /**
     * 设置经度
     */
    public static void setLongitude(Context context,String value){
        put(context,"longitude",value);
    }

    public static String getLongitude(Context context){
        return String.valueOf(get(context,"longitude",STRING));
    }

    // 上一次兑换返利时间戳
    public static void setPreReturnTime(Context context,long value){
        put(context,"returnTime",value);
    }

    public static Long getPreReturnTime(Context context){
        return (Long) get(context,"returnTime",LONG);
    }


}
