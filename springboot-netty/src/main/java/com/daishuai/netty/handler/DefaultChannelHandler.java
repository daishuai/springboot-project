package com.daishuai.netty.handler;

import com.alibaba.fastjson.JSON;
import com.daishuai.netty.common.CommCache;
import com.daishuai.netty.common.SpringBeanFactoryUtils;
import com.daishuai.netty.model.NettySession;
import com.daishuai.netty.model.TcpMessageModel;
import com.daishuai.netty.service.DispatchService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 服务端业务处理器
 * @createTime 2022年08月17日 13:39:00
 */
@Slf4j
public class DefaultChannelHandler extends ChannelInboundHandlerAdapter {

    private final DispatchService dispatchService = SpringBeanFactoryUtils.getBean(DispatchService.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String channelId = channel.id().asLongText();
        NettySession session = new NettySession();
        session.setChannelId(channelId);
        session.setCtx(ctx);
        CommCache.CHANNEL_MAP.put(channelId, session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String channelId = channel.id().asLongText();
        NettySession session = CommCache.CHANNEL_MAP.get(channelId);
        if (session == null) {
            return;
        }
        String clientId = session.getClientId();
        if (StringUtils.isNotBlank(clientId)) {
            CommCache.CLIENT_MAP.remove(clientId);
        }
        CommCache.CHANNEL_MAP.remove(channelId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;
        try {
            if (!(msg instanceof ByteBuf)) {
                ctx.fireChannelRead(msg);
                release = false;
                return;
            }
            ByteBuf byteBuf = (ByteBuf) msg;
            String jsonStr = byteBuf.toString(StandardCharsets.UTF_8);
            try {
                TcpMessageModel tcpMessageModel = JSON.parseObject(jsonStr, TcpMessageModel.class);
                String clientId = tcpMessageModel.getClientId();
                if (StringUtils.isNotBlank(clientId)) {
                    String channelId = ctx.channel().id().asLongText();
                    NettySession session = CommCache.CHANNEL_MAP.get(channelId);
                    session.setClientId(clientId);
                    CommCache.CLIENT_MAP.put(clientId, session);
                }
                assert dispatchService != null;
                dispatchService.dispatch(ctx, tcpMessageModel);
            } catch (Exception e) {
                release = false;
                log.info("反序列化出错: {}", e.getMessage());
                ctx.fireChannelRead(msg);
            }
        } finally {
            if (release) {
                ReferenceCountUtil.release(msg);
            }
        }

    }
}
