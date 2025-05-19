package com.cloud.emr.Examination.Equipment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "Equipment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @Column(name = "equipment_product_number", nullable = false)
    private String equipmentProductNumber;

    @Column(name = "equipment_manufacturer", nullable = false)
    private String equipmentManufacturer;

    @Column(name = "equipment_location", nullable = false)
    private String equipmentLocation;

    @Column(name = "equipment_state", nullable = false)
    private String equipmentState;

    @Column(name = "equipment_schedule")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date equipmentSchedule;

    // 이 아래는 그냥 다른 테이블에서 가져와서 화면에 띄울까?
}
