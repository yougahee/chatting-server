package com.morse.chatting_server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.morse.chatting_server.dto.request.ChattingTextDTO;
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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@NoArgsConstructor
public class ChattingHandler extends TextWebSocketHandler {

    private static HashMap<Long,WebSocketSession> sessions = new HashMap<>();
    private static final Gson gson = new GsonBuilder().create();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[Handler::afterConnectionEstablished] New WebSocket connection, sessionId: {}", session.getId());
        log.info("Chatting WebSocket 연결 성공 and SessionId : " + session.getId());
/*
        JsonObject response = new JsonObject();
        response.addProperty("id", "chatting_connected");
        response.addProperty("response", "success");
        session.sendMessage(new TextMessage(response.toString()));*/
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, CloseStatus status) throws Exception {
        if (!status.equalsCode(CloseStatus.NORMAL)) {
            log.warn("[Handler::afterConnectionClosed] status: {}, sessionId: {}", status, session.getId());
            log.warn("Chatting WebSocket 비정상적 연결 끊김 and SessionId : " + session.getId());
        }
        log.info("Chatting WebSocket 정상적으로 연결 끊김 SessionId : " + session.getId());
        sessions.remove(session);
    }

    // ## 에러 쏠 때만 필요
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        log.info("Incoming message: {}", jsonMessage);
        log.debug("Incoming message: {}", jsonMessage);


        switch (jsonMessage.get("id").getAsString()) {
            case "start":
                //start(session, jsonMessage);
                break;
            case "roomIdx":
                // ##redis로 바꾸던지 -> 직렬화문제?
                long roomIdx = Long.parseLong(jsonMessage.get("roomIdx").getAsString());
                sessions.put(roomIdx, session);
                log.info("Presenter로부터 room의 값을 가져옴 -> roomIdx : " + roomIdx);
                break;
            default:
                sendError(session, "Invalid message with id " + jsonMessage.get("id").getAsString());
                break;
        }
    }

    public void sendChatting(ChattingTextDTO chattingTextDTO, String nickname) {

        WebSocketSession session = sessions.get(chattingTextDTO.getRoomIdx());

        try {
            JsonObject response = new JsonObject();
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            response.addProperty("id", "sendChatting");
            response.addProperty("from", nickname);
            response.addProperty("message", chattingTextDTO.getTextMessage());
            response.addProperty("time", time);
            session.sendMessage(new TextMessage(response.toString()));
        } catch (Throwable t) {
            sendError(session, t.getMessage());
        }
    }

    private void sendError(WebSocketSession session, String message) {
        try {
            JsonObject response = new JsonObject();
            response.addProperty("id", "error");
            response.addProperty("message", message);
            session.sendMessage(new TextMessage(response.toString()));
        } catch (IOException e) {
            log.error("Exception sending message", e);
        }
    }
}
