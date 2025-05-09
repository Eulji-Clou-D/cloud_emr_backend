package com.cloud.emr.Main.User.controller;

import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.Core.common.annotation.AuthUser;
import com.cloud.emr.Main.User.dto.WaitUserResponse;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.service.UserService;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/approved/waituser")
    @AuthRole(roles = {RoleType.ADMIN})
    public List<WaitUserResponse> getUsersToBeApproved(@AuthUser UserEntity userEntity) {
       return userService.getUsersToBeApproved();
    }


}
