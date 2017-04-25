package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-03-15
 *
 * @author Json.
 */

public class SeparateOrderBean implements Serializable{

    private List<OrderdetailsBean> orderdetailsBeen;

    public List<OrderdetailsBean> getOrderdetailsBeen() {
        return orderdetailsBeen;
    }

    public void setOrderdetailsBeen(List<OrderdetailsBean> orderdetailsBeen) {
        this.orderdetailsBeen = orderdetailsBeen;
    }


    public class OrderdetailsBean implements Serializable{
        private String expressname;
        private String expressnumber;
        private String gid;
        private String id;
        private String info;
        private double money;
        private String num;
        private String number;
        private String oid;
        private String pic;
        private double price;
        private String repo;
        private int repoid;
        private int send;
        private String sid;
        private String size;
        private double userprice;
        private List<PathBean> imgsPath;

        public List<PathBean> getImgsPath() {
            return imgsPath;
        }

        public void setImgsPath(List<PathBean> imgsPath) {
            this.imgsPath = imgsPath;
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

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getRepo() {
            return repo;
        }

        public void setRepo(String repo) {
            this.repo = repo;
        }

        public int getRepoid() {
            return repoid;
        }

        public void setRepoid(int repoid) {
            this.repoid = repoid;
        }

        public int getSend() {
            return send;
        }

        public void setSend(int send) {
            this.send = send;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public double getUserprice() {
            return userprice;
        }

        public void setUserprice(double userprice) {
            this.userprice = userprice;
        }

        @Override
        public String toString() {
            return "OrderdetailsBean{" +
                    ", size='" + size + '\'' +
                    ", userprice=" + userprice +
                    ", imgsPath=" + imgsPath +
                    '}';
        }
    }
}
