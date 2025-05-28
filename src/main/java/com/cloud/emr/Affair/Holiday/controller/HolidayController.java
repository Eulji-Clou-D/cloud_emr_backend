package com.cloud.emr.Affair.Holiday.controller;

import com.cloud.emr.Affair.Holiday.dto.HolidayRequest;
import com.cloud.emr.Affair.Holiday.dto.HolidayResponse;
import com.cloud.emr.Affair.Holiday.service.HolidayService;
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
    public ResponseEntity<Map<String, Object>> register(@RequestBody HolidayRequest req) {
        HolidayResponse dto = service.registerHoliday(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "휴일 등록 성공",
                "data", dto
        ));
    }

    // 2. 휴일 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @RequestBody HolidayRequest req) {
        HolidayResponse dto = service.updateHoliday(id, req);
        return ResponseEntity.ok(Map.of(
                "message", "휴일 수정 성공",
                "data", dto
        ));
    }

    // 3. 휴일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        service.deleteHoliday(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                "message", "휴일 삭제 성공"
        ));
    }

    // 4. 휴일 목록 조회, 일 주 월 분기 년인지 여부를 period 받아서 동작
    // 하지만 현재는 일과 년만 받아서 동작하게 함
    @GetMapping("/{period}/{number}")
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