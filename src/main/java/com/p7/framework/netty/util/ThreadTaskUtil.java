package com.p7.framework.netty.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class ThreadTaskUtil {
    private static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    public static ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

}
