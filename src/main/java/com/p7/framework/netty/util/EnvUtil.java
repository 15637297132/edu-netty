package com.p7.framework.netty.util;

import io.netty.channel.epoll.Epoll;

public class EnvUtil {

    public static final String OS_NAME = System.getProperty("os.name");

    public static final boolean SSL = System.getProperty("ssl") != null;

    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    public static int workerThreadTotal = Runtime.getRuntime().availableProcessors() * 2;

    static {
        if (OS_NAME != null && OS_NAME.toLowerCase().contains("linux")) {
            isLinuxPlatform = true;
        }

        if (OS_NAME != null && OS_NAME.toLowerCase().contains("windows")) {
            isWindowsPlatform = true;
        }
    }

    public static boolean useEpoll() {
        return isLinuxPlatform && Epoll.isAvailable();
    }

    public static boolean isWindowsPlatform() {
        return isWindowsPlatform;
    }

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }

    public static final String WS_SCHEME = SSL ? "wss://" : "ws://";

    public static final String WS_PATH = "/ws";

}
