package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-02-22
 *
 * @author Json.
 */

public class ShoppingCartsResultBean {

    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public class SuccessBean {

        private int id;
        private String goodsId;
        private int userId;
        private GoodsBean goods;
        private double discount;

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

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public class GoodsBean {

            private Object id;
            private String number;
            private String name;
            private String brand;
            private String type;
            private double saleprice;
            private String pic;
            private int stock;
            private Object season;
            private Object year;
            private Object listing;
            private Object sex;
            private double discount;
            private int repoid;
            private Object color;
            private Object code;
            private Object repo;
            private Object uDiscount;
            private double userDiscount;
            private double userPrice;
            private Object agentId;
            private Object sales;
            private List<StocksBean> stocks;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public double getSaleprice() {
                return saleprice;
            }

            public void setSaleprice(double saleprice) {
                this.saleprice = saleprice;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public Object getSeason() {
                return season;
            }

            public void setSeason(Object season) {
                this.season = season;
            }

            public Object getYear() {
                return year;
            }

            public void setYear(Object year) {
                this.year = year;
            }

            public Object getListing() {
                return listing;
            }

            public void setListing(Object listing) {
                this.listing = listing;
            }

            public Object getSex() {
                return sex;
            }

            public void setSex(Object sex) {
                this.sex = sex;
            }

            public double getDiscount() {
                return discount;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public int getRepoid() {
                return repoid;
            }

            public void setRepoid(int repoid) {
                this.repoid = repoid;
            }

            public Object getColor() {
                return color;
            }

            public void setColor(Object color) {
                this.color = color;
            }

            public Object getCode() {
                return code;
            }

            public void setCode(Object code) {
                this.code = code;
            }

            public Object getRepo() {
                return repo;
            }

            public void setRepo(Object repo) {
                this.repo = repo;
            }

            public Object getUDiscount() {
                return uDiscount;
            }

            public void setUDiscount(Object uDiscount) {
                this.uDiscount = uDiscount;
            }

            public double getUserDiscount() {
                return userDiscount;
            }

            public void setUserDiscount(double userDiscount) {
                this.userDiscount = userDiscount;
            }

            public double getUserPrice() {
                return userPrice;
            }

            public void setUserPrice(double userPrice) {
                this.userPrice = userPrice;
            }

            public Object getAgentId() {
                return agentId;
            }

            public void setAgentId(Object agentId) {
                this.agentId = agentId;
            }

            public Object getSales() {
                return sales;
            }

            public void setSales(Object sales) {
                this.sales = sales;
            }

            public List<StocksBean> getStocks() {
                return stocks;
            }

            public void setStocks(List<StocksBean> stocks) {
                this.stocks = stocks;
            }

            public class StocksBean {

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
}
