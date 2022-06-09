package com.example.reactivepwads.config.websockets;

import com.example.reactivepwads.config.websockets.handler.AdWebSocketHandler;
import com.example.reactivepwads.config.websockets.handler.MessageWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration

public class WebSocketConfig {
    private final AdWebSocketHandler adWebSocketHandler;
    private final MessageWebSocketHandler messageWebSocketHandler;

    public WebSocketConfig(AdWebSocketHandler adWebSocketHandler, MessageWebSocketHandler messageWebSocketHandler) {
        this.adWebSocketHandler = adWebSocketHandler;
        this.messageWebSocketHandler = messageWebSocketHandler;
    }

    @Bean
    public WebSocketService webSocketService() {
        ReactorNettyRequestUpgradeStrategy strategy = new ReactorNettyRequestUpgradeStrategy();
        return new HandshakeWebSocketService(strategy);
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter(WebSocketService webSocketService) {
        return new WebSocketHandlerAdapter(webSocketService);
    }

    @Bean
    HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/ads", adWebSocketHandler);
        map.put("/ws/messages", messageWebSocketHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return mapping;
    }
}
