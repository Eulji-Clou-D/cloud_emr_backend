package com.cloud.emr.Main.User.entity;

import com.cloud.emr.Main.User.status.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "User")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private RoleType Role;

    //음..? 일단 만들어 다듬어만 드릴게요  - 최경태
    @Column(name = "user_login_id")
    private String userLoginId;
    private String userPassword;



    /*
    @NotNull
    @Enumerated(EnumType.STRING)
    private HospitalCode hospitalCode;
    */

    private String userDeptName;


    private String userName;
    private String userGender;
    private String userAddress;
    private String userEmail;
    private String userTel;
    private LocalDateTime userBirth;
    private LocalDateTime userHireDate;

    @CreationTimestamp
    private LocalDateTime userRegisterDate;

    public UserEntity(Long userId) {
    }


    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
