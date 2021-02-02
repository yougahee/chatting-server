package com.morse.chatting_server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ChattingData {
    private long presenterIdx;
    // ## presenter, viewer
    private String userType;
    private String textMessage;
}
