package com.morse.chatting_server.model;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

public final class WebSocketSessionHashMap {
	private static final HashMap<Long, WebSocketSession> sessionsHashMap = new HashMap<>();

	private WebSocketSessionHashMap() {}

	public static synchronized void insertSession(Long presenterIdx, WebSocketSession session) {
		sessionsHashMap.put(presenterIdx, session);
	}

	public static synchronized void removeSession(WebSocketSession session) {
		for (HashMap.Entry<Long, WebSocketSession> entry : sessionsHashMap.entrySet()) {
			if (entry.getValue().equals(session)) {
				sessionsHashMap.remove(entry.getKey());
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
