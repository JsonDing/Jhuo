package com.yunma.bean;

import java.io.Serializable;

/**
 * Created on 2017-03-23
 *
 * @author Json.
 */

public class DocBean implements Serializable {
    private String id;
    private String path;
    private String size;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocBean{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
