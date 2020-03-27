package com.p7.framework.netty.service;

import com.p7.framework.netty.websocketx.ServerUserManager;
import org.springframework.stereotype.Service;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 15:57
 **/
@Service
public class WebSocketService {

    public void broadcast(String msg){
        ServerUserManager.getInstance().broadcast(msg);
    }
}
