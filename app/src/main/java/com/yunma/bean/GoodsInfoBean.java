package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-01-09
 *
 * @author Json.
 */

public class GoodsInfoBean {

    private int Count;
    private double totalPrice;
    private int totalNums;
    private boolean success;
    private List<DataBean> Data;

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNums() {
        return totalNums;
    }

    public void setTotalNums(int totalNums) {
        this.totalNums = totalNums;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public class DataBean {

        private String color;
        private List<ColorInfoBean> colorInfo;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public List<ColorInfoBean> getColorInfo() {
            return colorInfo;
        }

        public void setColorInfo(List<ColorInfoBean> colorInfo) {
            this.colorInfo = colorInfo;
        }

        public class ColorInfoBean {

            private int nums;
            private int remain;
            private String size;

            public int getNums() {
                return nums;
            }

            public void setNums(int nums) {
                this.nums = nums;
            }

            public int getRemain() {
                return remain;
            }

            public void setRemain(int remain) {
                this.remain = remain;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }
    }
}
