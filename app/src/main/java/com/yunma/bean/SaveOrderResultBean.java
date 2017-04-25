package com.yunma.bean;

import java.util.Objects;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class SaveOrderResultBean {

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {

        private int id;
        private long date;
        private long payDate;
        private int userId;
        private String name;
        private String addr;
        private String tel;
        private Objects phone;
        private String expressname;
        private String expressnumber;
        private double expresscost;
        private double totalmoney;
        private double totalcost;
        private int totalnum;
        private Object pay;
        private Object send;
        private Object service;
        private Object userorderid;
        private Object orderdetails;
        private Object express;
        private Object repo;
        private Object user;
        private Object yunma;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public Objects getPhone() {
            return phone;
        }

        public void setPhone(Objects phone) {
            this.phone = phone;
        }

        public String getExpressname() {
            return expressname;
        }

        public void setExpressname(String expressname) {
            this.expressname = expressname;
        }

        public String getExpressnumber() {
            return expressnumber;
        }

        public void setExpressnumber(String expressnumber) {
            this.expressnumber = expressnumber;
        }

        public double getExpresscost() {
            return expresscost;
        }

        public void setExpresscost(double expresscost) {
            this.expresscost = expresscost;
        }

        public double getTotalmoney() {
            return totalmoney;
        }

        public void setTotalmoney(double totalmoney) {
            this.totalmoney = totalmoney;
        }

        public double getTotalcost() {
            return totalcost;
        }

        public void setTotalcost(double totalcost) {
            this.totalcost = totalcost;
        }

        public int getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(int totalnum) {
            this.totalnum = totalnum;
        }

        public Object getPay() {
            return pay;
        }

        public void setPay(Object pay) {
            this.pay = pay;
        }

        public Object getSend() {
            return send;
        }

        public void setSend(Object send) {
            this.send = send;
        }

        public Object getService() {
            return service;
        }

        public void setService(Object service) {
            this.service = service;
        }

        public Object getUserorderid() {
            return userorderid;
        }

        public void setUserorderid(Object userorderid) {
            this.userorderid = userorderid;
        }

        public Object getOrderdetails() {
            return orderdetails;
        }

        public void setOrderdetails(Object orderdetails) {
            this.orderdetails = orderdetails;
        }

        public Object getExpress() {
            return express;
        }

        public void setExpress(Object express) {
            this.express = express;
        }

        public Object getRepo() {
            return repo;
        }

        public void setRepo(Object repo) {
            this.repo = repo;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public Object getYunma() {
            return yunma;
        }

        public void setYunma(Object yunma) {
            this.yunma = yunma;
        }
    }
}
