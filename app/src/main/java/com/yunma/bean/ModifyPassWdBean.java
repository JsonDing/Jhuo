package com.yunma.bean;

/**
 * Created on 2017-02-15
 *
 * @author Json.
 */

public class ModifyPassWdBean {

    private String token;
    private String newpass;
    private String oldpass;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }

    public String getOldpass() {
        return oldpass;
    }

    public void setOldpass(String oldpass) {
        this.oldpass = oldpass;
    }
}
