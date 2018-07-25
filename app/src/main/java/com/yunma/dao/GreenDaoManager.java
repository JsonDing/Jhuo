package com.yunma.dao;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.yunma.greendao.DaoMaster;
import com.yunma.greendao.DaoSession;
import com.yunma.greendao.MySQLiteOpenHelper;
import com.yunma.jhuo.general.MyApplication;

/**
 * Created by hp on 2017/1/25.
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance; //单例

    private GreenDaoManager(){
        if (mInstance == null) {
           /* DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(MyApplication.getAppContext(), "Jhuo_db", null);*///此处为自己需要处理的表
            MigrationHelper.DEBUG = false;
            MySQLiteOpenHelper devOpenHelper = new
                    MySQLiteOpenHelper(MyApplication.getAppContext(), "Jhuo_db", null);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {//保证异步处理安全操作

                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}
