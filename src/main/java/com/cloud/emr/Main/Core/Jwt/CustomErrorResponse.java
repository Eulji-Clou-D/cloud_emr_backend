package com.cloud.emr.Main.Core.Jwt;

public record CustomErrorResponse(
        int status,
        String message
) {
}
