package com.cloud.emr.Main.User.dto;

import com.cloud.emr.Main.User.type.RoleType;
import lombok.Getter;

@Getter
public class WaitApprovedRequest {
    private Long userId;
    private RoleType role;
}
