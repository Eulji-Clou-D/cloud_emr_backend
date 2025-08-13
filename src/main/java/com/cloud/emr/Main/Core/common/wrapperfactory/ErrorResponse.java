package com.cloud.emr.Main.Core.common.wrapperfactory;

import com.cloud.emr.Main.Core.common.type.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse<T> extends BaseResponse<T> {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public static ErrorResponse<Void> fail(String message) {
        return ErrorResponse.<Void>builder()
                .message(message)
                .build();
    }

    public static <T> ErrorResponse<T> fail(T data) {
        return ErrorResponse.<T>builder()
                .message("요청이 실패했습니다.")
                .data(data)
                .build();
    }

    public static <T> ErrorResponse<T> fail(String message, T data) {
        return ErrorResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }

    public static ErrorResponse<Void> fail(HttpStatus status, String message) {
        return ErrorResponse.<Void>builder()
                .status(status)
                .message(message)
                .build();
    }

    public static <T> ErrorResponse<T> fail(HttpStatus status, String message, T data) {
        return ErrorResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static ErrorResponse<Void> fail(ErrorCodeEnum errorCodeEnum) {
        return ErrorResponse.<Void>builder()
                .status(errorCodeEnum.getStatus())
                .message(errorCodeEnum.getMessage())
                .build();
    }

    public static <T> ErrorResponse<T> fail(ErrorCodeEnum errorCodeEnum, T data) {
        return ErrorResponse.<T>builder()
                .status(errorCodeEnum.getStatus())
                .message(errorCodeEnum.getMessage())
                .data(data)
                .build();
    }
}
