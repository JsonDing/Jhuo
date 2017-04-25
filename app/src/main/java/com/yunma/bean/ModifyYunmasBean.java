package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-07
 *
 * @author Json.
 */

public class ModifyYunmasBean {

    private String token;
    private String id;
    private String name;
    private String number;
    private String info;
    private String saleprice;
    private String discount;
    private String ymprice;
    private String type;
    private String sex;
    private String issue;
    private String brand;
    private String label;
    private String pic;
    private List<ModStocksBean> stocks;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(String saleprice) {
        this.saleprice = saleprice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getYmprice() {
        return ymprice;
    }

    public void setYmprice(String ymprice) {
        this.ymprice = ymprice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ModStocksBean> getStocks() {
        return stocks;
    }

    public void setStocks(List<ModStocksBean> stocks) {
        this.stocks = stocks;
    }
}
