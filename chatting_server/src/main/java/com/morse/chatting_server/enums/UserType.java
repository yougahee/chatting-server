package com.morse.chatting_server.enums;

import lombok.Getter;

@Getter
public enum  UserType {
    PRESENTER("presenter"), VIEWER("viewer");

    private final String userType;
    UserType(String userType) { this.userType = userType; }
}
