package com.yunma.bean;

/**
 * Created on 2017-03-23
 *
 * @author Json.
 */

public class ExcelBean {
    private String phone;
    private String passwd;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "phone='" + phone + '\'' + ", passwd='" + passwd + '\'' ;
    }
}
