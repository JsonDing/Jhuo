package com.yunma.bean;

import java.util.List;

/**
 * Created on 2017-04-12
 *
 * @author Json.
 */

public class ModSelfYunmasBean {
    private String number;
    private String pic;
    private List<ModSelfStocksBean> stocks;

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

    public List<ModSelfStocksBean> getStocks() {
        return stocks;
    }

    public void setStocks(List<ModSelfStocksBean> stocks) {
        this.stocks = stocks;
    }
}
