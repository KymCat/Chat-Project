package com.project.ChatProject.controller;

import com.project.ChatProject.Dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * WebSocket 데이터 처리를 수행할 Controller
 *
 */
@RestController
@RequiredArgsConstructor
public class ChatController {
    // 특정 사용자에게 메시지를 보내는데 사용되는 STOMP을 이용한 템플릿
    private final SimpMessagingTemplate template;

    @MessageMapping("/msg")
    public ChatMessageDto send(ChatMessageDto chatMessageDto) {

        // convertAndSend() : STOMP 브로커를 통해 메세지를 발행(pub)할 때 쓰는 메서드
        template.convertAndSend("/sub/msg", chatMessageDto);
        return chatMessageDto;
    }
}
