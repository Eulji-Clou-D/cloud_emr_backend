package com.cloud.emr.Affair.DoctorTreatment.controller;


import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentRequest;
import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentResponse;
import com.cloud.emr.Affair.DoctorTreatment.service.DoctorTreatmentService;
import com.cloud.emr.Affair.Patient.service.PatientService;
import com.cloud.emr.Main.User.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/schedule")
public class DoctorTreatmentController {
    private final DoctorTreatmentService doctorTreatmentService;

    public DoctorTreatmentController(DoctorTreatmentService doctorTreatmentService, PatientService patientService, UserService userService) {
        this.doctorTreatmentService = doctorTreatmentService;
    }
    /*
     * 1. 일정 생성
     * 2. 일정 수정
     * 3. 일정 삭제
     * 4. user별 일정 조회
     * 5. 날짜별 일정 조회
     *
     * 나중에 User 어노테이션으로 튜닝 필요
     * */

    //일정 생성
    @PostMapping("/create")
    public ResponseEntity<Object> createDoctorTreatment(@RequestBody DoctorTreatmentRequest doctorTreatmentRequest) {
        try {

            DoctorTreatmentResponse doctorTreatmentResponse = doctorTreatmentService.createTreatment(doctorTreatmentRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "진료 일정 생성 성공",
                    "data", doctorTreatmentResponse
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "진료 일정 생성 실패",
                    "data", e.getMessage()
            ));
        }
    }

    //일정 수정
    // 화면에 뿌려지는 스케쥴 목록들 중 하나를 수정하고자 할 때, 해당 스케쥴 번호를 이용할 것임.
    // 따라서 해당 스케쥴의 소유자(userId)만 확인 후에 수정하는 절차를 밟겠음.
    @PostMapping("/update/{userId}/{doctorTreatmentId}")
    public ResponseEntity<Object> updateDoctorTreatment(@PathVariable Long userId, @PathVariable Long doctorTreatmentId, @RequestBody DoctorTreatmentRequest doctorTreatmentRequest) {
        try {
                DoctorTreatmentResponse response = doctorTreatmentService.updateDoctorTreatment(userId, doctorTreatmentId, doctorTreatmentRequest);

                if (response == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                            "message", "일정 생성 불가(일정이 겹치거나, 존재하지 않는 환자)"
                    ));
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                        "message", "일정 수정 성공",
                        "data", response
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "진료 일정 수정 실패",
                    "data", e.getMessage()
            ));
        }
    }

    //일정 삭제
    @PostMapping("/delete/{doctorTreatmentId}")
    public ResponseEntity<Object> deleteDoctorTreatment(@PathVariable Long doctorTreatmentId) {
        try {
            DoctorTreatmentResponse response = doctorTreatmentService.deleteById(doctorTreatmentId);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "message", "삭제 성공",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "스케쥴 삭제 실패",
                    "data", e.getMessage()
            ));
        }
    }


    //user별 일정 조회
    @GetMapping("/search/{userId}")
    public ResponseEntity<Object> getAllDoctorTreatmentById(@PathVariable Long userId) {
        List<DoctorTreatmentResponse> doctorTreatmentList = doctorTreatmentService.getAllDoctorTreatmentByUserId(userId);

        if(doctorTreatmentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "message","해당 유저는 일정이 없습니다."
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message",userId+"의 일정 조회 성공",
                "data", doctorTreatmentList
        ));
    }
}