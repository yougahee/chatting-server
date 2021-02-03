package com.morse.chatting_server.controller;

import com.morse.chatting_server.dto.request.ChattingData;
import com.morse.chatting_server.service.ChattingHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final ChattingHandlerService chattingHandler;

    @GetMapping("/")
    public String defaultRunning() {
        return "Chatting Server is Running";
    }

    @PostMapping("/send/message")
    public ResponseEntity<Void> sendMessage(@RequestHeader(value = "x-forward-useridx") String userIdx,
                                            @RequestHeader(value = "x-forward-email") String email,
                                            @RequestHeader(value = "x-forward-nickname") String nickname,
                                            @RequestBody ChattingData chattingData) throws IOException {
        //socket 통신
        //## 채팅을 친 유저의 정보는 x-forward-email, x-forward-nickname으로 알 수 있음.
        log.info("[send Message] chattingData presenterIdx : " + chattingData.getPresenterIdx() + " Message : " + chattingData.getTextMessage());
        chattingHandler.sendToPresenterChattingMessage(chattingData, userIdx, nickname);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
