package com.cloud.emr.Affair.Recess.controller;

import com.cloud.emr.Affair.Recess.dto.RecessRequest;
import com.cloud.emr.Affair.Recess.dto.RecessResponse;
import com.cloud.emr.Affair.Recess.service.RecessService;
import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.Core.common.annotation.AuthUser;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recess")
@RequiredArgsConstructor
public class RecessController {

    private final RecessService service;

    // 1. 휴진 등록
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody RecessRequest recessRequest, @AuthUser UserEntity user) {

        RecessResponse recessResponse = service.registerRecess(user, recessRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "휴진 등록 성공",
                "data", recessResponse
        ));
    }



    // 2. 휴진 수정
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id, @RequestBody RecessRequest recessRequest, @AuthUser UserEntity user) {

        RecessResponse recessResponse = service.updateRecess(user, id, recessRequest);

        return ResponseEntity.ok(Map.of(
                "message", "휴진 수정 성공",
                "data", recessResponse
        ));
    }



    // 3. 휴진 삭제
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable Long id, @AuthUser UserEntity user) {

        service.deleteRecess(user, id);

        return ResponseEntity.ok(Map.of(
                "message", "휴진 삭제 성공"
        ));
    }



    // 4. 휴진 목록 조회
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@AuthUser UserEntity user) {
        List<RecessResponse> list = service.listByRole(user, user.getRole());

        if (list.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "조회된 휴진이 없습니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "휴진 목록 조회 성공",
                "data", list
        ));
    }


}
