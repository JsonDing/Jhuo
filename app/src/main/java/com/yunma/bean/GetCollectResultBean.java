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

        private String id;
        private String goodsId;
        private int userId;
        private double saleprice;
        private double yunmaprice;
        private String name;
        private String number;
        private int repoid;
        private String pic;
        private List<StocksBean> stocks;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
