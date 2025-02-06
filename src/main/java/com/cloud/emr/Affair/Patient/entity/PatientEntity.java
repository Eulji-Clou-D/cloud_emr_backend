package com.cloud.emr.Affair.Patient.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "Patient")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientEntity {
    //환자번호
    @Id
//    @Size(min=8, max=8, message = "환자번호는 반드시 8자리여야 합니다.")
    @Column(name = "patient_no", nullable = false, unique = true)

    private Long patientNo;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    //환자 주민번호

    @Column(name = "patient_rrn", nullable = false, unique = true)
    private String patientRrn;

    @Column(name = "patient_gender", nullable = false)

    private String patientGender;

    @Column(name = "patient_birth")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date patientBirth;



    @Column(name = "patient_address")
    private String patientAddress;

    @Column(name = "patient_email", nullable = false, unique = true)
    private String patientEmail;

    @Column(name = "patient_tel", nullable = false, unique = true)
    private String patientTel;

    @Column(name = "patient_foreign", nullable = false)
    @Size(min=1, max=2, message = "Y/N")
    private String patientForeign;

    //여권 번호
    @Column(name = "patient_passport", unique = true)
    private String patientPassport;

    @Column(name = "patient_hypass_YN")
    @Size(min=1, max=2, message = "Y/N")
    private String patientHypassYN;

    @CreationTimestamp
    @Column(name = "patient_last_visit")
    private LocalDate patientLastVisit;

    //보호자 이름
    @Column(name = "patient_guardian")
    private String guardian;


}
