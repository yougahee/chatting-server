package com.morse.chatting_server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.morse.chatting_server.dto.request.ChattingData;
import com.morse.chatting_server.enums.UserType;
import com.morse.chatting_server.exception.NoNegativeNumberException;
import com.morse.chatting_server.exception.NotFoundException;
import com.morse.chatting_server.exception.NotSendMessageException;
import com.morse.chatting_server.utils.JwtUtils;
import com.morse.chatting_server.utils.ResponseMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
@NoArgsConstructor
public class ChattingHandlerService extends TextWebSocketHandler {

    private static final Gson gson = new GsonBuilder().create();
    private static final HashMap<Long,WebSocketSession> sessionsHashMap = new HashMap<>();

    private final ResponseMessage MESSAGE = new ResponseMessage();
    private final JwtUtils jwtUtils = new JwtUtils();
    private final LiveCheckService liveCheckService = new LiveCheckService();

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
            case "start":
                makeChattingRoom(session, jsonMessage);
                break;
            default:
                sendError(session, "Invalid message with id " + jsonMessage.get("id").getAsString());
                break;
        }
    }

    public void sendToPresenterChattingMessage(ChattingData chattingTextDTO, String nickname) throws IOException {
        long presenterIdx = chattingTextDTO.getPresenterIdx();

        if(!sessionsHashMap.containsKey(presenterIdx)) {

            //## 통신을 통해 해당 presenter가 live하고 있는 지 확인.
            // ## 테스트 해봐야 함
            if(liveCheckService.checkLiveRoom(presenterIdx)){
                //## true이면 presenter에게 재연결요청


                //## 잠시 후에 다시 전송해주세요.. -> 이게 뭐여 ㅂㄷㅂㄷ
                //## 클라한테 다시 요청하는 것보다 세션 재연결 요청 후 다시 연결이 되는 지 감지되면 데이터 보내주기/..
                //## 와우 넘 오바

                //## 일단, 세션 없다고 exception 보내줌
                throw new NotFoundException(MESSAGE.NOT_FOUND_SESSION);
            } else {
                //## 현재 방송 중이 아니라는 메세지를 보내 줌.
                throw new NotFoundException(MESSAGE.NOT_LIVE_ROOM_SESSION);
            }
        }

        WebSocketSession session = sessionsHashMap.get(chattingTextDTO.getPresenterIdx());

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
        if(message.get("token").isJsonNull())
            throw new NoNegativeNumberException(MESSAGE.ROOM_IDX_FAIL);

        String token = message.get("token").getAsString();
        long presenterIdx = jwtUtils.isValidateToken(token);
        if (presenterIdx <= 0) throw new NoNegativeNumberException(MESSAGE.NEGATIVE_ROOM_IDX_FAIL);

        sessionsHashMap.put(presenterIdx, session);
        log.info("presenterIdx : " + presenterIdx);
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
