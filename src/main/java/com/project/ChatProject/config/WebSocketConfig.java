package com.project.ChatProject.config;

import com.project.ChatProject.config.handler.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Spring Framework에서 WebSocket 구성을 위한 클래스
 *
 */

@Slf4j
@Configuration              // 설정 클래스로 지정
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketHandler chatWebSocketHandler;

    /**
     * webSocket 연결을 위한 Handler를 구성
     *
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("[+] 최초 WebSocket 연결을 위한 Handler 등록");
        registry
                .addHandler(chatWebSocketHandler, "ws-socket")
                .setAllowedOrigins("*");
                /*
                    addHandler() : 클라이언트에서 웹 소켓 연결을 위해 "ws-stomp"라는 엔드포인트로 연결을 시도하면
                        chatWebSocketHandler 핸들러에서 이를 처리
                    .setAllowedOrigins("*") : 접속 시도하는 모든 도메이 허용, CORS 허용
                 */
    }
}
