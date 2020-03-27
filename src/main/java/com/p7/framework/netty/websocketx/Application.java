package com.p7.framework.netty.websocketx;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 13:36
 **/
public class Application {
    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-application.xml");
        context.registerShutdownHook();
        context.start();
        synchronized (Server.class){
            try {
                Server.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
