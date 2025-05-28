package com.cloud.emr.Affair.Payment.service;

import com.cloud.emr.Affair.Payment.dto.PaymentRegisterRequest;
import com.cloud.emr.Affair.Payment.dto.PaymentResponse;
import com.cloud.emr.Affair.Payment.dto.PaymentUpdateRequest;
import com.cloud.emr.Affair.Payment.entity.PaymentEntity;
import com.cloud.emr.Affair.Payment.repository.PaymentRepository;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Treatment.Treatment.repository.TreatmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    // 1. 수납 내역 등록 (환자나 보험사에 대한 수납 정보만을 기록)
    @Transactional
    public PaymentResponse registerPayment(PaymentRegisterRequest request, TreatmentEntity treatmentEntity) {

        // 1. 총 수납 금액 (진료비)
       // Long totalAmount = treatmentEntity.getTreatmentTotalFee(); // 진료 총 금액

        // 2. 본인 부담 금액 = 진료 총 금액 - 보험사 지원 금액
        Long insuranceAmount = 10000L;  // 보험사 지원 금액 (예시로 고정)
       // Long selfPayAmount = totalAmount - insuranceAmount;  // 본인 부담 금액

        // 3. 현재 수납액 (지금까지 결제된 금액)
        // 최초 시점에서는 결제 내역이 아직 없기 때문 0으로 설정
        Long currentPaidAmount = 0L;
        
        // 4. 이번 수납액 (이번에 결제한 금액)
        // 이번에 결제할 금액은 본인 부담 금액에서 현재 수납액을 뺌
       // Long amountPaidNow = selfPayAmount - currentPaidAmount;  // 이번 결제 금액

        // 5. 남은 수납액 (본인 부담 금액 - 현재 수납액)
        //Long remainingAmount = selfPayAmount - currentPaidAmount;  // 남은 금액
/*

        // 결제 정보 생성
        PaymentEntity payment = PaymentEntity.builder()
                .treatmentEntity(treatmentEntity)
                .paymentTotalAmount(totalAmount) // 진료 총 금액 저장
                .paymentAmount(currentPaidAmount) // 이번 결제 금액
                .paymentCurrentMoney(amountPaidNow) // 현재까지 납부한 금액
                .paymentRemainMoney(totalAmount - remainingAmount) // 남은 금액 계산
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus("미납") // 최초 결제 등록 시에는 "미납" 상태로 설정
                .build();

        // 저장
        paymentRepository.save(payment);

 */
        return null;// new PaymentResponse(payment);
    }

/*
    // 결제 처리와 금액 변경
    @Transactional
    public PaymentResponse updatePayment(PaymentUpdateRequest request) {
        // 결제 정보 조회
        PaymentEntity payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException("해당 수납 내역을 찾을 수 없습니다."));

        // 1. 기존의 납부 금액과 이번에 추가된 금액 합산
        Long newTotalPaid = payment.getPaymentCurrentMoney() + request.getPaidAmount();

        // 2. 남은 금액 계산 (진료 총 금액에서 납부된 금액을 뺀 값)
        Long remainingAmount = payment.getPaymentTotalAmount() - newTotalPaid;

        // 3. 결제 상태 업데이트 (남은 금액에 따라 상태 변경)
        String paymentStatus = "미납";
        if (remainingAmount == 0) {
            paymentStatus = "완납";  // 모든 금액이 납부되면 "완납" 상태
        } else if (remainingAmount > 0) {
            paymentStatus = "분납";  // 남은 금액이 있으면 "분납" 상태
        }

        // 4. 결제 내역 업데이트 (새로운 객체 생성)
        PaymentEntity updatedPayment = PaymentEntity.builder()
                .paymentId(payment.getPaymentId())
                .treatmentEntity(payment.getTreatmentEntity())
                .paymentTotalAmount(payment.getPaymentTotalAmount())
                .paymentSelfPay(payment.getPaymentSelfPay())
                .paymentInsuranceMoney(payment.getPaymentInsuranceMoney())
                .paymentCurrentMoney(newTotalPaid)  // 이번 결제 금액 합산
                .paymentAmount(request.getPaidAmount()) // 이번 결제 금액
                .paymentRemainMoney(remainingAmount) // 남은 금액 계산
                .paymentStatus(paymentStatus) // 상태 업데이트
                .paymentMethod(request.getPaymentMethod()) // 결제 방법 업데이트
                .build();

        // 5. 결제 정보 저장
        paymentRepository.save(updatedPayment);

        // 6. 응답 반환 (결제 내역 정보 포함)
        return new PaymentResponse(updatedPayment);
    }


    // 단건 결제 조회 (결제 ID와 환자 ID를 동시에 고려)
    public PaymentResponse getPayment(Long paymentId, Long patientNo) {
        PaymentEntity payment = paymentRepository.findByPaymentIdAndTreatmentEntity_CheckInEntity_PatientEntity_PatientNo(paymentId, patientNo)

                .orElseThrow(() -> new EntityNotFoundException("해당 결제 내역을 찾을 수 없습니다."));

        return new PaymentResponse(payment);
    }

    // 전체 결제 내역 조회 (환자별 or 결제 ID별)
    public List<PaymentResponse> getAllPayments(Long patientNo, Long paymentId) {
        List<PaymentEntity> payments;

        if (patientNo != null && paymentId != null) {
            // 환자 번호와 결제 ID로 결제 내역 조회

            payments = paymentRepository.findByTreatmentEntity_CheckInEntity_PatientEntity_PatientNoAndPaymentId(patientNo, paymentId);

        } else if (patientNo != null) {
            // 환자 번호로 결제 내역 조회
            payments = paymentRepository.findByTreatmentEntity_CheckInEntity_PatientEntity_PatientNo(patientNo);
        } else if (paymentId != null) {
            // 결제 ID로 결제 내역 조회
            payments = paymentRepository.findById(paymentId).map(List::of).orElse(Collections.emptyList());
        } else {
            // 둘 다 없으면 전체 결제 내역 조회
            payments = paymentRepository.findAll();
        }

        return payments.stream().map(PaymentResponse::new).collect(Collectors.toList());
    }
*/
}
