package com.yunma.bean;

import java.io.Serializable;

/**
 * Created by Json on 2017/3/10.
 */

public class PathBean implements Serializable{

    public String imgsPath;

    public String getImgsPath() {
        return imgsPath;
    }

    public void setImgsPath(String imgsPath) {
        this.imgsPath = imgsPath;
    }

    @Override
    public String toString() {
        return imgsPath + ',';
    }
}
