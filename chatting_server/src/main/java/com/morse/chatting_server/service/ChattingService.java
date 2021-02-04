package com.morse.chatting_server.service;

import com.google.gson.JsonObject;
import com.morse.chatting_server.dto.request.ChattingData;
import com.morse.chatting_server.enums.UserType;
import com.morse.chatting_server.exception.DisconnectSessionException;
import com.morse.chatting_server.exception.NotFoundException;
import com.morse.chatting_server.model.WebSocketSessionHashMap;
import com.morse.chatting_server.utils.JwtUtils;
import com.morse.chatting_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingService {

	private final ResponseMessage MESSAGE;
	private final JwtUtils jwtUtils;
	private final LiveCheckService liveCheckService;

	public void makeChattingRoom(WebSocketSession session, JsonObject message) throws IOException {
		String token;

		try {
			token = message.get("token").getAsString();
		} catch (NullPointerException ne) {
			log.error(ne.getMessage());
			sendError(session, MESSAGE.ROOM_IDX_FAIL);
			session.close();
			return;
		}

		if (token == null || token.equals("")) {
			log.error("token is null");
			sendError(session, MESSAGE.TOKEN_NULL);
			session.close();
			return;
		}

		Long presenterIdx = jwtUtils.isValidateToken(token);
		if (presenterIdx == null) {
			sendErrorCustom(session, "tokenException", MESSAGE.JWT_EXCEPTION);
			session.close();
			return;
		}

		log.info("presenterIdx : " + presenterIdx);
		WebSocketSessionHashMap.insertSession(presenterIdx, session);
	}

	public void sendToPresenterChattingMessage(ChattingData chattingTextDTO, String user_idx, String nickname) throws IOException {
		Long presenterIdx = chattingTextDTO.getPresenterIdx();
		String userType = chattingTextDTO.getUserType();

		if (presenterIdx == null) {
			if (userType.equals(UserType.PRESENTER.getUserType()))
				presenterIdx = Long.parseLong(user_idx);
			else
				throw new NotFoundException(MESSAGE.PRESENTER_IDX_NOT_NULL);
		}

		//## 실제로 배포할 때는 IF문 안에 들어있는 두개 자리 바꾸기
		if (!WebSocketSessionHashMap.isSessionExist(presenterIdx)) {
			if (liveCheckService.checkLiveRoom(presenterIdx)) {
				if (userType.equals(UserType.PRESENTER.getUserType()))
					throw new DisconnectSessionException(MESSAGE.RECONNECT_SESSION);

				//## 잠시 후에 다시 전송해주세요..
				//## 클라한테 다시 요청하는 것보다 세션 재연결 요청 후 다시 연결이 되는 지 감지되면 데이터 보내주기.
				// 시그널링 서버에 재연결 로직
				//liveCheckService.reconnectRequest(presenterIdx);

				//일단, 세션 없다고 exception 보내줌
				throw new NotFoundException(MESSAGE.NOT_FOUND_SESSION);
			} else {
				throw new NotFoundException(MESSAGE.NOT_LIVE_ROOM_SESSION);
			}
		}

		WebSocketSession session = WebSocketSessionHashMap.getWebSocketSession(presenterIdx);

		try {
			JsonObject response = new JsonObject();
			String time = new SimpleDateFormat("HH:mm").format(new Date());
			response.addProperty("id", "sendChatting");
			response.addProperty("nickname", nickname);
			response.addProperty("message", chattingTextDTO.getTextMessage());
			response.addProperty("time", time);

			session.sendMessage(new TextMessage(response.toString()));
			log.info("[send Chatting] success : " + response.toString());
		} catch (Throwable t) {
			log.error("[send Chatting] error : " + t.getMessage());
			sendError(session, t.getMessage());
		}
	}

	public void sendError(WebSocketSession session, String message) {
		try {
			JsonObject response = new JsonObject();
			response.addProperty("id", "error");
			response.addProperty("message", message);
			session.sendMessage(new TextMessage(response.toString()));
		} catch (IOException e) {
			log.error("Exception sending message", e);
		}
	}

	public void sendErrorCustom(WebSocketSession session, String id, String message) {
		try {
			JsonObject response = new JsonObject();
			response.addProperty("id", id);
			response.addProperty("response", "reject");
			response.addProperty("message", message);
			session.sendMessage(new TextMessage(response.toString()));
		} catch (IOException e) {
			log.error("Exception sending message", e);
		}
	}
}
