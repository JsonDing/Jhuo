package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-04-13
 *
 * @author Json.
 */

public class AdInfoResultBean implements Serializable{

    private String tittle;

    private List<BannerBean> success;

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public List<BannerBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<BannerBean> success) {
        this.success = success;
    }
}
