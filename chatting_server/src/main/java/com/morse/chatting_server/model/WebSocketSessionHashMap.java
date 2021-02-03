package com.morse.chatting_server.model;

import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@NoArgsConstructor
public final class WebSocketSessionHashMap {
	private static final HashMap<Long, WebSocketSession> sessionsHashMap = new HashMap<>();

	public static synchronized void insertSession(Long presenterIdx, WebSocketSession session) {
		sessionsHashMap.put(presenterIdx, session);
	}

	public static synchronized void removeSession(WebSocketSession session) {
		for (Long key : sessionsHashMap.keySet()) {
			if (sessionsHashMap.get(key).equals(session)) {
				sessionsHashMap.remove(key);
				break;
			}
		}
	}

	public static synchronized WebSocketSession getWebSocketSession(Long presenterIdx) {
		return sessionsHashMap.get(presenterIdx);
	}

	public static synchronized boolean isSessionExist(Long presenterIdx) {
		return sessionsHashMap.containsKey(presenterIdx);
	}
}
