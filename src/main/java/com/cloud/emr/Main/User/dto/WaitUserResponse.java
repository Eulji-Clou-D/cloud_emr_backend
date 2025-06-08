package com.cloud.emr.Main.User.dto;

import com.cloud.emr.Main.User.entity.UserEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WaitUserResponse {
    Long userId;
    String loginId;
    String name;
    String email;
    String deptName;
    String gender;
    String telNum;
    LocalDateTime hireDate;

    public WaitUserResponse(Long userId, String loginId, String name, String email, String deptName, String gender, String telNum) {
        this.userId = userId;
        this.loginId = loginId;
        this.name = name;
        this.email = email;
        this.deptName = deptName;
        this.gender = gender;
        this.telNum = telNum;
    }
    public static WaitUserResponse from(UserEntity userEntity) {
        return new WaitUserResponse(
                userEntity.getId(),
                userEntity.getLoginId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getDepartment().getName(),
                userEntity.getGender().toString(),
                userEntity.getTelNum()
        );
    }
}
