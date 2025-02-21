package com.cloud.emr.Affair.Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 수납 등록 요청
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRegisterRequest {
    private Long treatmentId;   // 진료 ID
    private Long treatmentFeeId; // 진료 요금 ID
    private String paymentMethod; // 결제 방법 (카드, 현금 등)

}
