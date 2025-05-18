package com.cloud.emr.Main.Core.common.interceptor;

import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.Arrays;

@Component
@Slf4j(topic = "AuthInterceptor")
public class AuthInterceptor implements HandlerInterceptor {


    /*
     해당 코드는 설명용으로 주석을 많이 달라놨습니다.
     사용예시
     •	@AuthRole() → WAIT제외하고 통과
	 •	@AuthRole(roles = {RoleType.ADMIN}) → 명시된 권한만 통과
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 형검사를하는 동시에 맞으면 HandlerMethod method 선언
        if (!(handler instanceof HandlerMethod method)) {
            return true;
            // method는 여기서 존재하지 않음
        }

        AuthRole authCheck = method.getMethodAnnotation(AuthRole.class);

        if (authCheck != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("인증 정보가 없습니다. 로그인 필요.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
                return false;
            }

            final RoleType[] allowedRoles;
            RoleType[] tempRoles = authCheck.roles();
            if (tempRoles.length == 0) {
                tempRoles = Arrays.stream(RoleType.values())
                        .filter(role -> role != RoleType.WAIT)
                        .toArray(RoleType[]::new);
            }
            allowedRoles = tempRoles;

            boolean hasAccess = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .map(auth -> auth.replace("ROLE_", ""))
                    .map(RoleType::valueOf)
                    .anyMatch(role -> Arrays.asList(allowedRoles).contains(role));

            if (!hasAccess) {
                log.warn("접근 거부: {} (요청 URI: {})", authentication.getAuthorities(), request.getRequestURI());

                response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                return false;
            }
        }

        return true;
    }
}
