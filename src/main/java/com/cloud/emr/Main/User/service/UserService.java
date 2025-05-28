package com.cloud.emr.Main.User.service;

import com.cloud.emr.Main.User.dto.WaitUserResponse;

import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.cloud.emr.Main.User.type.RoleType;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 사용자 조회 메서드
    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null); // 유저가 없으면 null 반환
    }

    public List<WaitUserResponse> getUsersToBeApproved() {
        return userRepository.findAllByRole(RoleType.WAIT)
                .stream()
                .map(WaitUserResponse :: from)
                .toList(); // Role이 WAIT인 유저만 조회
    }

    public void changeUserRole(Long userId, RoleType roleType) {
        UserEntity user =  userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        user.setRole(roleType);
        userRepository.save(user);
    }

}
