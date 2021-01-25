package com.morse.chatting_server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ChattingTextDTO {
    private long roomIdx;
    private String textMessage;
}
