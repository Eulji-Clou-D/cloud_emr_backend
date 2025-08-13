package com.cloud.emr.Main.Core.common.wrapperfactory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> extends BaseResponse<T> {

    private HttpStatus status = HttpStatus.OK;

    public static ApiResponse<Void> of(String message) {
        return ApiResponse.<Void>builder()
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> of(T data) {
        return ApiResponse.<T>builder()
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> of(String message, T data) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> of(HttpStatus status, String message) {
        return ApiResponse.<Void>builder()
                .status(status)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }



}
