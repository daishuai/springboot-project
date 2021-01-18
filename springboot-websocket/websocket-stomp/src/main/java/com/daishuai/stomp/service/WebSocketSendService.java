package com.daishuai.stomp.service;

/**
 * @author Daishuai
 * @date 2021/1/15 16:07
 */
public interface WebSocketSendService {

    /**
     * 发送给指定的客户端
     *
     * @param response 响应结果
     * @param destination 目的地
     * @param clientIds 客户端ID
     */
    void sendToUser(Object response, String destination, String ... clientIds);


    /**
     * 广播消息
     *
     * @param response 响应结果
     * @param destination 目的地
     */
    void sendToAll(Object response, String destination);
}
