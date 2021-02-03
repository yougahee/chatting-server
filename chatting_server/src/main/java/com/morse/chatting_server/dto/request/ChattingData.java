package com.morse.chatting_server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ChattingData {
    private Long presenterIdx;
    private String userType;
    private String textMessage;
}
