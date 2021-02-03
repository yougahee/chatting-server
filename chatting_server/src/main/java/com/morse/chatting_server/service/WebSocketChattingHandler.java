package com.morse.chatting_server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.morse.chatting_server.model.WebSocketSessionHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChattingHandler extends TextWebSocketHandler {

    private static final Gson gson = new GsonBuilder().create();
    //private static final HashMap<Long,WebSocketSession> sessionsHashMap = new HashMap<>();
    private final ChattingService chattingService;

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

        WebSocketSessionHashMap.removeSession(session);
        //removeSession(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        log.info("Incoming message: {}", jsonMessage);
        log.debug("Incoming message: {}", jsonMessage);

        switch (jsonMessage.get("id").getAsString()) {
            case "start":
                chattingService.makeChattingRoom(session, jsonMessage);
                break;
            default:
                chattingService.sendError(session, "Invalid message with id " + jsonMessage.get("id").getAsString());
                break;
        }
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
        log.info("pong message : " + message.toString());
    }


    /*private void makeChattingRoom(WebSocketSession session, JsonObject message) {
        if(message.get("token").isJsonNull())
            throw new NoNegativeNumberException(MESSAGE.ROOM_IDX_FAIL);

        String token = message.get("token").getAsString();
        long presenterIdx = jwtUtils.isValidateToken(token);
        if (presenterIdx <= 0) throw new NoNegativeNumberException(MESSAGE.NEGATIVE_ROOM_IDX_FAIL);

        log.info("presenterIdx : " + presenterIdx);
        WebSocketSessionHashMap.insertSession(presenterIdx, session);
        //sessionsHashMap.put(presenterIdx, session);
    }
*/


	/*private void removeSession(WebSocketSession session) {
		for (Long key : sessionsHashMap.keySet()) {
			if (sessionsHashMap.get(key).equals(session)) {
				sessionsHashMap.remove(key);
				break;
			}
		}
	}*/
}
