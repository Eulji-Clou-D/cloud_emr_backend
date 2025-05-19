package com.cloud.emr.Examination.Examination.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;

@Entity(name = "Examination_Result")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_result_id", nullable = false)
    private Long examinationResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_id", referencedColumnName = "examination_id", nullable = false)
    private ExaminationEntity examinationEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id", nullable = false)
    private TreatmentEntity treatmentEntity;

    @Column(name = "examination_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date examinationDate;

    @Column(name = "examination_result")
    private String examinationResult;

    // 정상 수치인지
    @Column(name = "examination_normal")
    private Boolean examinationNormal;

    @Column(name = "examination_notes")
    private String examinationNotes;

    // 이 아래는 그냥 다른 테이블에서 가져와서 화면에 띄울까?
//    @Column(name = "examination_name")
//    private String examinationName;
//
//    @Column(name = "examination_type")
//    private String examinationType;
//
//    @Column(name = "patient_name")
//    private String patientName;
}
