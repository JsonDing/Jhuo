package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-02-23
 *
 * @author Json.
 */

public class GetShoppingListBean implements Serializable {

    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public class SuccessBean implements Serializable{

        private int cartNum;
        private String cartSize;
        private String goodsId;
        private int id;
        private String name;
        private String number;
        private String pic;
        private String label;
        private int repoid;
        private double saleprice;
        private int userId;
        private int issue;
        private double yunmaprice;
        private double specialprice;
        private int isSelected;
        private int stock;
        private List<StocksBean> stocks;

        public int getIssue() {
            return issue;
        }

        public void setIssue(int issue) {
            this.issue = issue;
        }

        public SuccessBean() {
        }

        public SuccessBean(int cartNum, String cartSize, String goodsId, int id, String name,
                           String number, String pic, int repoid, double saleprice, int userId,
                           double yunmaprice, int isSelected, int stock, List<StocksBean> stocks) {

            this.cartNum = cartNum;
            this.cartSize = cartSize;
            this.goodsId = goodsId;
            this.id = id;
            this.name = name;
            this.number = number;
            this.pic = pic;
            this.repoid = repoid;
            this.saleprice = saleprice;
            this.userId = userId;
            this.yunmaprice = yunmaprice;
            this.isSelected = isSelected;
            this.stock = stock;
            this.stocks = stocks;
        }

        public double getSpecialprice() {
            return specialprice;
        }

        public void setSpecialprice(double specialprice) {
            this.specialprice = specialprice;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(int isSelected) {
            this.isSelected = isSelected;
        }

        public int getCartNum() {
            return cartNum;
        }

        public void setCartNum(int cartNum) {
            this.cartNum = cartNum;
        }

        public String getCartSize() {
            return cartSize;
        }

        public void setCartSize(String cartSize) {
            this.cartSize = cartSize;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getRepoid() {
            return repoid;
        }

        public void setRepoid(int repoid) {
            this.repoid = repoid;
        }

        public double getSaleprice() {
            return saleprice;
        }

        public void setSaleprice(double saleprice) {
            this.saleprice = saleprice;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getYunmaprice() {
            return yunmaprice;
        }

        public void setYunmaprice(double yunmaprice) {
            this.yunmaprice = yunmaprice;
        }

        public List<StocksBean> getStocks() {
            return stocks;
        }

        public void setStocks(List<StocksBean> stocks) {
            this.stocks = stocks;
        }

        public class StocksBean implements Serializable{
            /**
             * gid : 343738-071YM24
             * id : 77417
             * num : 1
             * size : 33.5
             */

            private String gid;
            private int id;
            private int num;
            private String size;

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

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
        }
    }
}
