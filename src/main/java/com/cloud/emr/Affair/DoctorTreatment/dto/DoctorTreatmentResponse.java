package com.cloud.emr.Affair.DoctorTreatment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTreatmentResponse {
    private Long doctorTreatmentId;
    private Long patientNo;
    private Long userId;
    private LocalDateTime doctorTreatmentStart;
    private LocalDateTime doctorTreatmentEnd;
}
