package com.cloud.emr.Examination.EquipmentInspection.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentInspectionResponse {

    private Long equipmentId;

    private String equipmentName;

    private String equipmentProductNumber;

    private String equipmentManufacturer;

    private String equipmentLocation;

    private Long userId;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date equipmentInspectionDate;

    private String equipmentInspectionResult;

    private String equipmentInspectionRecords;

    private String equipmentInspectionNotes;

}
