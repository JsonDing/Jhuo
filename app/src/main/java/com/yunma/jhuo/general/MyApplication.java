package com.yunma.jhuo.general;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yunma.dao.GreenDaoManager;
import com.yunma.emchat.DemoHelper;

import org.xutils.x;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created on 2016-11-24.
 * @author Json
 */

public class MyApplication extends MultiDexApplication {

    public static Context applicationContext;
    public static String currentUserNick = "";
    public static MyApplication mInstance;
    private static UploadManager uploadManager;
    // 记录是否已经初始化
    private boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        applicationContext = this;
        initImageLoader(getApplicationContext());
        ZXingLibrary.initDisplayOpinion(this);
        initShareSDK();
        initXutils();
        initEchat();
        initQiniu();
        initGreenDao();
    }

    public MyApplication() {

    }

    private void initGreenDao() {
        GreenDaoManager.getInstance();
    }

    private void initShareSDK() {
        ShareSDK.initSDK(this,"4bf73946acaea429df012dd0e80e7a2a");
        SMSSDK.initSDK(this, "19a73c9b27b2c", "828cb1b774f3d693d1dd5156cbeee2fc");
        //   SMSSDK.initSDK(this, "f3fc6baa9ac4", "7f3dedcb36d92deebcb373af921d635a");
    }

    private void initXutils() {
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }

    private void initQiniu() {
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        uploadManager = new UploadManager(config);
    }

    public static UploadManager getUploadManager(){
        return  uploadManager;
    }

    private void initEchat() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null ||
                !processAppName.equalsIgnoreCase(applicationContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        DemoHelper.getInstance().init(applicationContext);
    }

    private String getAppName(int pid) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static Context getAppContext(){
       return applicationContext;
    }

    private void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(600, 600) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(4) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

}
