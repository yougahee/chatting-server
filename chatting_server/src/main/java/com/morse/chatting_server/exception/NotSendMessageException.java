package com.morse.chatting_server.exception;

public class NotSendMessageException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public NotSendMessageException() { super(); }

    public NotSendMessageException(Throwable e) { super(e); }

    public NotSendMessageException(String message) { super(message); }

    public NotSendMessageException(String message, Throwable e) { super(message, e); }
}
