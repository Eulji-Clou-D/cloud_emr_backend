package com.cloud.emr.Main.User.controller;

import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.Core.common.annotation.AuthUser;
import com.cloud.emr.Main.User.dto.WaitApprovedRequest;
import com.cloud.emr.Main.User.dto.WaitUserResponse;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.service.UserService;
import com.cloud.emr.Main.User.type.RoleType;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/approved/waitlist")
    @AuthRole(roles = {RoleType.ADMIN})
    public List<WaitUserResponse> getUsersToBeApproved() {
        return userService.getUsersToBeApproved();
    }

    @PostMapping("/approved/role")
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<Object> approvedRole(@Valid @RequestBody WaitApprovedRequest waitApprovedRequest) {
        try {
            userService.changeUserRole(waitApprovedRequest.getUserId(),waitApprovedRequest.getRole());
            return ResponseEntity.ok().body("승인 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("권한 변경 실패: " + e.getMessage());
        }
    }

}
