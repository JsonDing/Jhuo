package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2017-04-13
 * @author Json.
 */

public class VolumeResultBean implements Serializable{
    private int status;
    private List<VolumeListBean> success;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<VolumeListBean> getSuccess() {
        return success;
    }

    public void setSuccess(List<VolumeListBean> success) {
        this.success = success;
    }

}
