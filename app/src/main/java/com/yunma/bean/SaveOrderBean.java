package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class SaveOrderBean {

    private String token;
    private String addrid;
    private String remark;
    private String couponUserid;
    private List<OrderDetailsBean> orderdetails;

    public String getCouponUserid() {
        return couponUserid;
    }

    public void setCouponUserid(String couponUserid) {
        this.couponUserid = couponUserid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    public List<OrderDetailsBean> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<OrderDetailsBean> orderdetails) {
        this.orderdetails = orderdetails;
    }

    @Override
    public String toString() {
        return "SaveOrderBean{" +
                "token='" + token + '\'' +
                ", addrid='" + addrid + '\'' +
                ", orderdetails=" + orderdetails +
                '}';
    }
}
