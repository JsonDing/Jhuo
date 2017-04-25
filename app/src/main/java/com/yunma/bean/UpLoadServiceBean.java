package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class UpLoadServiceBean {

    private String token;
    private String oid;
    private String remark;
    private String pic;
    private List<ServicesBean> services;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<ServicesBean> getServices() {
        return services;
    }

    public void setServices(List<ServicesBean> services) {
        this.services = services;
    }

    public class ServicesBean {

        private String orderdetailid;
        private String num;
        private String pic;
        private String reason;
        private String remark;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getOrderdetailid() {
            return orderdetailid;
        }

        public void setOrderdetailid(String orderdetailid) {
            this.orderdetailid = orderdetailid;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
