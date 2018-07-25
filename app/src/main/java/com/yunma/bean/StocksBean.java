package com.yunma.bean;

import java.io.Serializable;

/**
 * Created by Json on 2017/3/12.
 */

public class StocksBean implements Serializable{
    /**
     * num : 1
     * size : x
     */

    private int position;
    private int id;
    private int num;//库存
    private String size;
    private int buyNum;//购买件数
    private int goodsId;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return size + "x" + num;
    }
}
