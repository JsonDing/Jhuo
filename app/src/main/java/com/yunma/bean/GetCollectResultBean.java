package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class GetCollectResultBean {

    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public class SuccessBean implements Serializable{

        private int id;
        private int goodsId;
        private int userId;
        private double saleprice;
        private double yunmaprice;
        private double specialprice;
        private double agentprice;
        private double discount;
        private int issue;
        private String name;
        private String number;
        private int repoid;
        private String pic;
        private List<StocksBean> stocks;

        public double getAgentprice() {
            return agentprice;
        }

        public void setAgentprice(double agentprice) {
            this.agentprice = agentprice;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getIssue() {
            return issue;
        }

        public void setIssue(int issue) {
            this.issue = issue;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
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

        public double getSpecialprice() {
            return specialprice;
        }

        public void setSpecialprice(double specialprice) {
            this.specialprice = specialprice;
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

        public int getRepoid() {
            return repoid;
        }

        public void setRepoid(int repoid) {
            this.repoid = repoid;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public List<StocksBean> getStocks() {
            return stocks;
        }

        public void setStocks(List<StocksBean> stocks) {
            this.stocks = stocks;
        }

        public class StocksBean implements Serializable{

            private int id;
            private String gid;
            private String size;
            private int num;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}
