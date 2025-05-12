package com.cloud.emr.Affair.DoctorTreatment.controller;


import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentRequest;
import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentResponse;
import com.cloud.emr.Affair.DoctorTreatment.entity.DoctorTreatmentEntity;
import com.cloud.emr.Affair.DoctorTreatment.service.DoctorTreatmentService;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.service.PatientService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class DoctorTreatmentController {
    private final DoctorTreatmentService doctorTreatmentService;
    private final PatientService patientService;
    private final UserService userService;

    public DoctorTreatmentController(DoctorTreatmentService doctorTreatmentService, PatientService patientService, UserService userService) {
        this.doctorTreatmentService = doctorTreatmentService;
        this.patientService = patientService;
        this.userService = userService;
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
            //의사는 겹쳐도 상관 없음. reqStartTime, reqEndTime은 겹치면 안 됨.
            Long reqUserId = doctorTreatmentRequest.getUserId();

            LocalDateTime reqStartTime = doctorTreatmentRequest.getDoctorTreatmentStart();
            LocalDateTime reqEndTime = doctorTreatmentRequest.getDoctorTreatmentEnd();

            //의사 정보 가져오기
            UserEntity targetUser = userService.findUserById(doctorTreatmentRequest.getUserId());
            if(targetUser == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                                "message", "해당하는 유저번호가 없습니다.",
                                "data", doctorTreatmentRequest.getUserId()
                        )
                );
            }

            //환자 정보 가져오기
            PatientEntity targetPatient = patientService.findPatientByNo(doctorTreatmentRequest.getPatientNo());
            if(targetPatient == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of(
                                "message", "해당하는 환자번호가 없습니다.",
                                "data", doctorTreatmentRequest.getPatientNo()
                        )
                );
            }

            //일정 겹치는지 확인
            if(doctorTreatmentService.isOverlap(reqUserId, reqStartTime, reqEndTime)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        Map.of(
                                "message", "일정이 겹칩니다.",
                                "data", doctorTreatmentRequest
                        )
                );
            }

            DoctorTreatmentResponse doctorTreatmentResponse = doctorTreatmentService.createTreatment(doctorTreatmentRequest, targetUser, targetPatient);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message","진료 일정 생성 성공",
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
    // 환자 간의 일정이 여러개라도, 화면에 뿌려질테고, 해당 스케쥴 번호를 통해 수정할 수 있음
//    @PostMapping("/update")
//    public ResponseEntity<Object> updateDoctorTreatment(@RequestParam Long userId, @RequestBody DoctorTreatmentRequest doctorTreatmentRequest) {
//        try{
//
//
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
//                    "message", "진료 일정 수정 실패",
//                    "data", e.getMessage()
//            ));
//        }
//    }
}
