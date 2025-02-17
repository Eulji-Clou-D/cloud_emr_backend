package com.cloud.emr.Affair.MedicalFee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalTypeResponse {
    private Long medicalTypeId;
    private String medicalTypeName;
    private Long medicalTypeFee;
}
