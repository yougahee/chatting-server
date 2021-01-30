package com.morse.chatting_server.utils;

import com.morse.chatting_server.dto.message.ErrorMessage;
import com.morse.chatting_server.exception.DisconnectSessionException;
import com.morse.chatting_server.exception.NotFoundException;
import com.morse.chatting_server.exception.NotSendMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ChattingControllerAdvice {
	@ExceptionHandler(value = {NotFoundException.class})
	public ResponseEntity<ErrorMessage> notFoundException(HttpServletRequest req, NotFoundException nfe) {
		log.error(nfe.getMessage(), nfe);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(nfe.getMessage(), 400, req.getRequestURI()));
	}

	@ExceptionHandler(value = {NotSendMessageException.class})
	public ResponseEntity<ErrorMessage> notSendMessageException(HttpServletRequest req, NotSendMessageException nsme) {
		log.error(nsme.getMessage(), nsme);
		return ResponseEntity
				.badRequest()
				.body(new ErrorMessage(nsme.getMessage(), 400, req.getRequestURI()));
	}

	//## 재연결 요청 어떤 status code로 써야 맞는 걸까?
	@ExceptionHandler(value = {DisconnectSessionException.class})
	public ResponseEntity<ErrorMessage> disconnectSessionException(HttpServletRequest req, DisconnectSessionException dse) {
		log.error(dse.getMessage(), dse);
		return ResponseEntity
				.accepted()
				.body(new ErrorMessage(dse.getMessage(), 400, req.getRequestURI()));
	}
}
