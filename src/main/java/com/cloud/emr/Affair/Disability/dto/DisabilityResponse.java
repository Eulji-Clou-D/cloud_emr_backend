package com.cloud.emr.Affair.Disability.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DisabilityResponse {

    private Long disabilityId;
    private long patientNo;
    private String patientName;
    private String disabilityGrade;
    private String disabilityType;
    private String disabilityDeviceYN;
    private String disabilityDeviceType;

}
