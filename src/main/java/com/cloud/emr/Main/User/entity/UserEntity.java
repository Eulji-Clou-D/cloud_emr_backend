package com.cloud.emr.Main.User.entity;

import com.cloud.emr.Main.User.type.Gender;
import com.cloud.emr.Main.User.type.RoleType;
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
    @Column(name = "id")
    private Long id;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(name = "login_id")
    private String loginId;
    private String password;



    /*
    @NotNull
    @Enumerated(EnumType.STRING)
    private HospitalCode hospitalCode;
    */

    private String deptName;


    private String name;
    private Gender gender;
    private String address;
    private String email;
    private String telNum;
    private LocalDateTime birth;
    private LocalDateTime hireDate;

    @CreationTimestamp
    private LocalDateTime registerDate;

    public UserEntity(Long userId) {
    }


    public void setUserPassword(String Password) {
        this.password = Password;
    }

}
