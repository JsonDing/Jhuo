package com.yunma.bean;

/**
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class AddGoodsCollectBean {

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public class SuccessBean {

        private int id;
        private String goodsId;
        private int userId;
        private double saleprice;
        private double yunmaprice;
        private String name;
        private String number;
        private String repoid;
        private String pic;
        private Object stocks;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getSaleprice() {
            return saleprice;
        }

        public void setSaleprice(double saleprice) {
            this.saleprice = saleprice;
        }

        public double getYunmaprice() {
            return yunmaprice;
        }

        public void setYunmaprice(double yunmaprice) {
            this.yunmaprice = yunmaprice;
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

        public String getRepoid() {
            return repoid;
        }

        public void setRepoid(String repoid) {
            this.repoid = repoid;
        }

        public String getPic() {
            return pic;
        }

        public void String(String pic) {
            this.pic = pic;
        }

        public Object getStocks() {
            return stocks;
        }

        public void setStocks(Object stocks) {
            this.stocks = stocks;
        }
    }
}
