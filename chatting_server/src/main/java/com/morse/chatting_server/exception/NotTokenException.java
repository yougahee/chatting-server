package com.morse.chatting_server.exception;

public class NotTokenException extends RuntimeException{
    static final long serialVersionUID = 1L;

    public NotTokenException() { super(); }

    public NotTokenException(Throwable e) { super(e); }

    public NotTokenException(String errorMessage) { super(errorMessage); }

    public NotTokenException(String message, Throwable e) { super(message, e); }

}
