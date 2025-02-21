package com.cloud.emr.Affair.Payment.dto;

import com.cloud.emr.Affair.Payment.entity.PaymentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 수납 응답
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private Long treatmentId;
    private String paymentStatus;
    private Long paymentTotalAmount;
    private Long paymentSelfPay;
    private Long paymentInsuranceMoney;
    private Long paymentCurrentMoney;
    private Long paymentAmount;
    private Long paymentRemainMoney;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    private String paymentMethod;

    public PaymentResponse(PaymentEntity paymentEntity) {
        this.paymentId = paymentEntity.getPaymentId();
        this.treatmentId = paymentEntity.getTreatmentEntity().getTreatmentId();
        this.paymentStatus = paymentEntity.getPaymentStatus();
        this.paymentTotalAmount = paymentEntity.getPaymentTotalAmount();
        this.paymentSelfPay = paymentEntity.getPaymentSelfPay();
        this.paymentInsuranceMoney = paymentEntity.getPaymentInsuranceMoney();
        this.paymentCurrentMoney = paymentEntity.getPaymentCurrentMoney();
        this.paymentAmount = paymentEntity.getPaymentAmount();
        this.paymentRemainMoney = paymentEntity.getPaymentRemainMoney();
        this.paymentDate = paymentEntity.getPaymentDate();
        this.paymentMethod = paymentEntity.getPaymentMethod();
    }
}