package com.yunma.bean;

/**
 * Created on 2017-02-21
 *
 * @author Json.
 */

public class AddAddressBean {

    private String token;
    private String name;
    private String tel;
    private String regoin;
    private String addr;
    private int used;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}
