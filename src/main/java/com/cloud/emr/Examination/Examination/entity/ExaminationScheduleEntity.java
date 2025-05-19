package com.cloud.emr.Examination.Examination.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Main.User.entity.UserEntity;

@Entity(name = "Examination_Schedule")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_schedule_id", nullable = false)
    private Long examinationScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_id", referencedColumnName = "examination_id", nullable = false)
    private ExaminationEntity examinationEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id", nullable = false)
    private TreatmentEntity treatmentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "examination_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date examinationDate;

    // 이 아래는 그냥 다른 테이블에서 가져와서 화면에 띄울까?
//    @Column(name = "examination_name")
//    private String examinationName;
//
//    @Column(name = "examination_type")
//    private String examinationType;
//
//    @Column(name = "patient_name")
//    private String patientName;
//
//    @Column(name = "User_name")
//    private String UserName;
//
//    @Column(name = "examination_location")
//    private String examinationLocation;
}
