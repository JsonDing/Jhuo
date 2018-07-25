package com.yunma.dao;

import org.greenrobot.greendao.annotation.*;

/**
 * Created on 2017-01-21
 *
 * @author Json.
 */
@Entity
public class UserInfos {
    @Id(autoincrement = false)
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
    @Property(nameInDb = "qq")
    private String qq;
    @Property(nameInDb = "weChat")
    private String weChat;
    @Property(nameInDb = "roleName")
    private String roleName;
    @Property(nameInDb = "roleId")
    private int roleId;
    @Property(nameInDb = "points")
    private int points;
    @Generated(hash = 512044703)
    public UserInfos(Long Id, Long userId, String phoneNumber, String passWd,
            String imgsPhotos, boolean isAutoLogin, String nickName, String gender,
            String realName, String qq, String weChat, String roleName, int roleId,
            int points) {
        this.Id = Id;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.passWd = passWd;
        this.imgsPhotos = imgsPhotos;
        this.isAutoLogin = isAutoLogin;
        this.nickName = nickName;
        this.gender = gender;
        this.realName = realName;
        this.qq = qq;
        this.weChat = weChat;
        this.roleName = roleName;
        this.roleId = roleId;
        this.points = points;
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
    public String getQq() {
        return this.qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getWeChat() {
        return this.weChat;
    }
    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public int getRoleId() {
        return this.roleId;
    }
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    public int getPoints() {
        return this.points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    
}
