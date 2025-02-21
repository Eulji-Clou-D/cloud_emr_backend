package com.cloud.emr.Affair.Payment.dto;

// 부분 결제나 추가 결제
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateRequest {

    private Long paymentId;
    private Long paidAmount; // 결제 금액
    private String paymentMethod;

}