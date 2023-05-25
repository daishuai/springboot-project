package com.daishuai.netty.handler;

import io.netty.channel.ChannelHandler;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description ChannelHandler集合
 * @createTime 2022年08月17日 17:09:00
 */
public interface ChannelHandlerHolder {

    ChannelHandler[] handlers();
}
