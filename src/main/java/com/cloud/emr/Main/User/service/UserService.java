package com.cloud.emr.Main.User.service;

import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 이 어노테이션이 누락되면 빈으로 등록되지 않음
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 사용자 조회 메서드
    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null); // 유저가 없으면 null 반환
    }

}
