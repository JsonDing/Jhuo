package com.yunma.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;
import java.util.Stack;

import static com.yunma.jhuo.general.MyApplication.applicationContext;

/**
 * Created by Json on 2017/4/3.
 */

public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        for (Activity act : activityStack) {
            if (!act.getClass().equals(activity.getClass())) {
                activityStack.add(activity);
            }
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
        getCurrentStack();
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        LogUtils.json("结束所有MainActivity之外的activity");
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            GlideUtils.glidClearMemory(applicationContext);
            GlideUtils.glidClearDisk(applicationContext);
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception ignored) {

        }
    }

    /**
     * 结束指定的Activity
     */
    public void getActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    private void getCurrentStack(){
        assert activityStack != null;
        LogUtils.json("当前栈中Activity数量：" + activityStack.size());
        for(Activity activity : activityStack){
            LogUtils.json("当前栈中Activity: " + activity);
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 启动第三方 app

     * @param mcontext
     * @param packagename
     */
    public void doStartApplicationWithPackageName (Context mcontext, String packagename) {

        // 通过包名获取此 APP 详细信息，包括 Activities、 services 、versioncode 、 name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mcontext.getPackageManager().getPackageInfo(packagename, 0 );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace() ;
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为 CATEGORY_LAUNCHER 的该包名的 Intent
        Intent resolveIntent = new Intent(Intent. ACTION_MAIN, null) ;
        resolveIntent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK ) ;
        resolveIntent.addCategory(Intent. CATEGORY_LAUNCHER );
        resolveIntent.setPackage(packageinfo. packageName );

        // 通过 getPackageManager()的 queryIntentActivities 方法遍历
        List<ResolveInfo> resolveinfoList = mcontext.getPackageManager()
                .queryIntentActivities(resolveIntent , 0) ;

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null ) {
            // packagename = 参数 packname
            String packageName = resolveinfo.activityInfo . packageName;
            // 这个就是我们要找的该 APP 的LAUNCHER 的 Activity[组织形式： packagename.mainActivityname]
            String className = resolveinfo. activityInfo .name ;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent. ACTION_MAIN) ;
            intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK ) ;
            intent.addCategory(Intent. CATEGORY_LAUNCHER );

            // 设置 ComponentName参数 1:packagename 参数2:MainActivity 路径
            ComponentName cn = new ComponentName(packageName , className) ;

            intent.setComponent(cn) ;
            mcontext.startActivity(intent) ;
        }
    }

}
