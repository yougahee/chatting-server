package com.morse.chatting_server.model;

import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@NoArgsConstructor
public final class WebSocketSessionHashMap {
	private static final HashMap<Long, WebSocketSession> sessionsHashMap = new HashMap<>();

	public static void insertSession(Long presenterIdx, WebSocketSession session) {
		sessionsHashMap.put(presenterIdx, session);
	}

	public static void removeSession(WebSocketSession session) {
		for (HashMap.Entry<Long, WebSocketSession> entry : sessionsHashMap.entrySet()) {
			if (entry.getValue().equals(session)) {
				sessionsHashMap.remove(entry.getKey());
				break;
			}
		}
	}

	public static WebSocketSession getWebSocketSession(Long presenterIdx) {
		return sessionsHashMap.get(presenterIdx);
	}

	public static boolean isSessionExist(Long presenterIdx) {
		return sessionsHashMap.containsKey(presenterIdx);
	}
}
