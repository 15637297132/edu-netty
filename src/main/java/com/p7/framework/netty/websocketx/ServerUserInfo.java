package com.p7.framework.netty.websocketx;

import io.netty.channel.Channel;

import java.io.Serializable;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 14:38
 **/
public class ServerUserInfo implements Serializable {

    private Integer userId;

    private String userName;

    private Channel channel;

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

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
