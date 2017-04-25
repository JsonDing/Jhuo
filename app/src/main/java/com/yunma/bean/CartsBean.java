package com.yunma.bean;

import java.io.Serializable;

/**
 * Created on 2017-04-24
 *
 * @author Json.
 */

public class CartsBean implements Serializable {

    private String gid;
    private String size;
    private String num;

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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "“" + size + "” " + num + "件" ;
    }
}
