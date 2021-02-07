package com.morse.chatting_server.dto.message;

import com.morse.chatting_server.utils.ResponseMessage;
import com.morse.chatting_server.utils.TimestampUtils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message {
    String timestamp;
    private String message;
    private Object data;

    public Message() {
        this.timestamp = TimestampUtils.getNow();
    }

    public Message(String message) {
        this.timestamp = TimestampUtils.getNow();
        this.message = message;
    }

    public Message(Object result) {
        this.timestamp = TimestampUtils.getNow();
        this.message = ResponseMessage.SUCCESS;
        this.data = result;
    }

    public Message(Object result, String message) {
        this.timestamp = TimestampUtils.getNow();
        this.message = message;
        this.data = result;
    }
}
