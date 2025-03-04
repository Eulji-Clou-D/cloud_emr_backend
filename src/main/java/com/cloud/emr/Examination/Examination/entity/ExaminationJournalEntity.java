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
// TODO: Make Doctor Entity
// import com.cloud.emr.Main.User.entity.DoctorEntity;
import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;

@Entity(name = "Examination_Journal")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationJournalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_journal_id", nullable = false)
    private Long examinationJournalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_id", referencedColumnName = "examination_id", nullable = false)
    private ExaminationEntity ExaminationEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", referencedColumnName = "treatment_id", nullable = false, columnDefinition = "INT")
    private TreatmentEntity TreatmentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity UserEntity;

    // TODO: Make Doctor Entity
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id", nullable = false)
//    private DoctorEntity DoctorEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id", nullable = false)
    private EquipmentEntity EquipmentEntity;

    @Column(name = "examination_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date examinationTime;

    @Column(name = "examination_equipment_usage")
    private Boolean examinationEquipmentUsage;

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
//
//    @Column(name = "User_name")
//    private String UserName;
//
//    @Column(name = "Doctor_name")
//    private String DoctorName;
//
//    @Column(name = "examination_equipment")
//    private String examinationEquipment;
}
