package com.cloud.emr.Affair.Disability.entity;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity (name = "Disability")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DisabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disability_id", nullable = false)
    private long disabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;  // 환자 정보와 연관

    // 장애 등급 (1급, 2급, 3급 등)
    @Column(name = "disability_grade", nullable = false)
    private String disabilityGrade;

    // 장애 유형 (예: 신체장애, 정신장애, 시각장애, 청각장애 등)
    @Column(name = "disability_type")
    private String disabilityType;

    // 보조기기 필요 여부 (YN)
    @Column(name = "assistive_device_YN", nullable = false)
    private String disabilityDeviceYN;

    // 보조기기 종류 (예: 휠체어, 보청기 등)
    @Column(name = "disability_device_type")
    private String disabilityDeviceType;

}
