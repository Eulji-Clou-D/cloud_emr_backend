package com.cloud.emr.Affair.MedicalFee.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MedicalTypeRequest {
    private String medicalTypeName;
    private Long medicalTypeFee;
}
