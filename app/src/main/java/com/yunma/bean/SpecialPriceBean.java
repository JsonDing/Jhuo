package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2016-12-28
 *
 * @author Json.
 */

public class SpecialPriceBean implements Serializable{

    private int count;
    private boolean success;
    private List<ItemsBean> items;

    public int getCount() {

        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public class ItemsBean implements Serializable{
        private int goodsId;
        private String goodsCode;
        private int type;
        private double price;
        private String goodsName;
        private int remainNums;
        private long pubdate;
        private long updateTime;
        private long endTime;
        private List<ImgsBean> imgs;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsCode() {
            return goodsCode;
        }

        public void setGoodsCode(String goodsCode) {
            this.goodsCode = goodsCode;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getRemainNums() {
            return remainNums;
        }

        public void setRemainNums(int remainNums) {
            this.remainNums = remainNums;
        }

        public long getPubdate() {
            return pubdate;
        }

        public void setPubdate(long pubdate) {
            this.pubdate = pubdate;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public List<ImgsBean> getImgs() {
            return imgs;
        }

        public void setImgs(List<ImgsBean> imgs) {
            this.imgs = imgs;
        }

        public class ImgsBean implements Serializable{
            private String imgUrl;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }
    }

}
