package com.daishuai.websocket.client.service;

import com.daishuai.websocket.client.converter.KemMessageConverter;
import com.daishuai.websocket.client.dto.KemWebSocketRequest;
import com.daishuai.websocket.client.exception.ServiceException;
import com.daishuai.websocket.client.handler.AbstractStompSessionHandler;
import com.daishuai.websocket.client.handler.ConnectStompSessionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Daishuai
 * @description WebSocket客户端
 * @date 2019/8/10 13:07
 */
@Slf4j
@Service
public class KemWebSocketClient implements InitializingBean {
    
    private WebSocketStompClient webSocketStompClient;
    
    private WebSocketHttpHeaders handshakeHeaders;
    
    private StompHeaders connectHeaders;
    
    private StompSession stompSession;
    
    @Autowired
    private Map<String, StompSessionHandler> stompSessionHandlerMap;
    
    @Value("${kem.websocket.client.userId:anonymous}")
    private String userId;
    
    @Value("${kem.websocket.client.clientId:default}")
    private String clientId;
    
    @Value("${kem.websocket.server.connectUri:ws://localhost/ws/stomp}")
    private String connectUri;
    
    
    /**
     * 发送消息
     * @param request
     */
    public void sendMessage(KemWebSocketRequest request) {
        if (!this.isConnected()) {
            throw new ServiceException("与服务端断开连接");
        }
        stompSession.setAutoReceipt(true);
        stompSession.send(request.getSendDestination(), request.getParam());
    }
    
    
    /**
     * 订阅
     * @param destination
     * @param handler
     */
    public void subscribe(String destination, Class<? extends StompSessionHandler> handler) {
        stompSession.subscribe(destination, stompSessionHandlerMap.get(this.getBeanName(handler)));
    }
    
    
    @Scheduled(cron = "0/10 * * * * *")
    public void connectTask() {
        log.info("检查WebSocket连接");
        if (stompSession != null && stompSession.isConnected()) {
            return;
        }
        try {
            StompSessionHandler sessionHandler = new ConnectStompSessionHandler();
            ListenableFuture<StompSession> listenableFuture = webSocketStompClient.connect(connectUri, handshakeHeaders, connectHeaders, sessionHandler);
            stompSession = listenableFuture.get();
            stompSessionHandlerMap.values().forEach(stompSessionHandler -> {
                if (stompSessionHandler instanceof AbstractStompSessionHandler) {
                    AbstractStompSessionHandler handler = (AbstractStompSessionHandler) stompSessionHandler;
                    String destination = handler.getDestination();
                    String[] destinations = StringUtils.split(destination, ",");
                    Arrays.stream(destinations).forEach(subscribeDestination -> stompSession.subscribe("/user/" + clientId + subscribeDestination, handler));
                }
            });
        } catch (Exception e) {
            log.info("连接失败:{}", e.getMessage());
        }
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        List<Transport> transports = new ArrayList<>();
        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        webSocketContainer.setDefaultMaxTextMessageBufferSize(50 * 1024 * 1024);
        webSocketContainer.setDefaultMaxBinaryMessageBufferSize(50 * 1024 * 1024);
        transports.add(new WebSocketTransport(new StandardWebSocketClient(webSocketContainer)));
        WebSocketClient webSocketClient = new SockJsClient(transports);
        webSocketStompClient = new WebSocketStompClient(webSocketClient);
        webSocketStompClient.setInboundMessageSizeLimit(50 * 1024 * 1024);
        MessageConverter messageConverter = new KemMessageConverter();
        webSocketStompClient.setMessageConverter(messageConverter);
        webSocketStompClient.setDefaultHeartbeat(new long[]{10000L, 10000L});
        webSocketStompClient.setTaskScheduler(new DefaultManagedTaskScheduler());
        
        handshakeHeaders = new WebSocketHttpHeaders();
        
        connectHeaders = new StompHeaders();
        connectHeaders.set("userId", userId);
        connectHeaders.set("onDisconnectTopic", "/topic");
        connectHeaders.set("clientId", clientId);
    }
    
    /**
     * 是否建立WebSocket连接
     * @return
     */
    private boolean isConnected() {
        return stompSession != null && stompSession.isConnected();
    }
    
    /**
     * 获取beanName
     * @param clazz
     * @return
     */
    private String getBeanName(Class<?> clazz) {
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(clazz);
        return beanNameGenerator.generateBeanName(abd, null);
    }
}
