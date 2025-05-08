package com.cloud.emr.Main.Core.common.interceptor;

import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j(topic = "AuthInterceptor")
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        AuthRole authCheck = method.getMethodAnnotation(AuthRole.class);

        if (authCheck != null) {
            UserEntity authUser = (UserEntity) request.getAttribute("authUser");

            if (authUser == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
                return false;
            }
            //유저가 단일 권한으로 제한 했지만 추후 복합권한고려
            RoleType userRole = authUser.getRole();
            boolean hasAccess = false;
            for (RoleType allowed : authCheck.roles()) {
                if (allowed == userRole) {
                    hasAccess = true;
                    break;
                }
            }

            if (!hasAccess) {
                log.warn("접근 거부: {} (요청 URI: {})", userRole, request.getRequestURI());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                return false;
            }
        }

        return true;
    }
}
