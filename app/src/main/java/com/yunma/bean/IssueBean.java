package com.yunma.bean;

/**
 * Created by Json on 2017/4/10.
 */

public class IssueBean {

    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjYxLCJpYXQiOjE0ODY1NDE3MDEyMDcsImV4dCI6MTQ4ODAxMjkzMDEzNX0.xo6OeW07BNaBLgyygaHYhJrKr5uoQPBlLFVG8x-ib_Y
     * id : 12
     * issue : 0
     */

    private String token;
    private String id;
    private String issue;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
