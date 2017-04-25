package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Json on 2017/3/5.
 */

public class GoodsUpLoadInfoBean implements Serializable{
    /**
     * discount : 0.345
     * info : 快递不发新疆，这里是text，字符串不限
     * label : 标签1,标签2
     * name : nike新品，快来买。 varchar 64
     * number : 132-3211 varchar 64
     * pic : as.jpg
     * saleprice : 999.00
     * stocks : [{"num":"1","size":"x"},{"num":"2","size":"xl"},{"num":"3","size":"xxl"}]
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjYxLCJpYXQiOjE0ODY1NDE3MDEyMDcsImV4dCI6MTQ4ODAxMjkzMDEzNX0.xo6OeW07BNaBLgyygaHYhJrKr5uoQPBlLFVG8x-ib_Y
     * type : 1
     * ymprice : 654
     */

    private String discount;
    private String info;
    private String label;
    private String name;
    private String number;
    private String pic;
    private String saleprice;
    private String token;
    private String type;
    private String ymprice;
    private List<StocksBean> stocks;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYmprice() {
        return ymprice;
    }

    public void setYmprice(String ymprice) {
        this.ymprice = ymprice;
    }

    public List<StocksBean> getStocks() {
        return stocks;
    }

    public void setStocks(List<StocksBean> stocks) {
        this.stocks = stocks;
    }

    public class StocksBean implements Serializable{
        /**
         * num : 1
         * size : x
         */

        private String num;
        private String size;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
