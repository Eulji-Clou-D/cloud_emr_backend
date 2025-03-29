package com.cloud.emr.Examination.ExaminationJournal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationJournalUpdateRequest {

    private Long equipmentId;

    private String examinationName;

    private String examinationType;

    private String examinationConstraints;

    private String examinationLocation;

    private String examinationPrice;
}