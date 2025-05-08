package com.cloud.emr.Affair.Recess.controller;

import com.cloud.emr.Affair.Recess.dto.RecessRequest;
import com.cloud.emr.Affair.Recess.dto.RecessResponse;
import com.cloud.emr.Affair.Recess.service.RecessService;
import com.cloud.emr.Main.User.status.RoleType;
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
    @PostMapping
    public ResponseEntity<Map<String, Object>> register(@RequestBody RecessRequest req) {
        RecessResponse dto = service.registerRecess(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "휴진 등록 성공",
                "data", dto
        ));
    }

    // 2. 휴진 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestBody RecessRequest req) {
        RecessResponse dto = service.updateRecess(id, req);
        return ResponseEntity.ok(Map.of(
                "message", "휴진 수정 성공",
                "data", dto
        ));
    }

    // 3. 휴진 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        service.deleteRecess(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                "message", "휴진 삭제 성공"
        ));
    }

    // 4. 휴진 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> list(@RequestParam RoleType role) {
        List<RecessResponse> list = service.listByRole(role);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "message", "조회된 휴진이 없습니다."
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "휴진 목록 조회 성공",
                "data", list
        ));
    }
}