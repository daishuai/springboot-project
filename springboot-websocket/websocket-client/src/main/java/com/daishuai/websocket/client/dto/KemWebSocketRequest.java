package com.daishuai.websocket.client.dto;

import lombok.Data;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 13:23
 */
@Data
public class KemWebSocketRequest {
    
    private String userId;
    
    private String clientId;
    
    private Long timestamp;
    
    /**
     * 请求参数
     */
    private Object param;
    
    /**
     * 发送地址
     */
    private String sendDestination;
    
}
