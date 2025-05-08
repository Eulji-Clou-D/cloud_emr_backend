package com.cloud.emr.Main.Core.Jwt;

import com.cloud.emr.Main.Auth.Dto.TokenResponse;
import com.cloud.emr.Main.Auth.service.RefreshTokenService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
    private static final String BEARER_PREFIX = "Bearer ";

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenResponse generateTokens(Long userId) {
        Date date = new Date();
        String userIdString = String.valueOf(userId);

        String accessToken = generateAccessToken(userIdString, date);
        String refreshToken = generateRefreshToken(userIdString, date);

        refreshTokenService.saveRefreshToken(userIdString, refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

    public String generateAccessToken(String userId, Date date) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", "USER")
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(String userId, Date date) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException exception) {
            log.error("JWT가 유효하지 않습니다.");
            throw new CustomAuthenticationException("지원하지 않는 JWT 형식입니다.");
        } catch (MalformedJwtException exception) {
            log.error("JWT가 유효하지 않습니다.");
            throw new CustomAuthenticationException("잘못된 JWT 형식입니다.");
        } catch (ExpiredJwtException exception) {
            log.error("JWT가 만료되었습니다.");
            throw new CustomAuthenticationException("JWT가 만료되었습니다.");
        } catch (IllegalArgumentException exception) {
            log.error("JWT가 null이거나 비어 있거나 공백만 있습니다.");
            throw new CustomAuthenticationException("JWT가 비어있거나 잘못되었습니다.");
        } catch (Exception exception) {
            log.error("JWT 검증에 실패했습니다.", exception);
            throw new CustomAuthenticationException("JWT 검증에 실패했습니다.");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserEntity user = userRepository.findById(Long.valueOf(claims.getSubject())).
                orElseThrow();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
        return new UsernamePasswordAuthenticationToken(user.getLoginId(), null, authorities);
    }

    public String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
