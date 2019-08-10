package com.daishuai.websocket.server.vo;

import lombok.Data;
import org.springframework.web.socket.WebSocketMessage;

/**
 * @author Daishuai
 * @description TODO
 * @date 2019/8/10 11:06
 */
@Data
public abstract class KdWebSocketMsgAbsVo<D, T, U> implements WebSocketMessage<T> {
    
    protected U userId;
    
    protected D destination;
    
    protected T payload;
    
    @Override
    public int getPayloadLength() {
        return 0;
    }
    
    @Override
    public boolean isLast() {
        return false;
    }
}
