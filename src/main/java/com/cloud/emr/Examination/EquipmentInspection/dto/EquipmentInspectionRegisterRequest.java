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
public class EquipmentInspectionRegisterRequest {

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private Long equipmentId;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String equipmentName;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String equipmentProductNumber;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String equipmentManufacturer;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String equipmentLocation;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private Long userId;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String userName;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date equipmentInspectionDate;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String equipmentInspectionResult;

    @Column(nullable = false)
    @NotBlank(message = "필수 값입니다.")
    private String equipmentInspectionRecords;

    private String equipmentInspectionNotes;

}