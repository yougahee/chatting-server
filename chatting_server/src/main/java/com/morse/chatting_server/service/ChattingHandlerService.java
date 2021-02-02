package com.morse.chatting_server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.morse.chatting_server.dto.request.ChattingData;
import com.morse.chatting_server.enums.UserType;
import com.morse.chatting_server.exception.DisconnectSessionException;
import com.morse.chatting_server.exception.NoNegativeNumberException;
import com.morse.chatting_server.exception.NotFoundException;
import com.morse.chatting_server.exception.NotSendMessageException;
import com.morse.chatting_server.utils.ResponseMessage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
@NoArgsConstructor
public class ChattingHandlerService extends TextWebSocketHandler {

    private static final Gson gson = new GsonBuilder().create();
    private final ResponseMessage MESSAGE = new ResponseMessage();
    private static final HashMap<Long,WebSocketSession> sessionsHashMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[Handler::afterConnectionEstablished] New WebSocket connection, sessionId: {}", session.getId());
        log.info("Chatting WebSocket 연결 성공 and SessionId : " + session.getId());
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, CloseStatus status) throws Exception {
        if (!status.equalsCode(CloseStatus.NORMAL)) {
            log.warn("[Handler::afterConnectionClosed] status: {}, sessionId: {}", status, session.getId());
            log.warn("Chatting WebSocket 비정상적 연결 끊김 and SessionId : " + session.getId());
        }
        log.info("Chatting WebSocket 정상적으로 연결 끊김 SessionId : " + session.getId());

        removeSession(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        log.info("Incoming message: {}", jsonMessage);
        log.debug("Incoming message: {}", jsonMessage);

        switch (jsonMessage.get("id").getAsString()) {
            case "roomIdx":
                break;
            default:
                sendError(session, "Invalid message with id " + jsonMessage.get("id").getAsString());
                break;
        }
    }

    public void sendToPresenterChattingMessage(ChattingData chattingTextDTO, String userIdx, String email, String nickname) {
/*
        //## Presenter가 보냈는데 session이 null일 수 가 있나?
        if(!sessionsHashMap.containsKey(chattingTextDTO.getRoomIdx())) {
            if(chattingTextDTO.getUserType() == UserType.PRESENTER) {
                //## 재연결 요청
                throw new DisconnectSessionException(MESSAGE.REQUEST_RECONNECT_WEBSOCKET);*/

        if(!sessionsHashMap.containsKey(chattingTextDTO.getRoomIdx())) {
            if(chattingTextDTO.getUserType().equals(UserType.PRESENTER.getUserType())) {
                //## 재연결 요청
                throw new NotFoundException(MESSAGE.RECONNECT_SESSION);
            }

            throw new NotFoundException(MESSAGE.NOT_FOUND_SESSION);
        }

        WebSocketSession session = sessionsHashMap.get(chattingTextDTO.getRoomIdx());

        //## 실시간 스트리밍 되고 있는지 파악 ( Redis 접근 ) -> Presenter가 재연결을 할 수 있도록 해야 함.
        //## 기본적으로 Presenter가 채팅서버와 연결이 끊긴다면 다 알려줘야 함

        try {
            JsonObject response = new JsonObject();
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            response.addProperty("id", "sendChatting");
            response.addProperty("from", nickname);
            response.addProperty("message", chattingTextDTO.getTextMessage());
            response.addProperty("time", time);
            session.sendMessage(new TextMessage(response.toString()));

            log.info("[send Chatting] success : " + response.toString());
        } catch (Throwable t) {
            log.error("[send Chatting] error : " + t.getMessage());
            sendError(session, t.getMessage());
        }
    }

    private void makeChattingRoom(WebSocketSession session, JsonObject message) {
        long roomIdx = message.get("roomIdx").getAsLong();
        if (roomIdx < 0)  throw new NoNegativeNumberException(MESSAGE.NEGATIVE_ROOM_IDX_FAIL);

        //## token을 처리하는 게 좋을까? token을 까서 현재 방송 중인 아이인지 파악하는 게 좋을까?
        //## 속도면에서는 token 검사가 나을 것 같긴 한데,


        sessionsHashMap.put(roomIdx, session);
        log.info("Presenter로부터 room의 값을 가져옴 -> roomIdx : " + roomIdx);
    }

    private void sendError(WebSocketSession session, String message) {
        try {
            JsonObject response = new JsonObject();
            response.addProperty("id", "error");
            response.addProperty("message", message);
            session.sendMessage(new TextMessage(response.toString()));

            throw new NotSendMessageException(MESSAGE.SEND_CHATTING_FAIL);
        } catch (IOException e) {
            log.error("Exception sending message", e);
        }
    }

    private void removeSession(WebSocketSession session) {
        for(Long key : sessionsHashMap.keySet()) {
            if(sessionsHashMap.get(key).equals(session)) {
                sessionsHashMap.remove(key);
                break;
            }
        }
    }
}
