package com.yunma.dao;

import org.greenrobot.greendao.annotation.*;

/**
 * Created by Json on 2017/2/1.
 */
@Entity
public class AppInfo {
    @Id
    private Long id;
    @Property(nameInDb = "isFirst")
    private int isFirst;//0:未显示过展示页；1：显示过
    @Property(nameInDb = "isLogin")
    private int isLogin;//判断用户当前是否在在线，0：不在线；1：在线
    @Property(nameInDb = "token")
    private String token;//用户token值，有效期1年
    @Property(nameInDb = "isFirstSetting")
    private int isFirstSetting;
    @Generated(hash = 1777763566)
    public AppInfo(Long id, int isFirst, int isLogin, String token,
            int isFirstSetting) {
        this.id = id;
        this.isFirst = isFirst;
        this.isLogin = isLogin;
        this.token = token;
        this.isFirstSetting = isFirstSetting;
    }
    @Generated(hash = 1656151854)
    public AppInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getIsFirst() {
        return this.isFirst;
    }
    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }
    public int getIsLogin() {
        return this.isLogin;
    }
    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getIsFirstSetting() {
        return this.isFirstSetting;
    }
    public void setIsFirstSetting(int isFirstSetting) {
        this.isFirstSetting = isFirstSetting;
    }
   
    
}
