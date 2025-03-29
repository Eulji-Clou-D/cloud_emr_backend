package com.cloud.emr.Examination.ExaminationSchedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationScheduleResponse {

    private Long examinationId;

    private Long equipmentId;

    private String equipmentName;

    private String examinationName;

    private String examinationType;

    private String examinationConstraints;

    private String examinationLocation;

    private String examinationPrice;

}
