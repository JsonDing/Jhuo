package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-04-24
 *
 * @author Json.
 */

public class AddToCartsRequestBean implements Serializable{

    private String token;
    private String list;
    private List<CartsBean> carts;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public List<CartsBean> getCarts() {
        return carts;
    }

    public void setCarts(List<CartsBean> carts) {
        this.carts = carts;
    }

}
