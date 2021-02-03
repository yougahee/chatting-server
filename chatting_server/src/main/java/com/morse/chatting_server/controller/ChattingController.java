package com.morse.chatting_server.controller;

import com.morse.chatting_server.dto.request.ChattingData;
import com.morse.chatting_server.service.ChattingService;
import com.morse.chatting_server.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final ChattingService chattingService;
    private final JwtUtils jwtUtils;

    @GetMapping("/")
    public String defaultRunning() {
        return "Chatting Server is Running";
    }

    @GetMapping("/token")
    public long test(@RequestHeader(value = "token") String token) {
        return jwtUtils.isValidateToken(token);
    }

    @PostMapping("/send/message")
    public ResponseEntity<Void> sendMessage(@RequestHeader(value = "x-forward-useridx") String userIdx,
                                            @RequestHeader(value = "x-forward-email") String email,
                                            @RequestHeader(value = "x-forward-nickname") String nickname,
                                            @RequestBody ChattingData chattingData) throws IOException {

        log.info("[send Message] chattingData presenterIdx : " + chattingData.getPresenterIdx() + " Message : " + chattingData.getTextMessage());

        chattingService.sendToPresenterChattingMessage(chattingData, userIdx, nickname);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
