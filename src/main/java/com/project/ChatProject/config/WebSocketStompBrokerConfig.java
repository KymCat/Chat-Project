package com.project.ChatProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration                  // 설정 클래스로 지정
@EnableWebSocketMessageBroker   // WebSocket 메세지 브로커를 활성화
public class WebSocketStompBrokerConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * configureMessageBroker() : 메세지 브로커 옵션을 구성
     *
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // 1. 구독 prefix 설정
        // 클라이언트가 서버에서 오는 메세지를 구독할 때 사용
        config.enableSimpleBroker("/sub");

        // 2. 발행 prefix 설정
        // 접두사 "/pub" 로 시작하는 메세지는 @MessageMapping 이 달린 메서드로 라우팅
        // 클라이언트가 서버로 메시지를 보낼 때 사용
        config.setApplicationDestinationPrefixes("/pub");
    }

    /**
     * registerStompEndpoints() : 각각 특정 URL에 매핑되는 STOMP 엔드포인트를 등록, 선택적으로 SockJS 폴백 옵션을 활성화하고 구성
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        /*
         *  addEndpoint : 클라이언트가 STOMP(WebSocket) 연결을 맺을 때 접근할 URL을 지정
         *  setAllowedOrigins : 허용할 Origin 설정 => '*' 설정 시 오류
         *  withSockJS : WebSocket을 지원하지 않는 브라우저를 위한 대체 기술
         */
        registry
                .addEndpoint("/ws-stomp")
                .setAllowedOrigins("http://localhost:3000") // CORS 설정
                .withSockJS();
    }
}
