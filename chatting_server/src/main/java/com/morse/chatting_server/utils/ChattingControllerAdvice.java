package com.morse.chatting_server.utils;

import com.morse.chatting_server.dto.message.ErrorMessage;
import com.morse.chatting_server.exception.*;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ChattingControllerAdvice {

	//Bad Request
	@ExceptionHandler(value = {NotFoundException.class})
	public ResponseEntity<ErrorMessage> notFoundException(HttpServletRequest req, NotFoundException nfe) {
		log.error(nfe.getMessage(), nfe);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(nfe.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getRequestURI()));
	}

	@ExceptionHandler(value = {NotSendMessageException.class})
	public ResponseEntity<ErrorMessage> notSendMessageException(HttpServletRequest req, NotSendMessageException nsme) {
		log.error(nsme.getMessage(), nsme);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(nsme.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getRequestURI()));
	}

	//## 재연결 요청 어떤 status code로 써야 맞는 걸까?
	@ExceptionHandler(value = {DisconnectSessionException.class})
	public ResponseEntity<ErrorMessage> disconnectSessionException(HttpServletRequest req, DisconnectSessionException dse) {
		log.error(dse.getMessage(), dse);
		return ResponseEntity
				.accepted()
				.body(new ErrorMessage(dse.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getRequestURI()));
	}

	@ExceptionHandler(value = {NoNegativeNumberException.class})
	public ResponseEntity<ErrorMessage> noNegativeNumberException(HttpServletRequest req, NoNegativeNumberException nne) {
		log.error(nne.getMessage(), nne);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(nne.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getRequestURI()));
	}

	@ExceptionHandler(value = {MissingRequestHeaderException.class})
	public ResponseEntity<ErrorMessage> missingRequestHeaderException(HttpServletRequest req, MissingRequestHeaderException mrhe) {
		log.error(mrhe.getMessage(), mrhe);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(mrhe.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getRequestURI()));
	}

	//token error
	@ExceptionHandler(value = {NotTokenException.class})
	public ResponseEntity<ErrorMessage> notTokenException(HttpServletRequest req, NotTokenException nte) {
		log.error(nte.getMessage(), nte);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(nte.getMessage(), HttpStatus.UNAUTHORIZED.value(), req.getRequestURI()));
	}

	@ExceptionHandler(value = {SignatureVerificationException.class})
	public ResponseEntity<ErrorMessage> signatureVerificationException(HttpServletRequest req, SignatureVerificationException sve) {
		log.error(sve.getMessage(), sve);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(sve.getMessage(), HttpStatus.UNAUTHORIZED.value(), req.getRequestURI()));
	}

	@ExceptionHandler(value = {JwtException.class})
	public ResponseEntity<ErrorMessage> signatureVerificationException(HttpServletRequest req, JwtException je) {
		log.error(je.getMessage(), je);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(je.getMessage(), HttpStatus.UNAUTHORIZED.value(), req.getRequestURI()));
	}

	//internal server error


}
