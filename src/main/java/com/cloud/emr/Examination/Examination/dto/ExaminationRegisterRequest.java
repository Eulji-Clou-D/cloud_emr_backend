package com.cloud.emr.Examination.Examination.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationRegisterRequest {

    private Long equipmentId;

    private String equipmentName;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String examinationName;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String examinationType;

    private String examinationConstraints;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String examinationLocation;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String examinationPrice;
}
