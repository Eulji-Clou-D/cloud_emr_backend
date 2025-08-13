package com.cloud.emr.Main.Core.common.wrapperfactory;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public static ResponseEntity<BaseResponse<Void>> makeVoidResponse(BaseResponse<Void> response){
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public static <T> ResponseEntity<BaseResponse<T>> makeGenericResponse(BaseResponse<T> response){
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
