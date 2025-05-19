package com.cloud.emr.Main.Auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 7; // 7일

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    /**
     * Redis에 사용자 ID를 key로 하여 리프레시 토큰을 저장
     *
     * @param userId 사용자 ID
     * @param refreshToken 저장할 리프레시 토큰
     */
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                getKey(userId),
                refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );
    }

    /**
     * Redis에서 사용자 ID에 해당하는 리프레시 토큰을 조회
     *
     * @param loginId 사용자 ID
     * @return 저장된 리프레시 토큰 (없으면 null)
     */
    public String getR교efreshToken(String loginId) {
        return redisTemplate.opsForValue().get(getKey(loginId));
    }

    /**
     * Redis에서 해당 사용자 ID의 리프레시 토큰을 삭제
     *
     * @param loginId 사용자 ID
     */
    public void deleteRefreshToken(String loginId) {
        redisTemplate.delete(getKey(loginId));
    }

    /**
     * 전달받은 리프레시 토큰이 Redis에 저장된 토큰과  검증
     *
     * @param username 사용자 ID
     * @param refreshToken 클라이언트로부터 받은 리프레시 토큰
     * @return 일치하면 true, 그렇지 않으면 false
     */
    public boolean validateRefreshToken(String username, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get(getKey(username));
        return refreshToken.equals(storedToken);
    }

    private String getKey(String username) {
        return REFRESH_TOKEN_PREFIX + username;
    }
}
