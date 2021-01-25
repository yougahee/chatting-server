package com.morse.chatting_server.controller;

import com.morse.chatting_server.dto.request.ChattingTextDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@Slf4j
@CrossOrigin("*")
@Controller
public class ChattingController {

    @PostMapping("")
    public ResponseEntity<Void> sendMessage(@RequestHeader(value = "x-forward-email") String email,
                                            @RequestHeader(value = "x-forward-nickname") String nickname,
                                            @RequestBody ChattingTextDTO chattingData) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
