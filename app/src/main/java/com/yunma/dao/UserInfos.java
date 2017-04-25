package com.yunma.dao;

import org.greenrobot.greendao.annotation.*;

/**
 * Created on 2017-01-21
 *
 * @author Json.
 */
@Entity
public class UserInfos {
    @Id
    private Long Id;
    @Property(nameInDb = "userId")
    private Long userId;
    @Property(nameInDb = "PhoneNumber")
    private String phoneNumber;
    @Property(nameInDb = "passWd")
    private String passWd;
    @Property(nameInDb = "imgsPhotos")
    private String imgsPhotos;
    @Property(nameInDb = "isAutoLogin")
    private boolean isAutoLogin;
    @Property(nameInDb = "nickName")
    private String nickName;
    @Property(nameInDb = "gender")
    private String gender;
    @Property(nameInDb = "realName")
    private String realName;

    @Generated(hash = 459055021)
    public UserInfos(Long Id, Long userId, String phoneNumber, String passWd,
            String imgsPhotos, boolean isAutoLogin, String nickName, String gender,
            String realName) {
        this.Id = Id;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.passWd = passWd;
        this.imgsPhotos = imgsPhotos;
        this.isAutoLogin = isAutoLogin;
        this.nickName = nickName;
        this.gender = gender;
        this.realName = realName;
    }
    @Generated(hash = 2018014889)
    public UserInfos() {
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPassWd() {
        return this.passWd;
    }
    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }
    public String getImgsPhotos() {
        return this.imgsPhotos;
    }
    public void setImgsPhotos(String imgsPhotos) {
        this.imgsPhotos = imgsPhotos;
    }
    public boolean getIsAutoLogin() {
        return this.isAutoLogin;
    }
    public void setIsAutoLogin(boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getRealName() {
        return this.realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
}
