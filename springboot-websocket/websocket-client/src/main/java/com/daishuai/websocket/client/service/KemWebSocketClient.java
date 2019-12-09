package com.daishuai.websocket.client.service;

import com.daishuai.websocket.client.constant.CommonCache;
import com.daishuai.websocket.client.converter.KemMessageConverter;
import com.daishuai.websocket.client.dto.KemWebSocketRequest;
import com.daishuai.websocket.client.exception.ServiceException;
import com.daishuai.websocket.client.handler.AbstractStompSessionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.*;
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
import java.lang.reflect.Type;
import java.util.*;

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
     *
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
     *
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
     *
     * @return
     */
    private boolean isConnected() {
        return stompSession != null && stompSession.isConnected();
    }
    
    /**
     * 获取beanName
     *
     * @param clazz
     * @return
     */
    private String getBeanName(Class<?> clazz) {
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(clazz);
        return beanNameGenerator.generateBeanName(abd, null);
    }
    
    @Scheduled(cron = "0/10 * * * * *")
    public void heartBeat() {
        if (isConnected()) {
            Map<String, Object> request = new HashMap<>();
            request.put("timestamp", System.currentTimeMillis());
            request.put("incidentCode", "c665d8c2cf8c4726980b0d135b684b22");
            request.put("userId", clientId);
            request.put("type", "wsxx");
            
            stompSession.send("/ws/heartBeat/ping", request);
        }
    }
    
    class ConnectStompSessionHandler extends StompSessionHandlerAdapter {
        
        private long lastTime;
        
        /**
         * Invoked when the session is ready to use, i.e. after the underlying
         * transport (TCP, WebSocket) is connected and a STOMP CONNECTED frame is
         * received from the broker.
         *
         * @param session          the client STOMP session
         * @param connectedHeaders the STOMP CONNECTED frame headers
         */
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            lastTime = System.currentTimeMillis();
            //与服务端建立连接
            CommonCache.isConnect = true;
            log.info("连接成功");
        }
        
        /**
         * Invoked before {@link #handleFrame(StompHeaders, Object)} to determine the
         * type of Object the payload should be converted to.
         *
         * @param headers the headers of a message
         */
        @Override
        public Type getPayloadType(StompHeaders headers) {
            log.info("getPayloadType ");
            return String.class;
        }
        
        /**
         * Handle a STOMP frame with the payload converted to the target type returned
         * from {@link #getPayloadType(StompHeaders)}.
         *
         * @param headers the headers of the frame
         * @param payload the payload or {@code null} if there was no payload
         */
        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            log.info("handleFrame,payload:{}", payload);
        }
        
        /**
         * Handle any exception arising while processing a STOMP frame such as a
         * failure to convert the payload or an unhandled exception in the
         * application {@code StompFrameHandler}.
         *
         * @param session   the client STOMP session
         * @param command   the STOMP command of the frame
         * @param headers   the headers
         * @param payload   the raw payload
         * @param exception the exception
         */
        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            CommonCache.isConnect = session.isConnected();
            log.info("handleException 出错：{}", exception.getMessage(), exception);
            log.info("handleException距上次建立连接时间间隔：{}秒", (System.currentTimeMillis() - lastTime) / 1000);
        }
        
        /**
         * Handle a low level transport error which could be an I/O error or a
         * failure to encode or decode a STOMP message.
         * <p>Note that
         * {@link org.springframework.messaging.simp.stomp.ConnectionLostException
         * ConnectionLostException} will be passed into this method when the
         * connection is lost rather than closed normally via
         * {@link StompSession#disconnect()}.
         *
         * @param session   the client STOMP session
         * @param exception the exception that occurred
         */
        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            CommonCache.isConnect = session.isConnected();
            log.error("handleTransportError :{}", exception.getMessage(), exception);
            log.info("handleTransportError距上次建立连接时间间隔：{}秒", (System.currentTimeMillis() - lastTime) / 1000);
        }
    }
}
