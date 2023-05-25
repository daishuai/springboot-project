package com.daishuai.netty.model;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 客户端会话
 * @createTime 2022年08月18日 09:08:00
 */
@Data
public class NettySession {

    private String clientId;

    private String channelId;

    private ChannelHandlerContext ctx;
}
