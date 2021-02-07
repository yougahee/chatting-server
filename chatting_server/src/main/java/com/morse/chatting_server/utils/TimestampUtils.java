package com.morse.chatting_server.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimestampUtils {
	private static final String RESPONSE_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String VIDEO_TIMESTAMP_PATTERN = "yyyyMMddHHmmssSSS";

	private static final DateTimeFormatter responseDateTimeformatter = DateTimeFormatter.ofPattern(RESPONSE_TIMESTAMP_PATTERN);
	private static final DateTimeFormatter videoDateTimeformatter = DateTimeFormatter.ofPattern(VIDEO_TIMESTAMP_PATTERN);
	private static final SimpleDateFormat responseSimpleDateformatter = new SimpleDateFormat(RESPONSE_TIMESTAMP_PATTERN);

	private TimestampUtils() {
	}

	public static String getNow() {
		return convertResponseDateFormat(LocalDateTime.now());
	}

	public static String getVideoPrefix() {
		return convertVideoDateFormat(LocalDateTime.now());
	}

	public static String convertResponseDateFormat(LocalDateTime time) {
		return time.format(responseDateTimeformatter);
	}

	public static String convertResponseDateFormat(Date time) {
		return responseSimpleDateformatter.format(time);
	}

	public static String convertVideoDateFormat(LocalDateTime time) {
		return time.format(videoDateTimeformatter);
	}

}
