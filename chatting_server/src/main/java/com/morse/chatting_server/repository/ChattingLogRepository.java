package com.morse.chatting_server.repository;

import com.morse.chatting_server.model.ChattingLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChattingLogRepository extends MongoRepository<ChattingLog, String> {

	public ChattingLog findAllByRoomIdx(Long roomIdx);

	public List<ChattingLog> findByRoomIdx(Long roomIdx);
}
