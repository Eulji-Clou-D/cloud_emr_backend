package com.cloud.emr.Examination.ExaminationResult.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResultUpdateRequest {

    private Long equipmentId;

    private String examinationName;

    private String examinationType;

    private String examinationConstraints;

    private String examinationLocation;

    private String examinationPrice;
}