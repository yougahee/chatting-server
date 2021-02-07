package com.morse.chatting_server.model;

import com.morse.chatting_server.utils.TimestampUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatting_log")
@Setter @Getter
public class ChattingLog {

	@Id
	private Long roomIdx;

	private Long presenterIdx;
	private Long userIdx;
	private String nickname;
	private String sendMessage;
	private String sendTime;

	public ChattingLog() {
		this.sendTime = TimestampUtils.getNow();
	}

	public ChattingLog(Long roomIdx, Long presenterIdx, Long userIdx, String nickname, String sendMessage) {
		this.roomIdx = roomIdx;
		this.presenterIdx = presenterIdx;
		this.userIdx = userIdx;
		this.nickname = nickname;
		this.sendMessage = sendMessage;
		this.sendTime = TimestampUtils.getNow();
	}

	@Override
	public String toString() {
		return String.format(
				"ChattingLog[presenter_idx=%s, user_idx=%s, nickname='%s', send_message='%s', send_time='%s']",
				presenterIdx, userIdx, nickname, sendMessage, sendTime);
	}
}
