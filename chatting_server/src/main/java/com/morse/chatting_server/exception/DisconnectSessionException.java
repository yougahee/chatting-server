package com.morse.chatting_server.exception;

public class DisconnectSessionException extends RuntimeException {
	static final long serialVersionUID = 1L;

	public DisconnectSessionException() { super(); }

	public DisconnectSessionException(Throwable e) { super(e); }

	public DisconnectSessionException(String message) { super(message); }

	public DisconnectSessionException(String message, Throwable e) { super(message, e); }

}
