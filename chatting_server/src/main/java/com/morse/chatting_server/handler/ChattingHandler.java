package com.morse.chatting_server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@NoArgsConstructor
public class ChattingHandler extends TextWebSocketHandler {

    private static Set<WebSocketSession> sessions = new ConcurrentHashMap().newKeySet();
    private static final Gson gson = new GsonBuilder().create();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[Handler::afterConnectionEstablished] New WebSocket connection, sessionId: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, CloseStatus status) throws Exception {
        if (!status.equalsCode(CloseStatus.NORMAL)) {
            log.warn("[Handler::afterConnectionClosed] status: {}, sessionId: {}", status, session.getId());
        }
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

            default:
                sendError(session, "Invalid message with id " + jsonMessage.get("id").getAsString());
                break;
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
