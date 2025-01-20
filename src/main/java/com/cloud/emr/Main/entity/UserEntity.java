package com.cloud.emr.Main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "User")
@Builder
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    private AuthEntity authId;

    /*
    @NotNull
    @Enumerated(EnumType.STRING)
    private HospitalCode hospitalCode;
    */

    private String userDeptName;
    private String userLoginId;
    private String userPassword;
    private String userName;
    private String userGender;
    private String userAddress;
    private String userEmail;
    private String userTel;
    private LocalDateTime userBirth;
    private LocalDateTime userHireDate;

    @CreationTimestamp
    private LocalDateTime userRegisterDate;

    public UserEntity getUserEntity() {
        return UserEntity.builder()
                .authId(this.authId)
                // .hospitalCode(this.hospitalCode)
                .userDeptName(this.userDeptName)
                .userLoginId(this.userLoginId)
                .userPassword(this.userPassword)
                .userName(this.userName)
                .userGender(this.userGender)
                .userAddress(this.userAddress)
                .userEmail(this.userEmail)
                .userTel(this.userTel)
                .userBirth(this.userBirth)
                .userHireDate(this.userHireDate)
                .build();
    }

}
