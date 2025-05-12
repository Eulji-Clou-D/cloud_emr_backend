package com.cloud.emr.Affair.DoctorTreatment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DoctorTreatmentRequest {
    private Long doctorTreatmentId;
    private Long patientNo;
    private Long userId;
    private LocalDateTime doctorTreatmentStart;
    private LocalDateTime doctorTreatmentEnd;
}
