package com.morse.chatting_server.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseMessage {

    //재연결
    public String REQUEST_RECONNECT_WEBSOCKET = "WebSocket과 재연결을 해주세요";


    //세션
    public String NOT_FOUND_SESSION = "세션이 존재하지 않습니다. 재연결 중입니다.";
    public String NOT_LIVE_ROOM_SESSION = "해당 스트리머는 현재 방송 중이지 않습니다.";

    // 전송실패
    public String SEND_CHATTING_FAIL = "채팅이 정상적으로 전송되지 않았습니다.";
    public String TEXT_MESSAGE_NULL = "보내는 채팅은 null이 될 수 없습니다.";


    //재연결 요청
    public String RECONNECT_SESSION = "세션이 존재하지 않습니다. 재연결을 해주세요.";

    //roomIdx
    public String ROOM_IDX_FAIL = "룸 idx값이 정상적으로 오지 않습니다.";
    public String NEGATIVE_ROOM_IDX_FAIL = "룸 IDX는 음수일 수 없습니다.";
}
