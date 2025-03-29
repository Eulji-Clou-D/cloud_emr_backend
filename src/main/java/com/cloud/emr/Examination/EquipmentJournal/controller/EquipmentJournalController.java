package com.cloud.emr.Examination.EquipmentJournal.controller;

import com.cloud.emr.Examination.EquipmentJournal.dto.EquipmentJournalRegisterRequest;
import com.cloud.emr.Examination.EquipmentJournal.dto.EquipmentJournalResponse;
import com.cloud.emr.Examination.EquipmentJournal.dto.EquipmentJournalUpdateRequest;
import com.cloud.emr.Examination.EquipmentJournal.service.EquipmentJournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentJournalController {

    private final EquipmentJournalService equipmentJournalService;

    public EquipmentJournalController(EquipmentJournalService equipmentJournalService) {
        this.equipmentJournalService = equipmentJournalService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerEquipment(@RequestBody EquipmentJournalRegisterRequest equipmentJournalRegisterRequest) {
        try {
            EquipmentEntity responseData = equipmentJournalService.registerEquipment(equipmentJournalRegisterRequest);

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



    @GetMapping("/read/{equipmentId}")
    public ResponseEntity<Map<String, Object>> viewEquipment(@PathVariable Long equipmentId) {
        try {
            EquipmentJournalResponse equipmentJournalResponse = equipmentJournalService.readEquipment(equipmentId);
            if (equipmentJournalResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "검사 정보를 찾을 수 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "조회 성공",
                    "data", equipmentJournalResponse
            ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/read/{equipmentName}")
    public ResponseEntity<Map<String, Object>> getEquipmentByEquipmentName(@PathVariable String equipmentName) {
        try {
            List<EquipmentJournalResponse> equipmentJournalRespons = equipmentJournalService.readEquipmentByEquipmentName(equipmentName);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "부분 조회 성공",
                    "data", equipmentJournalRespons
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
    public ResponseEntity<Map<String, Object>> getAllEquipmentInfo() {
        try {
            List<EquipmentJournalResponse> equipmentJournalRespons = equipmentJournalService.readAllEquipment();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "전체 조회 성공",
                    "data", equipmentJournalRespons
            ));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "전체 조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    // 4. 장애인 정보 수정
    @PostMapping("/update/{equipmentId}")
    public ResponseEntity<Map<String, Object>> updateEquipment(
            @PathVariable Long equipmentId,
            @RequestBody EquipmentJournalUpdateRequest equipmentJournalUpdateRequest) {

        try {
            EquipmentEntity updatedData = equipmentJournalService.updateEquipment(equipmentId, equipmentJournalUpdateRequest);

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
    @PostMapping("/delete/{equipmentId}")
    public ResponseEntity<Map<String, Object>> deleteEquipment(@PathVariable Long equipmentId) {
        try {

            EquipmentJournalResponse deletedEquipment = equipmentJournalService.deleteEquipment(equipmentId);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "삭제 성공",
                    "deletedEquipment", deletedEquipment
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "삭제 실패",
                    "error", e.getMessage()
            ));
        }
    }
}
