package com.cloud.emr.Main.Department.controller;

import com.cloud.emr.Main.Core.common.annotation.AuthRole;
import com.cloud.emr.Main.Core.common.annotation.AuthUser;
import com.cloud.emr.Main.Department.dto.DepartmentResponse;
import com.cloud.emr.Main.Department.service.DepartmentService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("/api/departments")
@RestController
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/all")
     public List<DepartmentResponse> getDepartments() {
        return departmentService.getAllDepartments();
    }
}
