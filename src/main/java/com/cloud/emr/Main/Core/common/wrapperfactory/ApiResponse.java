package com.cloud.emr.Main.Core.common.wrapperfactory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private boolean success = true;
    @Builder.Default
    private int code = 200;
    private String message;
    private T data;

}
