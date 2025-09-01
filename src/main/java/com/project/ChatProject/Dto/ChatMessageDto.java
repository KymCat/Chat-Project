package com.project.ChatProject.Dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * 구독자와 수신자 간의 메세지를 주고받는 형태를 구성한 Object
 *
 */
@Data
@Slf4j
public class ChatMessageDto {
    private String content;
    private String sender;

    public ChatMessageDto(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }
}
