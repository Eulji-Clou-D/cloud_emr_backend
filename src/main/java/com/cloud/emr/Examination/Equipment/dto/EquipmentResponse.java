package com.cloud.emr.Examination.Equipment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponse {

    private Long equipmentId;

    private String equipmentName;

    private String equipmentProductNumber;

    private String equipmentManufacturer;

    private String equipmentLocation;

    private String equipmentState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date equipmentSchedule;

}
