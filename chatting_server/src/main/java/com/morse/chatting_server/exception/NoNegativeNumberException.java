package com.morse.chatting_server.exception;

public class NoNegativeNumberException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public NoNegativeNumberException() { super(); }

    public NoNegativeNumberException(Throwable e) { super(e); }

    public NoNegativeNumberException(String errorMessage) {
        super(errorMessage);
    }

    public NoNegativeNumberException(String message, Throwable e) { super(message, e); }
}
