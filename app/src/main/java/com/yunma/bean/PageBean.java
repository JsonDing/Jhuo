package com.yunma.bean;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class PageBean extends TokenBean{

    private String size;
    private String page;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
