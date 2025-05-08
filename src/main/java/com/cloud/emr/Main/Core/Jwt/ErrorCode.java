package com.cloud.emr.Main.Core.Jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "엑세스 권한이 없습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
