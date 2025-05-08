package com.cloud.emr.Affair.DoctorTreatment.entity;


import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "DoctorTreatment")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTreatmentEntity {
    @Id
    @Column(name = "doctorTreatment_id", nullable = false, unique = true)
    private Long doctorTreatmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "doctorTreatment_starttime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime doctorTreatmentStart;  //진료 시작 시간

    @Column(name = "doctorTreatment_endtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime doctorTreatmentEnd;  //진료 종료 시간
}
