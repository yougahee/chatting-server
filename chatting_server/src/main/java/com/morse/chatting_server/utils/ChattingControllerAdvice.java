package com.morse.chatting_server.utils;

import com.morse.chatting_server.dto.message.ErrorMessage;
import com.morse.chatting_server.exception.NotFoundException;
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

}
