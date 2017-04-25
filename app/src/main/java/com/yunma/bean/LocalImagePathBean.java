package com.yunma.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Json on 2017/3/10.
 */

public class LocalImagePathBean implements Serializable {
    private List<PathBean> pathBeen;

    public List<PathBean> getPathBeen() {
        return pathBeen;
    }

    public void setPathBeen(List<PathBean> pathBeen) {
        this.pathBeen = pathBeen;
    }

    @Override
    public String toString() {
        return pathBeen + "";
    }
}
