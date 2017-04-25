package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-13
 *
 * @author Json.
 */

public class VolumeResultBean {
    private int status;
    private List<SuccessBean> success;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public class SuccessBean {
        /**
            优惠券发送id
            优惠券id
            持优惠券人id
            发送优惠券时间
            优惠券状态：1=过期，0=未过期
            优惠券类型（1包邮 2限件包邮 3不限件包邮 4限消费商品 5不限消费商品）
            优惠券名称
            门槛
            抵用金额
         */

        private int id;
        private int couponId;
        private int userId;
        private long date;
        private int status;
        private int day;
        private int type;
        private String name;
        private int astrict;
        private double money;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAstrict() {
            return astrict;
        }

        public void setAstrict(int astrict) {
            this.astrict = astrict;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }


}
