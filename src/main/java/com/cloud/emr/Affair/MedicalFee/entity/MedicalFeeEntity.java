package com.cloud.emr.Affair.MedicalFee.entity;


import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "medical_fee")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalFeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_fee_id", nullable = false, columnDefinition = "INT")
    private Long medicalFeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_type_id", referencedColumnName = "medical_type_id", nullable = false, columnDefinition = "INT")
    private MedicalTypeEntity medicalTypeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id")
    private TreatmentEntity treatmentEntity;
}
