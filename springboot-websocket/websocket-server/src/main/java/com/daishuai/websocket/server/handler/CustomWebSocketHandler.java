package com.daishuai.websocket.server.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author keda
 * @date 2024/8/24 15:28:52
 */
@Component
public class CustomWebSocketHandler extends AbstractWebSocketHandler {

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        WebSocketMessage<String> textMessage = new TextMessage("{}");
        session.sendMessage(textMessage);
    }
}
