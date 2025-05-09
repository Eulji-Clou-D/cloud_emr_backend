package com.cloud.emr.Main.Department.controller;

import com.cloud.emr.Main.Department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DepartmentController {

    private DepartmentService departmentService;

}
