package com.cloud.emr.Main.Core.Jwt;


import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserRepository userRepository;


    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader(AUTHORIZATION_HEADER);
            String token = jwtUtil.resolveToken(header);

            // 토큰이 있을 때만 인증 처리
            if (StringUtils.hasText(token)) {
                if (jwtUtil.validateToken(token)) {
                    Authentication authentication = jwtUtil.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                  
                    log.info("Security Context에 인증 정보를 저장 성공. 인증된 사용자: {}, 권한: {}", authentication.getPrincipal(), authentication.getAuthorities());

                    // 세션에 role포함
                    // Long userId = jwtUtil.getUserIdFromToken(token);
                    // UserEntity user = userRepository.findById(userId).orElseThrow(()-> new CustomAuthenticationException("존재하지 않은 유저"));
                    // request.setAttribute("authUser", user);

                } else {
                    throw new CustomAuthenticationException("유효하지 않은 토큰입니다.");
                }
            }

            // 항상 필터 체인 호출
            filterChain.doFilter(request, response);

        } catch (CustomAuthenticationException e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
            jwtAuthenticationEntryPoint.commence(request, response, e);
        }
    }
}