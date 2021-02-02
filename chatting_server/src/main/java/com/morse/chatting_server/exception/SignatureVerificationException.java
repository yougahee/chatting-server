package com.morse.chatting_server.exception;

public class SignatureVerificationException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public SignatureVerificationException() { super(); }

    public SignatureVerificationException(Throwable e) { super(e); }

    public SignatureVerificationException(String errorMessage) { super(errorMessage); }

    public SignatureVerificationException(String message, Throwable e) { super(message, e); }
}
