package com.cloud.emr.Affair.Disability.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DisabilityRegisterRequest {

    @NotBlank(message = "환자번호는 필수 값입니다.")
    @Size(min = 8, max = 8, message = "환자번호는 반드시 8자리여야 합니다.")
    @Column(nullable = false, length = 8)
    private Long patientNo;  // 환자 번호

    @NotBlank(message = "장애 등급은 필수 값입니다.")
    private String disabilityGrade;  // 장애 등급

    @NotBlank(message = "장애 유형은 필수 값입니다.")
    private String disabilityType;  // 장애 유형

    @NotBlank(message = "보조기기 필요 여부는 필수 값입니다.")
    private String disabilityDeviceYN;  // 보조기기 필요 여부 (YN)

    private String disabilityDeviceType;  // 보조기기 종류 (예: 휠체어, 보청기 등)

}
