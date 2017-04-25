package com.yunma.bean;

import java.io.Serializable;

/**
 * Created on 2017-02-21
 *
 * @author Json.
 */

public class ModifyAddressBean implements Serializable{

    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjYxLCJpYXQiOjE0ODY1NDE3MDEyMDcsImV4dCI6MTQ4ODAxMjkzMDEzNX0.xo6OeW07BNaBLgyygaHYhJrKr5uoQPBlLFVG8x-ib_Y
     * id : 4
     * name : 老刘
     * tel : 654321
     * regoin : 台湾
     * addr : 中国
     */

    private String token;
    private int id;
    private String name;
    private String tel;
    private String regoin;
    private String addr;
    private int used;

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRegoin() {
        return regoin;
    }

    public void setRegoin(String regoin) {
        this.regoin = regoin;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
