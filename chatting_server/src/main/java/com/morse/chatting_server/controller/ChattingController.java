package com.morse.chatting_server.controller;

import com.morse.chatting_server.dto.request.ChattingTextDTO;
import com.morse.chatting_server.service.ChattingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final ChattingHandler chattingHandler;

    @GetMapping("/")
    public String defaultRunning() {
        return "Chatting Server is Running";
    }

    //## 네이밍 전체적으로 수정해야함
    @PostMapping("/send/message")
    public ResponseEntity<Void> sendMessage(@RequestHeader(value = "x-forward-email") String email,
                                            @RequestHeader(value = "x-forward-nickname") String nickname,
                                            @RequestBody ChattingTextDTO chattingData) {
        //## socket 통신
        //## 채팅을 친 유저의 정보는 x-forward-email, x-forward-nickname으로 알 수 있음.
        chattingHandler.sendChatting(chattingData, nickname);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
