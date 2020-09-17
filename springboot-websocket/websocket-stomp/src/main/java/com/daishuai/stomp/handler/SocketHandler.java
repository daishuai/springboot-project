package com.daishuai.stomp.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Daishuai
 * @date 2020/7/29 9:30
 */
@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info(" handleTextMessage start ........");
        String payload = message.getPayload();
        log.info("message: {}", payload);
        this.sendMessageToUser(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected ... SessionId: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        sessions.remove(session);
        log.info("Session {} closed because of {}", session.getId(), status.getReason());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("error occurred at sender {}",session.getId(), exception);
    }

    private void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                log.info("session {} is not online", session.getId());
                continue;
            }
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error("send message to session {} occurred error", session.getId(), e);
            }
        }
    }

    private void sendMessageToUser(WebSocketSession session, TextMessage message) {
        if (!session.isOpen()) {
            log.info("session {} is not online", session.getId());
            return;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("send message to session {} occurred error", session.getId(), e);
        }
    }
}
