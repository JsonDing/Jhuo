package com.yunma.jhuo.general;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yunma.dao.GreenDaoManager;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;

import org.xutils.x;

import java.io.File;


/**
 * Created on 2016-11-24.
 * @author Json
 */

public class MyApplication extends MultiDexApplication {

    public static Context applicationContext;
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
        initXutils();
       // initEchat();
        initQiniu();
        initGreenDao();
        initBaidu();
        initUmeng();
    }

    private void initBaidu() {
        //SDKInitializer.initialize(getApplicationContext());
    }


    private void initGreenDao() {
        GreenDaoManager.getInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initUmeng() {
        PlatformConfig.setWeixin("wx129574b4dc257e3d", "21a80ff308c20b40568c53ca31175a7c");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        UMShareAPI.get(this);
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
                .build();
        uploadManager = new UploadManager(config);
    }

    public static UploadManager getUploadManager(){
        return  uploadManager;
    }

    public static Context getAppContext(){
       return applicationContext;
    }

    private void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(
                        600, 600)
                .threadPoolSize(4) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(
                        context, 5 * 1000, 30 * 1000))
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        LogUtils.json("Application: onLowMemory");
        System.gc();
        GlideUtils.glidClearMemory(this);
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        LogUtils.json("Application: onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }



}
