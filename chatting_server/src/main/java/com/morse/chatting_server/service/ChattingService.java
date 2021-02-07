package com.morse.chatting_server.service;

import com.google.gson.JsonObject;
import com.morse.chatting_server.dto.request.ChattingData;
import com.morse.chatting_server.enums.UserType;
import com.morse.chatting_server.exception.DisconnectSessionException;
import com.morse.chatting_server.exception.NotFoundException;
import com.morse.chatting_server.model.ChattingLog;
import com.morse.chatting_server.model.WebSocketSessionHashMap;
import com.morse.chatting_server.repository.ChattingLogRepository;
import com.morse.chatting_server.utils.JwtUtils;
import com.morse.chatting_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingService {

	private final LiveCheckService liveCheckService;
	private final ChattingLogRepository chattingLogRepository;
	private final JwtUtils jwtUtils;

	public void makeChattingRoom(WebSocketSession session, JsonObject message) throws IOException {
		String token;

		try {
			token = message.get("token").getAsString();
		} catch (NullPointerException ne) {
			log.error(ne.getMessage());
			sendError(session, ResponseMessage.ROOM_IDX_FAIL);
			session.close();
			return;
		}

		Long presenterIdx = jwtUtils.isValidateToken(token);
		isPresenterIdxNull(presenterIdx, session);
		WebSocketSessionHashMap.insertSession(presenterIdx, session);
	}

	public void sendToPresenterChattingMessage(ChattingData chattingTextDTO, String userIdx, String nickname) throws IOException {
		Long presenterIdx = chattingTextDTO.getPresenterIdx();
		String userType = chattingTextDTO.getUserType();
		nickname = URLDecoder.decode(nickname, StandardCharsets.UTF_8);

		if (presenterIdx == null) {
			if (userType.equals(UserType.PRESENTER.getUserType()))
				presenterIdx = Long.parseLong(userIdx);
			else
				throw new NotFoundException(ResponseMessage.PRESENTER_IDX_NOT_NULL);
		}

		existSession(presenterIdx, userType);
		WebSocketSession session = WebSocketSessionHashMap.getWebSocketSession(presenterIdx);
		sendMessageToPresenter(session, nickname, chattingTextDTO.getTextMessage());
		chattingLogRepository.save(new ChattingLog(Integer.toUnsignedLong(0), presenterIdx, Long.parseLong(userIdx), nickname, chattingTextDTO.getTextMessage()));
	}

	public void sendMessageToPresenter(WebSocketSession session, String nickname, String message) {
		try {
			JsonObject response = new JsonObject();
			String time = new SimpleDateFormat("HH:mm").format(new Date());
			response.addProperty("id", "sendChatting");
			response.addProperty("nickname", nickname);
			response.addProperty("message", message);
			response.addProperty("time", time);

			session.sendMessage(new TextMessage(response.toString()));
			log.info("[send Chatting] success : " + response.toString());
		} catch (Throwable t) {
			log.error("[send Chatting] error : " + t.getMessage());
			sendError(session, t.getMessage());
		}
	}

	public void existSession(Long presenterIdx, String userType) throws IOException {
		if (!WebSocketSessionHashMap.isSessionExist(presenterIdx)) {
			if (liveCheckService.checkLiveRoom(presenterIdx)) {
				if (userType.equals(UserType.PRESENTER.getUserType()))
					throw new DisconnectSessionException(ResponseMessage.RECONNECT_SESSION);
				else
					throw new NotFoundException(ResponseMessage.NOT_FOUND_SESSION);
			} else {
				throw new NotFoundException(ResponseMessage.NOT_LIVE_ROOM_SESSION);
			}
		}
	}

	public void isPresenterIdxNull(Long presenterIdx, WebSocketSession session) throws IOException {
		if (presenterIdx == null) {
			sendErrorCustom(session, "tokenException", ResponseMessage.JWT_EXCEPTION);
			session.close();
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
