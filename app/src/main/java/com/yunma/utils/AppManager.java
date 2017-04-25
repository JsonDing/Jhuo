package com.yunma.utils;

import android.app.Activity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            GlideUtils.glidClearMemory(applicationContext);
            GlideUtils.glidClearDisk(applicationContext);
            EMClient.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    LogUtils.log("EMchat 退出成功！");
                }

                @Override
                public void onError(int i, String s) {
                    LogUtils.log("EMchat 退出失败！");
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
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
        LogUtils.log("当前栈中Activity数量：" + activityStack.size());
        for(Activity activity : activityStack){
            LogUtils.log("当前栈中Activity: " + activity);
        }
    }
}
