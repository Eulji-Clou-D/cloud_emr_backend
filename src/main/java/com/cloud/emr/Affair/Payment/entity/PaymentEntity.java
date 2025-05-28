package com.cloud.emr.Affair.Payment.entity;

import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Payment")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {

    // 수납 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    // 비식별관계 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id", nullable = false)
    private TreatmentEntity treatmentEntity;

    // 수납여부 (완납/분납/미납)
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    // 총 수납 금액 (진료비 + 처치비 + 등등...)
    @Column(name = "payment_total_amount")
    private Long paymentTotalAmount;

    // 본인 부담 금액
    @Column(name = "payment_self_pay")
    private Long paymentSelfPay;

    // 보험사 지원 금액
    @Column(name = "payment_insurance_money")
    private Long paymentInsuranceMoney;

    // 현재 수납액
    @Column(name = "payment_current_money")
    private Long paymentCurrentMoney;  // 지금까지 결제된 총 금액

    // 이번 수납액
    @Column(name = "payment_amount")
    private Long paymentAmount;  // 이번에 결제한 금액

    // 남은 수납액
    @Column(name = "payment_remain_money")
    private Long paymentRemainMoney;

    // 수납일 (자동 기록)
    @CreationTimestamp
    @Column(name = "payment_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    // 결제 수단 (카드, 현금 등등)
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

}
