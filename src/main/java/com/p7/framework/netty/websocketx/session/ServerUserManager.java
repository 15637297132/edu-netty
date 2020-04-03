package com.p7.framework.netty.websocketx.session;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-03-26 14:34
 **/
public class ServerUserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerUserManager.class);

    private static final ServerUserManager INSTANCE = new ServerUserManager();

    private ConcurrentMap<Integer, ChannelGroup> userIdChannelGroup = new ConcurrentHashMap<>();

    private static ConcurrentMap<Channel, ServerUserInfo> channelServerUserInfo = new ConcurrentHashMap<>();

    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ServerUserManager getInstance() {
        return INSTANCE;
    }

    private ReentrantLock lock = new ReentrantLock();

    public void add(final ServerUserInfo serverUserInfo) {

        try {
            lock.lock();
            channelGroup.add(serverUserInfo.getChannel());
            ChannelGroup channels = userIdChannelGroup.get(serverUserInfo.getUserId());
            if (channels == null) {
                channels = new DefaultChannelGroup(serverUserInfo.getChannel().eventLoop());
                userIdChannelGroup.putIfAbsent(serverUserInfo.getUserId(), channels);
            }
            channels.add(serverUserInfo.getChannel());
            channelServerUserInfo.putIfAbsent(serverUserInfo.getChannel(), serverUserInfo);
        } finally {
            lock.unlock();
        }
    }

    public void remove(Channel channel) {
        LOGGER.info("channel {} remove", channel.toString());
        channel.close();
        try {
            lock.lock();
            channelGroup.remove(channel);
            ServerUserInfo serverUserInfo = channelServerUserInfo.get(channel);
            if (serverUserInfo != null) {
                ChannelGroup channels = userIdChannelGroup.get(serverUserInfo.getUserId());
                if (channels != null && channels.size() > 0) {
                    channels.remove(channel);
                    if (channels.size() == 0) {
                        userIdChannelGroup.remove(serverUserInfo.getUserId());
                    }
                } else {
                    userIdChannelGroup.remove(serverUserInfo.getUserId());
                }
            }
            channelServerUserInfo.remove(channel);
        } finally {
            lock.unlock();
        }
    }

    public void invalidCheck() {
        if (!channelServerUserInfo.isEmpty()) {
            for (Map.Entry<Channel, ServerUserInfo> entry : channelServerUserInfo.entrySet()) {
                Channel channel = entry.getKey();
                if (!channel.isActive() && !channel.isOpen()) {
                    remove(channel);
                    channel.close();
                    LOGGER.info("channel invalid , channel closed");
                }
                Integer userId = entry.getValue().getUserId();
                ChannelGroup channels = userIdChannelGroup.get(userId);
                LOGGER.info("userId {} , channel group size {}", userId, channels != null ? channels.size() : null);
            }
            LOGGER.info("global channel group size {}", channelGroup.size());
        }
    }

    public void broadcast(String msg) {
        LOGGER.info("broadcast {}", msg);
        channelGroup.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void broadcastPing() {
        LOGGER.info("broadcast ping exec");
        channelGroup.writeAndFlush(new PongWebSocketFrame());
    }
}
