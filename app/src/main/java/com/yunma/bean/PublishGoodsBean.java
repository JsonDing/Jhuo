package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Json on 2017/3/12.
 */

public class PublishGoodsBean implements Serializable{

    private String list;
    private String token;
    private List<YunmasBeanList> yunmas;

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<YunmasBeanList> getYunmas() {
        return yunmas;
    }

    public void setYunmas(List<YunmasBeanList> yunmas) {
        this.yunmas = yunmas;
    }

}
