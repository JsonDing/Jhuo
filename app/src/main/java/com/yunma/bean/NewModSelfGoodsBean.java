package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-12
 *
 * @author Json.
 */

public class NewModSelfGoodsBean {

    private String token;
    private String list;
    private List<ModSelfYunmasBean> yunmas;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public List<ModSelfYunmasBean> getYunmas() {
        return yunmas;
    }

    public void setYunmas(List<ModSelfYunmasBean> yunmas) {
        this.yunmas = yunmas;
    }

    /*public class YunmasBean {

        private String number;
        private String pic;
        private List<StocksBean> stocks;

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

        public List<StocksBean> getStocks() {
            return stocks;
        }

        public void setStocks(List<StocksBean> stocks) {
            this.stocks = stocks;
        }
    }*/
}
