package com.cloud.emr.Main.service;

import com.cloud.emr.Main.dto.UserRegisterDTO;
import com.cloud.emr.Main.entity.UserEntity;
import com.cloud.emr.Main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 이 어노테이션이 누락되면 빈으로 등록되지 않음
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity registerUser(UserRegisterDTO userRegisterDTO) {
        // DTO에서 Entity로 변환
        UserEntity userEntity = userRegisterDTO.toUserEntity();

        // 데이터베이스에 저장
        return userRepository.save(userEntity);
    }
}
