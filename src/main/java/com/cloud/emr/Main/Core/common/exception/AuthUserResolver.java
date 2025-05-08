package com.cloud.emr.Main.Core.common.exception;

import com.cloud.emr.Main.Core.Jwt.CustomAuthenticationException;
import com.cloud.emr.Main.Core.Jwt.JwtUtil;
import com.cloud.emr.Main.Core.common.annotation.AuthUser;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUserResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        boolean isMemberType = UserEntity.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && isMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String header = webRequest.getHeader("Authorization");
        String token = jwtUtil.resolveToken(header);

        UserEntity user =  userRepository.findById(jwtUtil.getUserIdFromToken(token)).orElseThrow(() -> {
                    log.error("JWT 인증 실패");
                    return new CustomAuthenticationException("JWT 인증 실패");
                }
        );

        //인터셉터에서도 접근 가능하도록 request에 저장
        webRequest.setAttribute("authUser", user, NativeWebRequest.SCOPE_REQUEST);

        return user;
    }
}
