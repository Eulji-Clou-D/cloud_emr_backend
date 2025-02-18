package com.cloud.emr.Affair.Disability.controller;

import com.cloud.emr.Affair.Disability.dto.DisabilityRegisterRequest;
import com.cloud.emr.Affair.Disability.dto.DisabilityResponse;
import com.cloud.emr.Affair.Disability.dto.DisabilityUpdateRequest;
import com.cloud.emr.Affair.Disability.entity.DisabilityEntity;
import com.cloud.emr.Affair.Disability.service.DisabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/disability")
public class DisabilityController {

    @Autowired
    private DisabilityService disabilityService;

    // 1. 장애인 정보 등록 (해당하는 환자에 추가적으로 등록되는 형태이다.)
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerDisability(@RequestBody DisabilityRegisterRequest disabilityRegisterRequest) {
        try {
            // 장애인 정보 등록 처리 + 동일한 환자번호로 이미 장애인 정보 등록이 불가능
            DisabilityEntity responseData = disabilityService.registerDisability(disabilityRegisterRequest);

            // 환자 이름도 같이 가져오기
            String patientName = responseData.getPatientEntity().getPatientName();

            // 성공적인 등록 처리
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "등록 성공",
                    "patientName", patientName,
                    "data", responseData
            ));

        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "등록 실패",
                    "error", e.getMessage()
            ));
        }
    }


    // 2. 장애인 정보 조회 (환자번호에 해당하는 장애인 정보 조회)
    @GetMapping("/read/{patientNo}")
    public ResponseEntity<Map<String, Object>> viewDisability(@PathVariable Long patientNo) {
        try {
            DisabilityResponse disabilityResponse = disabilityService.readDisabilityByPatientNo(patientNo);
            if (disabilityResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "장애인 정보를 찾을 수 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "조회 성공",
                    "data", disabilityResponse
            ));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    // 3. 장애인 정보 전체 조회
    @GetMapping("/read/all")
    public ResponseEntity<Map<String, Object>> getAllDisabilityInfo() {
        try {
            List<DisabilityResponse> disabilityResponses = disabilityService.readAllDisabilities();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "전체 조회 성공",
                    "data", disabilityResponses
            ));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "전체 조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

    // 4. 장애인 정보 수정
    @PostMapping("/update/{patientNo}")
    public ResponseEntity<Map<String, Object>> updateDisability(
            @PathVariable Long patientNo,
            @RequestBody DisabilityUpdateRequest disabilityUpdateRequest) {

        try {
            DisabilityEntity updatedData = disabilityService.updateDisability(patientNo, disabilityUpdateRequest);

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
    @PostMapping("/delete/{patientNo}")
    public ResponseEntity<Map<String, Object>> deleteDisability(@PathVariable Long patientNo) {
        try {

            DisabilityResponse deletedDisability = disabilityService.deleteDisability(patientNo);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "삭제 성공",
                    "deletedDisability", deletedDisability
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "삭제 실패",
                    "error", e.getMessage()
            ));
        }
    }

}
