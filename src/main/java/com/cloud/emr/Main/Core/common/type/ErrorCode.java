package com.cloud.emr.Main.Core.common.type;

import lombok.Getter;
import org.hibernate.Internal;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT(400, HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, HttpStatus.METHOD_NOT_ALLOWED, "사용할 수 없는 기능입니다."),
    REQUEST_TIMEOUT(408, HttpStatus.REQUEST_TIMEOUT, "처리 시간이 너무 오래 걸립니다."),
    CONFLICT(409, HttpStatus.CONFLICT, "리소스 충돌이 발생했습니다."),
    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String getErrorName() {
        return this.name(); // 예: "INVALID_INPUT"
    }
}
