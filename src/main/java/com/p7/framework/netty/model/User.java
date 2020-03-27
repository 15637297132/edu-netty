package com.p7.framework.netty.model;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 9:42
 **/
public class User {

    private Integer userId;
    private String userName;

    public User(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
