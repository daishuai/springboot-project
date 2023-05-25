package com.daishuai.netty.service;

import com.daishuai.netty.common.CommCache;
import com.daishuai.netty.model.NettySession;
import com.daishuai.netty.model.TcpMessageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Daishuai
 * @version 1.0.0
 * @description 消息发送
 * @createTime 2022年08月18日 09:53:00
 */
@Service
public class MessageSendService {

    public void sendMessage(TcpMessageModel messageModel) {
        if (messageModel == null) {
            return;
        }
        String clientId = messageModel.getClientId();
        if (StringUtils.isBlank(clientId)) {
            for (NettySession session : CommCache.CHANNEL_MAP.values()) {
                session.getCtx().channel().writeAndFlush(messageModel);
            }
        } else {
            NettySession session = CommCache.CLIENT_MAP.get(clientId);
            if (session != null) {
                session.getCtx().channel().writeAndFlush(messageModel);
            } else {
                CommCache.CHANNEL_MAP.values().stream().filter(se -> StringUtils.isBlank(se.getClientId()))
                        .forEach(se -> se.getCtx().channel().writeAndFlush(messageModel));
            }
        }
    }
}
