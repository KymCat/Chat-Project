package config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // WebSocket Session 들을 관리하는 리스트입니다.
    // ConcurrentHashMap : 멀티 스레드 환경에서 안전하게 쓸 수 있는 Map
    // <String, WebSocketSession> : 클라이언트 ID , 해당 클라이언트 WebSocket 연결 정보
    private static final ConcurrentHashMap<String, WebSocketSession> clientSession = new ConcurrentHashMap<>();

    /**
     * [연결 성공] WebSocket HandShake 이후, WebSocket 연결이 열려 사용할 준비가 된 후 호출
     * - 성공했다면 session 값을 추가
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[+] afterConnectionEstablished :: " + session.getId());
        clientSession.put(session.getId(), session);
    }

    /**
     * [메세지 전달] 새로운 WebSocket 메세지가 도착했을 때 호출
     * - 전달 받은 메세지를 순회하면서 메시지를 전송
     * - message.getPayload() 를 통해 메시지가 전달됨
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("[+] handleTextMessage :: " + session);
        log.info("[+] handleTextMessage :: " + message.getPayload()); // 전송된 메세지 내용

        clientSession.forEach((id, info) -> {
            log.info("client Id :: " + id + " WebSocket 연결 정보 :: " + info);
            if (!id.equals(session.getId())) { // 자기 자신에게는 보내지 않는다.
                try {
                    info.sendMessage(message);
                }
                catch (IOException e) {
                    log.error("[+] handleTextMessage IOException :: " + e.getMessage());
                }
            }
        });
    }

    /**
     * [소켓 종료 및 전송 오류] WebSocket 연결이 어느 쪽에서든 종료되거나 전송 오류가 발생한 후 호출
     * - 종료 및 실패하였을 경우 해당 세션을 제거
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clientSession.remove(session); // 해쉬맵에서 제거
        log.info("[+] afterConnectionClosed - Session " + session.getId());
    }
}
