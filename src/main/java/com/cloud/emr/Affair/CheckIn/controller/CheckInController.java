package com.cloud.emr.Affair.CheckIn.controller;

import com.cloud.emr.Affair.CheckIn.dto.CheckInListResponse;
import com.cloud.emr.Affair.CheckIn.dto.CheckInRequest;
import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Affair.CheckIn.repository.CheckInRepository;
import com.cloud.emr.Affair.CheckIn.service.CheckInService;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.repository.PatientRepository;
import com.cloud.emr.Affair.Patient.service.PatientService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    // 1. 접수 등록
    @PostMapping("/register")
    public ResponseEntity<Object> registerCheckIn(@RequestBody CheckInRequest checkInRequest, @RequestParam Long userId) {
        try {
            // 사용자 조회
            UserEntity userEntity = userService.findUserById(userId);
            if (userEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
            }

            // 이미 존재하는 환자여야 접수가 가능하도록
            PatientEntity patientEntity = patientService.findPatientByNo(checkInRequest.getPatientNo());
            if (patientEntity == null) {  // 환자 번호가 존재하지 않으면 접수 불가
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Patient not found"));
            }

            // 접수 등록 처리
            CheckInEntity newCheckIn = checkInService.createCheckIn(checkInRequest, userEntity, patientEntity);

            // 응답 데이터 생성 (userId만 포함)
            Map<String, Object> responseData = Map.of(
                    "checkInId", newCheckIn.getCheckInId(),
                    "patientNo", newCheckIn.getPatientEntity().getPatientNo(),
                    "userId", userEntity.getUserId(), // userEntity에서 userId만 추출
                    "checkInDate", newCheckIn.getCheckInDate(),
                    "checkInPurpose", newCheckIn.getCheckInPurpose()
            );

            // 성공적인 접수 처리
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "접수 성공",
                    "data", responseData
            ));

        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "접수 실패",
                    "error", e.getMessage()
            ));
        }
    }


    // 2. 접수 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Object> getCheckInList() {
        List<CheckInListResponse> checkInList = checkInService.getCheckInList();

        if (checkInList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "message", "목록에 접수가 없습니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "접수 목록 조회 성공",
                "data", checkInList
        ));
    }

    // 3. 접수 삭제
    @PostMapping("/cancel")
    public ResponseEntity<Object> cancelCheckIn(@RequestParam Long checkInId) {
        try {
            checkInService.cancelCheckIn(checkInId);  // checkInId를 전달받아 취소 처리

            // 성공적인 취소 처리
            return ResponseEntity.ok(Map.of(
                    "message", "접수 취소 완료",
                    "checkInId", checkInId
            ));
        } catch (RuntimeException e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "존재하지 않는 접수",
                    "error", e.getMessage()
            ));
        }
    }

}


/*
------------------------------------------------------------------------------

 회원가입 이후

 1. 접수 요청

 POST

 http://localhost:8081/api/checkin/register?userId=

 raw

 {
   "patientNo": "N0000001",
   "checkInPurpose": "정기검진"
 }

------------------------------------------------------------------------------

 2. 접수 목록 (하나라도 접수된게 있으면 조회 가능)

 GET

 http://localhost:8081/api/checkin/list

 none


------------------------------------------------------------------------------

 3. 접수 취소

  POST

  http://localhost:8081/api/checkin/cancel?checkInId=

  none

------------------------------------------------------------------------------

 */

