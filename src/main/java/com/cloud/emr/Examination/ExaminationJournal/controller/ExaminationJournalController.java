package com.cloud.emr.Examination.ExaminationJournal.controller;

import com.cloud.emr.Examination.ExaminationJournal.dto.ExaminationJournalRegisterRequest;
import com.cloud.emr.Examination.ExaminationJournal.dto.ExaminationJournalResponse;
import com.cloud.emr.Examination.ExaminationJournal.dto.ExaminationJournalUpdateRequest;
import com.cloud.emr.Examination.ExaminationJournal.service.ExaminationJournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/examination")
public class ExaminationJournalController {

    private final ExaminationJournalService examinationJournalService;

    public ExaminationJournalController(ExaminationJournalService examinationJournalService) {
        this.examinationJournalService = examinationJournalService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerExamination(@RequestBody ExaminationJournalRegisterRequest examinationJournalRegisterRequest) {
        try {
            ExaminationEntity responseData = examinationJournalService.registerExamination(examinationJournalRegisterRequest);

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
            ExaminationJournalResponse examinationJournalResponse = examinationJournalService.readExamination(examinationId);
            if (examinationJournalResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "검사 정보를 찾을 수 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "조회 성공",
                    "data", examinationJournalResponse
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
            List<ExaminationJournalResponse> examinationJournalRespons = examinationJournalService.readExaminationByEuipmentId(equipmentID);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "부분 조회 성공",
                    "data", examinationJournalRespons
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
            List<ExaminationJournalResponse> examinationJournalRespons = examinationJournalService.readAllExamination();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "전체 조회 성공",
                    "data", examinationJournalRespons
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
            @RequestBody ExaminationJournalUpdateRequest examinationJournalUpdateRequest) {

        try {
            ExaminationEntity updatedData = examinationJournalService.updateExamination(examinationId, examinationJournalUpdateRequest);

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

            ExaminationJournalResponse deletedExamination = examinationJournalService.deleteExamination(examinationId);

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
