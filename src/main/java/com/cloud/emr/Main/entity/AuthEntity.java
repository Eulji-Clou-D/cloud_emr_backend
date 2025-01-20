package com.cloud.emr.Main.entity;

import com.cloud.emr.Main.status.AuthType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "Auth")
@Getter
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authId;

    @Enumerated(EnumType.STRING)
    private AuthType authName;
    // 사용자 역할 (ADMIN, DOCTOR, STAFF, PATIENT)

}

