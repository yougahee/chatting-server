package com.morse.chatting_server.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseMessage {
    //세션
    public String NOT_FOUND_SESSION = "세션이 존재하지 않습니다.";

    // 전송실패
    public String SEND_CHATTING_FAIL = "채팅이 정상적으로 전송되지 않았습니다.";

    //재연결 요청
    public String RECONNECT_SESSION = "세션이 존재하지 않습니다. 재연결을 해주세요.";

    //roomIdx
    public String NEGATIVE_ROOM_IDX_FAIL = "룸 IDX는 음수일 수 없습니다.";
}
