package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-12
 *
 * @author Json.
 */

public class ModifySelfResultBean {

    private List<SuccessBean> success;

    public List<SuccessBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<SuccessBean> success) {
        this.success = success;
    }

    public class SuccessBean {

        private long date;
        private int id;
        private String number;
        private String pic;
        private int stock;
        private List<StocksBean> stocks;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public List<StocksBean> getStocks() {
            return stocks;
        }

        public void setStocks(List<StocksBean> stocks) {
            this.stocks = stocks;
        }

        public class StocksBean {

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
