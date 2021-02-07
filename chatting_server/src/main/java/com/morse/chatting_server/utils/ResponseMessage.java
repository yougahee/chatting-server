package com.morse.chatting_server.utils;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {

    private ResponseMessage() {}

    //세션
    public static final String NOT_FOUND_SESSION = "세션이 존재하지 않습니다. 재연결 중입니다. 잠시 후에 다시 보내주세요..";
    public static final String NOT_LIVE_ROOM_SESSION = "해당 스트리머는 현재 방송 중이지 않습니다.";

    // 전송실패
    public static final String SEND_CHATTING_FAIL = "채팅이 정상적으로 전송되지 않았습니다.";
    public static final String TEXT_MESSAGE_NULL = "보내는 채팅은 null이 될 수 없습니다.";


    //재연결 요청
    public static final String RECONNECT_SESSION = "세션이 존재하지 않습니다. 재연결을 해주세요.";

    //presenterIdx
    public static final String PRESENTER_IDX_NOT_NULL = "PresenterIdx는 null값이 올 수 없습니다.";

    //roomIdx
    public static final String ROOM_IDX_FAIL = "룸 idx값이 정상적으로 오지 않습니다.";
    public static final String NEGATIVE_ROOM_IDX_FAIL = "룸 IDX는 음수일 수 없습니다.";

    //token
    public static final String TOKEN_NULL = "Token null";
    public static final String EXPIRED_TOKEN = "토큰이 만료되었습니다.";
    public static final String MODULATE_TOKEN = "토큰이 변조되었습니다.";
    public static final String NOT_TOKEN_TYPE = "토큰의 유형이 아닙니다.";
    public static final String JWT_EXCEPTION = "Jwt Exception";
}
