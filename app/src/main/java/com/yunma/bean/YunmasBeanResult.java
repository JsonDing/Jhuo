package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-03-22
 *
 * @author Json.
 */

public class YunmasBeanResult {


    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public static class SuccessBean {

        private String brand;
        private double discount;
        private int id;
        private String info;
        private String label;
        private String name;
        private String number;
        private String pic;
        private double saleprice;
        private int sex;
        private int stock;
        private int type;
        private double ymprice;
        private List<AddStocksBean> stocks;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public double getSaleprice() {
            return saleprice;
        }

        public void setSaleprice(double saleprice) {
            this.saleprice = saleprice;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getYmprice() {
            return ymprice;
        }

        public void setYmprice(double ymprice) {
            this.ymprice = ymprice;
        }

        public List<AddStocksBean> getStocks() {
            return stocks;
        }

        public void setStocks(List<AddStocksBean> stocks) {
            this.stocks = stocks;
        }

        public static class AddStocksBean {
            /**
             * id : 3041
             * num : 1
             * size : S/(160)
             * yunmaId : 1083
             */

            private int id;
            private int num;
            private String size;
            private int yunmaId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public int getYunmaId() {
                return yunmaId;
            }

            public void setYunmaId(int yunmaId) {
                this.yunmaId = yunmaId;
            }
        }
    }
}
