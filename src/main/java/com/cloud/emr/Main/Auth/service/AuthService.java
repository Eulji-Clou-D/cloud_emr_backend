package com.cloud.emr.Main.Auth.service;

import com.cloud.emr.Main.Auth.Dto.TokenResponse;
import com.cloud.emr.Main.Auth.Jwt.JwtUtil;
import com.cloud.emr.Main.Auth.Dto.LoginRequest;
import com.cloud.emr.Main.Auth.Dto.RegisterRequest;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public String register(RegisterRequest userRegisterRequest) {

        // 중복 검증: 이메일이나 사용자 계정이 이미 존재하는지 확인
        if (userRepository.existsByUserLoginId(userRegisterRequest.getUserLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 계정입니다.");
        }

        // DTO에서 Entity로 변환
        UserEntity userEntity = userRegisterRequest.toUserEntity();
        userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));

        // 데이터베이스에 저장
        return userRepository.save(userEntity).getUserLoginId();
    }


    /**
     * 로그인 처리
     * @param request : 로그인 요청 DTO
     * @return TokenResponse : 액세스/리프레시 토큰
     * @throws IllegalArgumentException : 로그인 실패 시
     */
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByUserLoginId(request.getUserLoginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Date now = new Date();
        String accessToken = jwtUtil.generateAccessToken(user.getUserId().toString(), now);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId().toString(), now);

        return TokenResponse.of(accessToken, refreshToken);
    }}
