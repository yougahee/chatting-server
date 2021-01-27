package com.morse.chatting_server.controller;

import com.morse.chatting_server.dto.ChattingMessage;
import com.morse.chatting_server.dto.Message;
import com.morse.chatting_server.dto.request.ChattingTextDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin("*")
@RestController
public class ChattingController {

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

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChattingMessage send(Message message) throws Exception {
        //String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new ChattingMessage("test", message.getMessage());
    }
}
