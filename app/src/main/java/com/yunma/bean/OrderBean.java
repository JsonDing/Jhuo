package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class OrderBean implements Serializable {

    private String token;
    private String addrid;
    private String yunma;
    private List<OrderDetailsBean> orderdetails;

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

    public String getYunma() {
        return yunma;
    }

    public void setYunma(String yunma) {
        this.yunma = yunma;
    }

    public List<OrderDetailsBean> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<OrderDetailsBean> orderdetails) {
        this.orderdetails = orderdetails;
    }

}
