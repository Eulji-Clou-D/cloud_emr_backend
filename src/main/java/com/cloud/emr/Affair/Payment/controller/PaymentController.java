package com.cloud.emr.Affair.Payment.controller;

import com.cloud.emr.Affair.Payment.dto.PaymentRegisterRequest;
import com.cloud.emr.Affair.Payment.dto.PaymentResponse;
import com.cloud.emr.Affair.Payment.dto.PaymentUpdateRequest;
import com.cloud.emr.Affair.Payment.entity.PaymentEntity;
import com.cloud.emr.Affair.Payment.service.PaymentService;
import com.cloud.emr.Affair.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Affair.Treatment.repository.TreatmentRepository;
import com.cloud.emr.Affair.Treatment.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TreatmentService treatmentService;

    // 1. 수납 내역 등록 (환자나 보험사에 대한 수납 정보만을 기록)
    @PostMapping("/register")
    public ResponseEntity<Object> registerPayment(@RequestBody PaymentRegisterRequest paymentRegisterRequest, @RequestParam Long treatmentId) {
        try {
            // 1. 진료 조회
            TreatmentEntity treatmentEntity = treatmentService.findTreatmentById(treatmentId);
            if (treatmentEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "해당 진료를 찾을 수 없습니다."));
            }

            // 3. 결제 등록 서비스 호출
            PaymentResponse responseData = paymentService.registerPayment(paymentRegisterRequest, treatmentEntity);
            // 4. 결제 등록 성공 응답
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "접수 성공",
                    "data", responseData
            ));

        } catch (IllegalArgumentException e) {
            // 결제 금액이 총 금액을 초과한 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));

        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "접수 실패",
                    "error", e.getMessage()
            ));
        }
    }

    // 2. 부분 결제 및 완납
    @PostMapping("/update")
    public ResponseEntity<Object> updatePayment(@RequestBody PaymentUpdateRequest paymentUpdateRequest) {
        try {
            // 결제 업데이트 서비스 호출
            PaymentResponse response = paymentService.updatePayment(paymentUpdateRequest);

            // 응답
            return ResponseEntity.ok(Map.of(
                    "message", "결제 정보 업데이트 성공",
                    "data", response,
                    "status", "성공"
            ));
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "결제 정보 업데이트 실패",
                    "error", e.getMessage(),
                    "status", "실패"
            ));
        }
    }

    // 3. 결제내역 단건 조회 (결제 ID와 환자 ID를 동시에 고려)
    @GetMapping("/{paymentId}/patient/{patientNo}")
    public ResponseEntity<Object> getPayment(@PathVariable Long paymentId, @PathVariable Long patientNo) {
        try {
            // 결제 내역 조회 서비스 호출
            PaymentResponse response = paymentService.getPayment(paymentId, patientNo);

            // 응답
            return ResponseEntity.ok(Map.of(
                    "message", "결제 내역 조회 성공",
                    "data", response,
                    "status", "성공"
            ));
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "결제 내역을 찾을 수 없습니다.",
                    "error", e.getMessage(),
                    "status", "실패"
            ));
        }
    }

    // 4. 전체 결제 내역 조회 (환자별 or 전체 조회, 결제 ID 포함)
    @GetMapping("/all")
    public ResponseEntity<Object> getAllPayments(@RequestParam(required = false) Long patientNo, @RequestParam(required = false) Long paymentId) {
        try {
            // 전체 결제 내역 조회
            List<PaymentResponse> paymentResponses = paymentService.getAllPayments(patientNo, paymentId);

            // 응답
            return ResponseEntity.ok(Map.of(
                    "message", "결제 내역 조회 성공",
                    "data", paymentResponses
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "결제 내역 조회 실패",
                    "error", e.getMessage()
            ));
        }
    }

}
