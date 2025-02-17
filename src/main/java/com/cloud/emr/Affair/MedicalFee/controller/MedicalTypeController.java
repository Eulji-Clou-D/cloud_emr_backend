package com.cloud.emr.Affair.MedicalFee.controller;

import com.cloud.emr.Affair.MedicalFee.dto.MedicalTypeRequest;
import com.cloud.emr.Affair.MedicalFee.dto.MedicalTypeResponse;
import com.cloud.emr.Affair.MedicalFee.entity.MedicalTypeEntity;
import com.cloud.emr.Affair.MedicalFee.service.MedicalTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/medicaltype")
public class MedicalTypeController {
    private final MedicalTypeService medicalTypeService;

    public MedicalTypeController(MedicalTypeService medicalTypeService) {
        this.medicalTypeService = medicalTypeService;
    }

    //진료 항목 등록 (항목 이름, 금액)
    @PostMapping("/register")
    public ResponseEntity<Object> registerMedicalType(@RequestBody MedicalTypeRequest request) {
        //존재하는 진료 항목 정보 확인 후 등록
        try {
            MedicalTypeEntity medicalTypeEntity = medicalTypeService.findMedicalTypeByName(request.getMedicalTypeName());
            if(medicalTypeEntity != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "message","이미 존재하는 진료항목입니다."
                ));
            }

            MedicalTypeResponse response = medicalTypeService.createMedicalType(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message","등록 성공",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message","등록 실패",
                    "error", e.getMessage()
            ));
        }
    }
    //진료 항목 수정 (항목 이름, 금액)
    @PostMapping("/update")
    public ResponseEntity<Object> updateMedicalType(@RequestBody MedicalTypeRequest request, @RequestParam Long medicalTypeId) {
        try {
            MedicalTypeResponse response = medicalTypeService.updateMedicalType(request, medicalTypeId);


            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
               "message","수정 성공",
               "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message","수정 실패",
                    "error", e.getMessage()
            ));
        }
    }

    //진료 항목 삭제 (이름 or id)
    @PostMapping("/delete")
    public ResponseEntity<Object> deleteMedicalType(@RequestParam Long medicalTypeId) {
        try {
            MedicalTypeResponse response = medicalTypeService.deleteById(medicalTypeId);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message","삭제 성공",
                    "data", response
            ));

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message","삭제 실패",
                    "error", e.getMessage()
            ));
        }
    }
}
