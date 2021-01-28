package com.morse.chatting_server.repository;

import com.morse.chatting_server.model.ChattingLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChattingLogRepository extends MongoRepository<ChattingLog, String> {

	Optional<ChattingLog> findAllByRoomIdx(String roomIdx);
}
