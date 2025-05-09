package com.cloud.emr.Main.Department.dto;

import com.cloud.emr.Main.Department.type.DepartmentType;
import lombok.Getter;

@Getter
public class DepartmentResponse {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private String departmentNameEng;
    private String departmentType;

    public DepartmentResponse(Long id, String departmentCode, String departmentName, String departmentNameEng, DepartmentType departmentType) {
        this.id = id;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.departmentNameEng = departmentNameEng;
        this.departmentType = departmentType.toString();
    }

}
