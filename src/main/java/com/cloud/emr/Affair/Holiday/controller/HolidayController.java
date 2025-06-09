package com.cloud.emr.Affair.Holiday.controller;

import com.cloud.emr.Affair.Holiday.dto.HolidayRequest;
import com.cloud.emr.Affair.Holiday.dto.HolidayResponse;
import com.cloud.emr.Affair.Holiday.service.HolidayService;
import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService service;

    // 1. 휴일 등록
    @PostMapping
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<Void> register(@RequestBody HolidayRequest req) {
        service.registerHoliday(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 휴일 수정
    @PutMapping("/{id}")
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestBody HolidayRequest req) {
        ResponseEntity<HolidayResponse> response = service.updateHoliday(id, req);
        return ResponseEntity.ok(Map.of(
                "message", "휴일 수정 성공",
                "data", response
        ));
    }

    // 3. 휴일 삭제
    @DeleteMapping("/{id}")
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        service.deleteHoliday(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                "message", "휴일 삭제 성공"
        ));
    }

    // 4. 휴일 목록 조회, 일 주 월 분기 년인지 여부를 period 받아서 동작
    // 하지만 현재는 일과 년만 받아서 동작하게 함
    @GetMapping("/{period}/{number}")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<Map<String, Object>> list(@PathVariable String period, @PathVariable String number) {
        List<HolidayResponse> list = service.listByPeriod(period, number);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "message", "조회된 휴일이 없습니다."
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "휴일 목록 조회 성공",
                "data", list
        ));
    }

}