package com.cloud.emr.Main.Department.entity;

import com.cloud.emr.Main.Department.type.DepartmentType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DepartmentEntity {

    @Id
    private Long Id;

    private String departmentName;

    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;
}
