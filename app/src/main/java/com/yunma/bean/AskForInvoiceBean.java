package com.yunma.bean;

/**
 * Created on 2017-03-29
 *
 * @author Json.
 */

public class AskForInvoiceBean {

    private String token;
    private String addrid;
    private String name;
    private String orderid;
    private String type;
    private String vatid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddrid() {
        return addrid;
    }

    public void setAddrid(String addrid) {
        this.addrid = addrid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVatid() {
        return vatid;
    }

    public void setVatid(String vatid) {
        this.vatid = vatid;
    }

    @Override
    public String toString() {
        return "AskForInvoiceBean{" +
                "token='" + token + '\'' +
                ", addrid='" + addrid + '\'' +
                ", name='" + name + '\'' +
                ", orderid='" + orderid + '\'' +
                ", type='" + type + '\'' +
                ", vatid='" + vatid + '\'' +
                '}';
    }
}
