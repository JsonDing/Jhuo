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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private List<OrderdetailsBean> orderdetails;

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

    public List<OrderdetailsBean> getOrderdetails() {
        return orderdetails;
    }

    public void setOrderdetails(List<OrderdetailsBean> orderdetails) {
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
