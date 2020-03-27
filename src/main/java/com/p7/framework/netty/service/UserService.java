package com.p7.framework.netty.service;

import com.p7.framework.netty.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 9:41
 **/
@Service("userService")
public class UserService implements InitializingBean {

    public ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        userMap.put("token-1", new User(1, "张三"));
        userMap.put("token-2", new User(2, "lisa"));
        userMap.put("token-3", new User(3, "赵四"));
        userMap.put("token-4", new User(4, "netty"));
        userMap.put("token-5", new User(5, "java"));
        userMap.put("token-6", new User(6, "hello"));
        userMap.put("token-7", new User(7, "王五"));
        userMap.put("token-8", new User(8, "李大"));
        userMap.put("token-9", new User(9, "world"));
    }


    public User getUserByToken(String token) {
        return userMap.get(token);
    }

}
