package com.cloud.emr.Main.Auth.Jwt;

public record CustomErrorResponse(
        int status,
        String message
) {
}
