package com.morse.chatting_server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "chatting_log")
@Setter @Getter
public class ChattingLog {

	@Id
	public String roomIdx;

	public String userIdx;
	public String nickname;
	public String sendMessage;
	public String sendTime;

	public ChattingLog() {
		this.sendTime = convertNow();
	}

	public ChattingLog(String roomIdx, String userIdx, String nickname, String sendMessage) {
		this.roomIdx = roomIdx;
		this.userIdx = userIdx;
		this.nickname = nickname;
		this.sendMessage = sendMessage;
		this.sendTime = convertNow();
	}

	@Override
	public String toString() {
		return String.format(
				"ChattingLog[room_idx=%s, user_idx=%s, nickname='%s', send_message='%s', send_time='%s']",
				roomIdx, userIdx, nickname, sendMessage);
	}

	public String convertNow() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

}
