package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-03-13
 *
 * @author Json.
 */

public class YunmasBean extends TokenBean implements Serializable {

    private String brand;
    private String discount;
    private String info;
    private String label;
    private String name;
    private String number;
    private String pic;
    private String saleprice;
    private String sex;
    private String type;
    private String ymprice;
    private List<StocksBean> stocks;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
}
