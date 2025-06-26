package com.cloud.emr.Main.Core.common.wrapperfactory;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private boolean success = true;
    @Builder.Default
    private int code = 200;
    private String message;
    private T data;

    public static ResponseEntity<ApiResponse<Void>> of(String message) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .message(message)
                .build();
        return ResponseEntity.status(body.getCode()).body(body);
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
        return ResponseEntity.status(body.getCode()).body(body);
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(String message, T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(body.getCode()).body(body);
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(int code, String message, T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(code).body(body);
    }

    public static ResponseEntity<ApiResponse<Void>> fail(String message) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(false)
                .code(500)
                .message(message)
                .build();
        return ResponseEntity.status(body.getCode()).body(body);
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .success(false)
                .code(500)
                .message("요청 처리를 실패했습니다.")
                .data(data)
                .build();
        return ResponseEntity.status(body.getCode()).body(body);
    }

    public static ResponseEntity<ApiResponse<Void>> fail(int code, String message) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(code).body(body);
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(int code, String message, T data) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(code).body(body);
    }

}
