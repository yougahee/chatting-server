package com.morse.chatting_server.dto.request;

import com.morse.chatting_server.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ChattingData {
    private long roomIdx;
    // ## presenter, viewer
    private Enum<UserType> userType;
    private String textMessage;
}
