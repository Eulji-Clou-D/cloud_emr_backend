package com.cloud.emr.Examination.EquipmentInspection.controller;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.EquipmentInspection.dto.EquipmentInspectionRegisterRequest;
import com.cloud.emr.Examination.EquipmentInspection.dto.EquipmentInspectionResponse;
import com.cloud.emr.Examination.EquipmentInspection.dto.EquipmentInspectionUpdateRequest;
import com.cloud.emr.Examination.EquipmentInspection.service.EquipmentInspectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipmentinspection")
public class EquipmentInspectionController {

    private final EquipmentInspectionService equipmentInspectionService;

    public EquipmentInspectionController(EquipmentInspectionService equipmentInspectionService) {
        this.equipmentInspectionService = equipmentInspectionService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerEquipment(@RequestBody EquipmentInspectionRegisterRequest equipmentInspectionRegisterRequest) {
        try {
            EquipmentEntity responseData = equipmentInspectionService.registerEquipment(equipmentInspectionRegisterRequest);

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
            EquipmentInspectionResponse equipmentInspectionResponse = equipmentInspectionService.readEquipment(equipmentId);
            if (equipmentInspectionResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "검사 정보를 찾을 수 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "조회 성공",
                    "data", equipmentInspectionResponse
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
            List<EquipmentInspectionResponse> equipmentInspectionRespons = equipmentInspectionService.readEquipmentByEquipmentName(equipmentName);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "부분 조회 성공",
                    "data", equipmentInspectionRespons
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
            List<EquipmentInspectionResponse> equipmentInspectionRespons = equipmentInspectionService.readAllEquipment();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "전체 조회 성공",
                    "data", equipmentInspectionRespons
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
            @RequestBody EquipmentInspectionUpdateRequest equipmentInspectionUpdateRequest) {

        try {
            EquipmentEntity updatedData = equipmentInspectionService.updateEquipment(equipmentId, equipmentInspectionUpdateRequest);

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

            EquipmentInspectionResponse deletedEquipment = equipmentInspectionService.deleteEquipment(equipmentId);

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
