package com.daishuai.websocket.server.service;

import com.daishuai.websocket.server.vo.KdWebSocketMsgDefaultVo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Daishuai
 * @description WebSocket Server发送消息
 * @date 2019/8/10 11:07
 */
public interface KdWebSocketService {
    
    SimpMessagingTemplate getTemplate();
    
    /**
     * 广播或者自定义播,根据topic scope 来判断
     *
     * @param vo
     */
    default void send(KdWebSocketMsgDefaultVo vo) {
        getTemplate().convertAndSend(vo.getDestination(), vo.getPayload());
    }
    
    /**
     * 根据用户id单播
     *
     * @param userId
     * @param vo
     */
    default void send(String userId, KdWebSocketMsgDefaultVo vo) {
        getTemplate().convertAndSendToUser(userId, vo.getDestination(), vo.getPayload());
    }
    
    /**
     * 组播, 发送给一组指定用户
     *
     * @param userIds
     * @param vo
     */
    default void send(List<String> userIds, KdWebSocketMsgDefaultVo vo) {
        if (CollectionUtils.isEmpty(userIds)) {
            userIds.forEach(userId -> send(userId, vo));
        }
    }
}
