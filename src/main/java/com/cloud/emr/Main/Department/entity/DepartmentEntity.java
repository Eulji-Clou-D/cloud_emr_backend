package com.cloud.emr.Main.Department.entity;

import com.cloud.emr.Main.Department.type.DepartmentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "department")
@Getter
@Setter
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,name = "department_code")
    private String code; //약어

    @Column(unique = true,name = "department_name")
    private String name;

    @Column(unique = true,name = "department_eng_name")
    private String EngName;

    @Column(name = "department_type")
    @Enumerated(EnumType.STRING)
    private DepartmentType type;
}
