package com.cloud.emr.Affair.Patient.controller;


import com.cloud.emr.Affair.Patient.dto.PatientRegisterRequest;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.repository.PatientRepository;
import com.cloud.emr.Affair.Patient.service.PatientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientService patientService;

    @Transactional
    public Long generatePatientNo() {
        // DB에서 가장 최근의 환자 번호를 조회
        // 가장 최근의 환자 번호 조회
        Optional<PatientEntity> lastPatient = patientRepository.findTopByOrderByPatientNoDesc();

        // 테이블이 비어있으면 1부터 시작

        return lastPatient.map(p -> p.getPatientNo() + 1).orElse(1L);
    }

    // 문자열 포맷으로 8자리 유지하는 메서드
    public String formatPatientNo(Long patientNo) {
        return String.format("%08d", patientNo);
    }

    //환자등록
    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody @Valid PatientRegisterRequest patientRegisterRequest) throws Exception {
        Long patientNo = generatePatientNo();
        PatientEntity patientEntity = PatientEntity.builder()
                .patientNo(patientNo)
                .patientName(patientRegisterRequest.getPatientName())
                .patientRrn(patientRegisterRequest.getPatientRrn())
                .patientGender(patientRegisterRequest.getPatientGender())
                .patientBirth(patientRegisterRequest.getPatientBirth())
                .patientAddress(patientRegisterRequest.getPatientAddress())
                .patientEmail(patientRegisterRequest.getPatientEmail())
                .patientTel(patientRegisterRequest.getPatientTel())
                .patientForeign(patientRegisterRequest.getPatientForeign())
                .patientPassport(patientRegisterRequest.getPatientPassport())
                .patientHypassYN(patientRegisterRequest.getPatientHypassYN())
                .patientLastVisit(patientRegisterRequest.getPatientLastVisit())
                .guardian(patientRegisterRequest.getGuardian())
                .build();

        try {
            patientRepository.save(patientEntity);
            return ResponseEntity.ok(
                    Map.of("message", "환자가 정상적으로 등록되었습니다.", "patientNo", formatPatientNo(patientNo))
            );
        } catch (ObjectOptimisticLockingFailureException e) {
            // 충돌 처리 로직
            throw new Exception("다른 사용자가 이미 데이터를 수정했습니다.");
        }
    }


    //환자 검색
    @PostMapping("/search")
    public ResponseEntity<?> searchPatient(@RequestParam("patientName") String patientName) {
        try {
            List<PatientEntity> patients = PatientService.searchPatientByName(patientName);

            if (patients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("해당 이름으로 등록된 환자가 없습니다 : " + patientName);
            }

            // patientNo 필드만 추출
            // 출력 JSON에 동명이인을 포함한 모든 OOO을 찾기 위해, patientNo값을 가져옴
            List<String> patientNos = patients.stream()
                    .map(patient -> formatPatientNo(patient.getPatientNo()))
                    .toList();

//            List<PatientSearchResponse> response = patients.stream()
//                    .map(patient -> new PatientSearchResponse(
//                            patient.getPatientNo(),
//                            patient.getPatientName()
//                    ))
//                    .toList();

            return ResponseEntity.ok(
                    Map.of("message", patientName + "를 찾았습니다." ,
                                "no", patientNos
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    //환자정보조회
    @PostMapping("/detail")
    public ResponseEntity<?> detailPatient(@RequestParam("patientNo") Long patientNo) {
        try {
            PatientEntity patient = patientService.findPatientByNo(patientNo);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "환자 정보를 불러왔습니다.",
                            "patient", patient
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", e.getMessage()
                    ));
        }
    }
}
