package com.cloud.emr.Main.Core.config;


import com.cloud.emr.Main.Core.common.exception.AuthUserResolver;
import com.cloud.emr.Main.Core.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthUserResolver authUserResolver;

    /**
     * CORS 설정
     * - 프론트엔드 개발 환경에서 요청을 허용하기 위해 사용됨
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }

    /**
     * 인터셉터 설정
     * - 요청 전/후 공통 로직을 처리할 때 사용됨 (ex. 인증, 로깅 등)
     * - 여기서는 인증 처리를 담당

     * - 추가할 작업 : 스웨거.

     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login", "/auth/register", "/callback/**");

    }

    /**
     * 커스텀 파라미터 리졸버 등록
     * - 컨트롤러 메서드 파라미터에 사용자 객체를 자동 주입해주는 로직 연결
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
    }
}
