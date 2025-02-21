package com.cloud.emr.Affair.Payment.repository;

import com.cloud.emr.Affair.Payment.entity.PaymentEntity;
import com.cloud.emr.Affair.Treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    // 결제 ID와 환자 번호로 결제 내역 조회
    Optional<PaymentEntity> findByPaymentIdAndTreatmentEntity_CheckInEntity_PatientEntity_PatientNo(Long paymentId, Long patientNo);

    // 환자 번호와 결제 ID로 결제 내역 조회
    List<PaymentEntity> findByTreatmentEntity_CheckInEntity_PatientEntity_PatientNoAndPaymentId(Long patientNo, Long paymentId);


    // 환자 번호로 결제 내역 조회
    List<PaymentEntity> findByTreatmentEntity_CheckInEntity_PatientEntity_PatientNo(Long patientNo);

    // 결제 ID로 결제 내역 조회
    Optional<PaymentEntity> findById(Long paymentId);

}
