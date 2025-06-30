package com.cloud.emr.Affair.Holiday.controller;

import com.cloud.emr.Affair.Holiday.dto.HolidayRequest;
import com.cloud.emr.Affair.Holiday.dto.HolidayResponse;
import com.cloud.emr.Affair.Holiday.service.HolidayService;
import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.Core.common.wrapperfactory.ApiResponse;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService service;

    // 1. 휴일 등록
    @PostMapping
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody HolidayRequest req) {
        service.registerHoliday(req);
        return ApiResponse.of("휴일 등록 성공");
    }

    // 2. 휴일 수정
    @PutMapping("/{id}")
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<ApiResponse<HolidayResponse>> update(
            @PathVariable Long id,
            @RequestBody HolidayRequest req) {
        HolidayResponse response = service.updateHoliday(id, req);
        return ApiResponse.of("휴일 수정 성공", response);
    }

    // 3. 휴일 삭제
    @DeleteMapping("/{id}")
    @AuthRole(roles = {RoleType.ADMIN})
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.deleteHoliday(id);
        return ApiResponse.of("휴일 삭제 성공");
    }

    // 4. 휴일 목록 조회, 일 주 월 분기 년 기간에 따라 조회
    @GetMapping("/day/{date}")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<? extends ApiResponse<?>> readByDay(@PathVariable String date) {
        try {
            HolidayResponse response = service.readHolidayByDay(date);
            return ApiResponse.of("1개의 휴일 조회 성공", response);
        } catch (NoSuchElementException e){
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    @GetMapping("/week/{date}")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<? extends ApiResponse<?>> readByWeek(@PathVariable String date) {
        // yearMonthWeek format: yyyyMMWd (e.g., 202507W1)
        try {
            List<HolidayResponse> response = service.readHolidayByWeek(date);
            return ApiResponse.of(String.format("%d개의 휴일 조회 성공", response.size()), response);
        } catch (NoSuchElementException e){
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    @GetMapping("/month/{date}")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<? extends ApiResponse<?>> readByMonth(@PathVariable String date) {
        try {
            List<HolidayResponse> response = service.readHolidayByMonth(date);
            return ApiResponse.of(String.format("%d개의 휴일 조회 성공", response.size()), response);
        } catch (NoSuchElementException e){
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    @GetMapping("/quarter/{date}")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<? extends ApiResponse<?>> readByQuarter(@PathVariable String date) {
        // yearQuarter format: YYYYQn (e.g., 2025Q1)
        try {
            List<HolidayResponse> response = service.readHolidayByQuarter(date);
            return ApiResponse.of(String.format("%d개의 휴일 조회 성공", response.size()), response);
        } catch (NoSuchElementException e){
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    @GetMapping("/year/{date}")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<? extends ApiResponse<?>> readByYear(@PathVariable String date) {
        try {
            List<HolidayResponse> response = service.readHolidayByYear(date);
            return ApiResponse.of(String.format("%d개의 휴일 조회 성공", response.size()), response);
        } catch (NoSuchElementException e){
            return ApiResponse.fail(400, e.getMessage());
        }
    }

    // If security risk exists, tell me to change, like to post with request body HolidayRangeRequest
    @GetMapping("/range")
    @AuthRole(roles = {RoleType.ADMIN, RoleType.DOCTOR, RoleType.STAFF})
    public ResponseEntity<? extends ApiResponse<?>> readByRange(String startDate, String endDate) {
        try {
            List<HolidayResponse> response = service.readHolidayByRange(startDate, endDate);
            return ApiResponse.of(String.format("%s - %s : %d개의 휴일 조회 성공", startDate, endDate, response.size()), response);
        } catch (NoSuchElementException e){
            return ApiResponse.fail(400, e.getMessage());
        }
    }

}