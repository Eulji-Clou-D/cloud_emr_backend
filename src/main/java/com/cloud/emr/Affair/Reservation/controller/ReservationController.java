package com.cloud.emr.Affair.Reservation.controller;

import com.cloud.emr.Affair.CheckIn.repository.CheckInRepository;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.repository.PatientRepository;
import com.cloud.emr.Affair.Patient.service.PatientService;
import com.cloud.emr.Affair.Reservation.dto.ReservationRegisterRequest;
import com.cloud.emr.Affair.Reservation.dto.ReservationReadResponse;
import com.cloud.emr.Affair.Reservation.dto.ReservationUpdateRequest;
import com.cloud.emr.Affair.Reservation.repository.ReservationRepository;
import com.cloud.emr.Affair.Reservation.service.ReservationService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    // 사용자 조회 메서드 사용을 위한 주입
    @Autowired
    private UserService userService;


    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    // 1. 환자 예약
    // 아직 미구현 (예약은 환자 접수가 되면 사라짐)
    // 구현 (예약은 이미 환자로 등록되어 있는 상태여야 함)
    @PostMapping("/register")
    public ResponseEntity<Object> registerReservation(@RequestBody ReservationRegisterRequest request, @RequestParam Long userId) {
        try {
            // 사용자 정보 확인
            UserEntity userEntity = userService.findUserById(userId);
            if (userEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
            }


            // 환자 정보 확인 
            PatientEntity patientEntity = patientService.findPatientByNo(request.getPatientNo());
            if (patientEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "등록되지 않은 환자입니다"));
            }


            // 예약 등록 처리
            ReservationReadResponse response = reservationService.createReservation(request, userEntity, patientEntity);

            // 성공적인 예약 처리
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "예약 성공",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "예약 실패",
                    "error", e.getMessage()
            ));
        }
    }


    // 2. 예약 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Object> getReservations() {
        List<ReservationReadResponse> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "message", "예약된 내역이 없습니다."
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "예약 목록 조회 성공",
                "data", reservations
        ));
    }

    // 3. 예약 변경 (일자 변경 가능, 에약 여부를 Y, N으로 변경함으로써 취소 가능)
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateReservation(
            @RequestParam Long reservationId,
            @RequestBody ReservationUpdateRequest updateRequest) {
        try {
            // 예약 변경 처리 서비스 호출
            ReservationReadResponse updatedReservation = reservationService.updateReservation(reservationId, updateRequest);

            // 예약 변경 정보 추출
            String reservationStatus = updatedReservation.getReservationYn().equals("Y") ? "날짜/시간 변경" : "예약 취소";

            // 변경된 예약 정보 반환
            return ResponseEntity.ok(Map.of(
                    "message", "예약 변경 성공",
                    "reservationStatus", reservationStatus,
                    "data", updatedReservation
            ));
        } catch (RuntimeException e) {
            // 예외 처리: 예약을 찾을 수 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "존재하지 않는 예약",
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            // 예외 처리: 기타 예기치 않은 오류 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "예약 변경 처리 중 오류 발생",
                    "error", e.getMessage()
            ));
        }
    }
}

