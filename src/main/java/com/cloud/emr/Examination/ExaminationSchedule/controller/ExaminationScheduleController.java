package com.cloud.emr.Examination.ExaminationSchedule.controller;

import com.cloud.emr.Examination.ExaminationSchedule.dto.ExaminationScheduleRegisterRequest;
import com.cloud.emr.Examination.ExaminationSchedule.dto.ExaminationScheduleResponse;
import com.cloud.emr.Examination.ExaminationSchedule.dto.ExaminationScheduleUpdateRequest;
import com.cloud.emr.Examination.ExaminationSchedule.service.ExaminationScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/examination")
public class ExaminationScheduleController {

    private final ExaminationScheduleService examinationScheduleService;

    public ExaminationScheduleController(ExaminationScheduleService examinationScheduleService) {
        this.examinationScheduleService = examinationScheduleService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerExamination(@RequestBody ExaminationScheduleRegisterRequest examinationScheduleRegisterRequest) {
        try {
            ExaminationEntity responseData = examinationScheduleService.registerExamination(examinationScheduleRegisterRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "등록 성공",
                    "data", responseData
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "등록 실패",
                    "error", e.getMessage()
            ));
        }
    }



    @GetMapping("/read/{examinationId}")
    public ResponseEntity<Map<String, Object>> viewExamination(@PathVariable Long examinationId) {
        try {
            ExaminationScheduleResponse examinationScheduleResponse = examinationScheduleService.readExamination(examinationId);
            if (examinationScheduleResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "검사 정보를 찾을 수 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "조회 성공",
                    "data", examinationScheduleResponse
            ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/read/{equipmentID}")
    public ResponseEntity<Map<String, Object>> getExaminationByEquipmentId(@PathVariable Long equipmentID) {
        try {
            List<ExaminationScheduleResponse> examinationScheduleRespons = examinationScheduleService.readExaminationByEuipmentId(equipmentID);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "부분 조회 성공",
                    "data", examinationScheduleRespons
            ));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "부분 조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    // 3. 장애인 정보 전체 조회
    @GetMapping("/read/all")
    public ResponseEntity<Map<String, Object>> getAllExaminationInfo() {
        try {
            List<ExaminationScheduleResponse> examinationScheduleRespons = examinationScheduleService.readAllExamination();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "전체 조회 성공",
                    "data", examinationScheduleRespons
            ));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "전체 조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    // 4. 장애인 정보 수정
    @PostMapping("/update/{examinationId}")
    public ResponseEntity<Map<String, Object>> updateExamination(
            @PathVariable Long examinationId,
            @RequestBody ExaminationScheduleUpdateRequest examinationScheduleUpdateRequest) {

        try {
            ExaminationEntity updatedData = examinationScheduleService.updateExamination(examinationId, examinationScheduleUpdateRequest);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "수정 성공",
                    "data", updatedData
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "수정 실패",
                    "error", e.getMessage()
            ));
        }
    }


    // 5. 장애인 정보 삭제
    @PostMapping("/delete/{examinationId}")
    public ResponseEntity<Map<String, Object>> deleteExamination(@PathVariable Long examinationId) {
        try {

            ExaminationScheduleResponse deletedExamination = examinationScheduleService.deleteExamination(examinationId);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "삭제 성공",
                    "deletedExamination", deletedExamination
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "삭제 실패",
                    "error", e.getMessage()
            ));
        }
    }
}
